package com.example.coffetec

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.coffetec.databinding.ActivityRegistrationBinding
import com.example.coffetec.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private val binding:ActivityRegistrationBinding by lazy {
        ActivityRegistrationBinding.inflate(layoutInflater)
    }

    private var URI:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailSignUpET.text.toString()
            val pass = binding.passwordSignUpET.editText!!.text.toString()
            if(binding.nameET.text.toString() != "" && binding.houseCampET.text.toString() != "" && binding.documentET.text.toString() != ""
                && email != "" && binding.phoneET.text.toString() != "" && pass != "" && binding.passwordConfirmET.editText!!.text.toString() != ""){

                if(pass == binding.passwordConfirmET.editText!!.text.toString()){
                    if (URI != "") {
                        Firebase.auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {
                            Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_LONG).show()
                            registerUserData()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Algo fallo: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(this, "Selecciona una foto de perfil", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Los campos de contraseña deben coincidir", Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show()
            }
        }

        val gallerylauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            gallerylauncher.launch(intent)
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

    private fun registerUserData() {
        val uid = Firebase.auth.currentUser?.uid
        Log.e(
            "<--<<<","${uid}"
        )
        uid?.let {
            val user = User(
                uid,
                binding.nameET.text.toString(),
                binding.houseCampET.text.toString(),
                binding.documentET.text.toString(),
                binding.emailSignUpET.text.toString(),
                binding.phoneET.text.toString(),
                URI
            )
            Firebase.firestore.collection("users").document(user.id).set(user).addOnSuccessListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    /**fun onCameraResult(result: ActivityResult){
        // val bitmap = result.data?.extras?.get("data") as Bitmap
        // binding.photoIMG.setImageBitmap(bitmap)
        if(result.resultCode == Activity.RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(file?.path)
            val thumball = Bitmap.createScaledBitmap(bitmap, bitmap.width/4,bitmap.height/4,true)
            binding.imgPerfil.setImageBitmap(thumball)
        }else if(result.resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(activity,"No tomo la foto", Toast.LENGTH_SHORT).show()
        }
    }**/

    fun onGalleryResult(result: ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            URI = result.data?.data.toString()
        }
    }
}