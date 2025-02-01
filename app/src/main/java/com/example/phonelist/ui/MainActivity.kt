package com.example.phonelist.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
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

        val dbHelper = DBHelper(this)
        val sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)

        /*
        Esta ação tem o efeito de logout, onde o usuario é redirecionado para a LoginActivity e o sharedPreferences é limpo
         */
        binding.buttonLogout.setOnClickListener {
            val editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("username", "")
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Listagem visual dos contatos na tela
        contactList = dbHelper.getAllContact()
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            contactList
        )
        binding.listViewContacts.adapter = adapter

        /*
        Ao clicar em algum item ...
         */
        binding.listViewContacts.setOnItemClickListener{_, _, position, _ ->
            val intent = Intent(applicationContext, ContactDetailActivity::class.java)
            intent.putExtra("id",contactList[position].id.toString())
            startActivity(intent)
        }

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(applicationContext, NewContactActivity::class.java))
        }
    }
}