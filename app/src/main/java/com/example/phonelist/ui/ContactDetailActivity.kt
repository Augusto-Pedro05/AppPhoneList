package com.example.phonelist.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.phonelist.R
import com.example.phonelist.database.DBHelper
import com.example.phonelist.databinding.ActivityContactDetailBinding
import com.example.phonelist.model.ContactModel

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db : DBHelper
    private var contactModel = ContactModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        /*
        Busca a intent criada na MainActivity e exibe as informações do contato selecionado para que possa ser editado
         */
        val i = intent
        val id = i.extras?.getString("id")
        db = DBHelper(applicationContext)
        if (id != null) {
            contactModel = db.getContact(id.toInt())

            binding.editName.setText(contactModel.name)
            binding.editAddress.setText(contactModel.address)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone.toString())
        }

        binding.buttonSave.setOnClickListener {
            val res = db.updateContact(
                id = contactModel.id,
                name = binding.editName.text.toString(),
                address = binding.editAddress.text.toString(),
                email = binding.editEmail.text.toString(),
                phone = binding.editPhone.text.toString().toInt(),
                imageId = contactModel.imageId
            )

            if (res > 0) {
                Toast.makeText(applicationContext, getString(R.string.update_ok), Toast.LENGTH_SHORT).show()
                setResult(1,i)
                finish()
            }else{
                Toast.makeText(applicationContext,
                    getString(R.string.update_error), Toast.LENGTH_SHORT).show()
                setResult(0,i)
                finish()
            }
        }
        binding.buttonCancel.setOnClickListener {
            binding.editName.setText(contactModel.name)
            binding.editAddress.setText(contactModel.address)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone.toString())
            finish()
        }
        binding.buttonDelete.setOnClickListener {
            val res = db.deleteContact(contactModel.id)

            if (res > 0) {
                Toast.makeText(applicationContext, getString(R.string.delete_ok), Toast.LENGTH_SHORT).show()
                setResult(1,i)
                finish()
            }else{
                Toast.makeText(applicationContext,
                    getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
                setResult(0,i)
                finish()
            }
        }
    }
}