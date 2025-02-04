package com.example.phonelist.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.phonelist.R
import com.example.phonelist.database.DBHelper
import com.example.phonelist.databinding.ActivityMainBinding
import com.example.phonelist.model.ContactModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactList: List<ContactModel>
    private lateinit var adapter: ArrayAdapter<ContactModel>
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        val sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)
        loadList() // Carrega a lista de contatos

        /*
        Esta ação tem o efeito de logout, onde o usuario é redirecionado para a LoginActivity e o sharedPreferences é limpo
         */
        binding.buttonLogout.setOnClickListener {
            val editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("username", "")
            editor.apply()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        /*
        Ao clicar em algum item se é redirecionado para a ContactDetailActivity, onde vai ter mais detalhes sobre o contato
         */
        binding.listViewContacts.setOnItemClickListener{_, _, position, _ ->
            val intent = Intent(this, ContactDetailActivity::class.java)
            intent.putExtra("id",contactList[position].id.toString())
            result.launch(intent)
        }

        /*
        Redireciona para a tela de adicionar novo contato - NewContactActivity
         */
        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext, NewContactActivity::class.java))
        }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.data!=null && it.resultCode == 1){
                loadList()
            }else if(it.data!=null && it.resultCode == 0){
                Toast.makeText(applicationContext, "Operation Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /*
    Listagem visual dos contatos na tela
     */
    private fun loadList(){
        contactList = dbHelper.getAllContact()
        adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            contactList
        )
        binding.listViewContacts.adapter = adapter
    }
}