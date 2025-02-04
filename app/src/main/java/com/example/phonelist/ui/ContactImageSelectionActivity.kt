package com.example.phonelist.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.phonelist.R
import com.example.phonelist.databinding.ActivityContactImageSelectionBinding

class ContactImageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactImageSelectionBinding
    private lateinit var i : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactImageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        i = intent

        binding.imageProfile1.setOnClickListener {sendID(R.drawable.profile1)}
        binding.imageProfile2.setOnClickListener {sendID(R.drawable.profile2)}
        binding.imageProfile3.setOnClickListener {sendID(R.drawable.profile3)}
        binding.imageProfile4.setOnClickListener {sendID(R.drawable.profile4)}
        binding.imageProfile5.setOnClickListener {sendID(R.drawable.profile5)}
        binding.imageProfile6.setOnClickListener {sendID(R.drawable.profile6)}
        binding.buttonRemoveImage.setOnClickListener {sendID(R.drawable.contacts)}
    }

    /*
    Retorna o id da imagem selecionada para a tela anterior
     */
    private fun sendID(id: Int){
        i.putExtra("id",id)
        setResult(1,i)
        finish()
    }
}