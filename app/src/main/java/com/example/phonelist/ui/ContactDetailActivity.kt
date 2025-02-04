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
import com.example.phonelist.databinding.ActivityContactDetailBinding
import com.example.phonelist.model.ContactModel

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db : DBHelper
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var contactModel = ContactModel()
    private var imageId: Int? = -1

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
            if(contactModel.imageId > 0){
                binding.imageContact.setImageDrawable(resources.getDrawable(contactModel.imageId))
            }else{
                binding.imageContact.setImageResource(R.drawable.contacts)
            }
        }

        /*
        Salva as alterações feitas no contato
         */
        binding.buttonSave.setOnClickListener {
            val res = imageId?.let { it1 ->
                db.updateContact(
                    id = contactModel.id,
                    name = binding.editName.text.toString(),
                    address = binding.editAddress.text.toString(),
                    email = binding.editEmail.text.toString(),
                    phone = binding.editPhone.text.toString().toInt(),
                    imageId = it1
                )
            }
            if (res != null) {
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
        }
        /*
        Cancela a ação de salvar as alterações
         */
        binding.buttonCancel.setOnClickListener {
            binding.editName.setText(contactModel.name)
            binding.editAddress.setText(contactModel.address)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone.toString())
            finish()
        }
        /*
        Deleta o contato
         */
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
                imageId = it.data?.extras?.getInt("id")
                binding.imageContact.setImageDrawable(resources.getDrawable(imageId!!))
            }else{
                imageId = -1
                binding.imageContact.setImageResource(R.drawable.contacts)
            }
        }
    }
}