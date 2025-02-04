package com.example.phonelist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.phonelist.R
import com.example.phonelist.database.DBHelper
import com.example.phonelist.databinding.ActivityNewContactBinding

class NewContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewContactBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var id: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val i = intent
        val db = DBHelper(applicationContext)

        /*
        Salva um contato no banco de dados
         */
        binding.buttonSave.setOnClickListener {
            val name = binding.editName.text.toString()
            val address = binding.editAddress.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString().toInt()
            var imageId = -1
            if(id != null){
                imageId = id as Int
            }

            if(name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty()){
                val res = db.insertContact(name, address, email, phone, imageId)
                if(res > 0){
                    Toast.makeText(applicationContext,
                        getString(R.string.insert_ok), Toast.LENGTH_SHORT).show()
                    setResult(1,i)
                    finish()
                }else{
                    Toast.makeText(applicationContext,
                        getString(R.string.insert_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        /*
        Cancela a ação de salvar o contato
         */
        binding.buttonCancel.setOnClickListener {
            setResult(0,i)
            finish()
        }

        /*
        Abre a tela de seleção de imagem
         */
        binding.imageContact.setOnClickListener {
            launcher.launch(Intent(applicationContext, ContactImageSelectionActivity::class.java))
        }

        /*
        Recebe o resultado da tela de seleção de imagem
         */
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == 1){
                id = it.data?.extras?.getInt("id")
                binding.imageContact.setImageDrawable(resources.getDrawable(id!!))
            }else{
                id = -1
                binding.imageContact.setImageResource(R.drawable.contacts)
            }
        }
    }
}