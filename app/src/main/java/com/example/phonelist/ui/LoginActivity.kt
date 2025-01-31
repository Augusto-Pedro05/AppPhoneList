package com.example.phonelist.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.phonelist.R
import com.example.phonelist.database.DBHelper
import com.example.phonelist.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DBHelper(this)

        sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)

        /*
        Salta para a MainActivity depois de uma serie de validações de login
        1) Verifica se os campos estão vazios
        2) Verifica se o usuário existe no banco de dados
        3) Verifica se o usuário quer ser logado
         */
        binding.buttonLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val logged = binding.checkboxLogged.isChecked

            if(username.isNotEmpty() && password.isNotEmpty()){
                if(db.login(username, password)){
                    if(logged){
                        val editor : SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("username", username)
                        editor.apply()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.login_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.editUsername.setText("")
                binding.editPassword.setText("")
            }else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_insert_all_required_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /*
        Salta para a SignUpActivity, onde será feito o registro do usuário
         */
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.textRecoverPassword.setOnClickListener {

        }
    }
}