package com.example.coffetec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.coffetec.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private val binding:ActivityRegistrationBinding by lazy {
        ActivityRegistrationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailSignUpET.text.toString()
            val pass = binding.passwordSignUpET.editText!!.text.toString()
            if(binding.nameET.text.toString() != "" && binding.houseCampET.text.toString() != "" && binding.documentET.text.toString() != ""
                && email != "" && binding.phoneET.text.toString() != "" && pass != "" && binding.passwordConfirmET.editText!!.text.toString() != ""){

                if(pass == binding.passwordConfirmET.editText!!.text.toString()){
                    Firebase.auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {
                        Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Algo fallo: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Los campos de contraseña deben coincidir", Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show()
            }
        }

        binding.signInTxt.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }
}