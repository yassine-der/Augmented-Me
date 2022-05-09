package com.example.augmentedme

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.augmentedme.Model.User
import com.example.augmentedme.api.RetrofiteInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class register : AppCompatActivity() {
    lateinit var lastNameUpdate: EditText

    lateinit var firstNameUpdate: EditText
    lateinit var usertNameUpdate: EditText


    lateinit var confirmePasswordUpdate: EditText

    lateinit var passwordUpdate: EditText


    lateinit var registerButton: Button
    //lateinit var registerImageButton: Button
    private var selectedImageUri: Uri? = null
    var imagePicker: ImageView?=null
    private lateinit var fab: FloatingActionButton
    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    lateinit var imageRegister: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        lastNameUpdate = findViewById(R.id.lastnamme)
        firstNameUpdate = findViewById(R.id.premierNom)
        usertNameUpdate = findViewById(R.id.usernamee)
        passwordUpdate = findViewById(R.id.motdepassse2)
        confirmePasswordUpdate = findViewById(R.id.motdePasse)
        registerButton = findViewById(R.id.btnAdd111)
        imagePicker = findViewById(R.id.imageView666)
        fab = findViewById(R.id.floatingActionButton252)

        fab.setOnClickListener{
            pickImageFromGallery()
        }

        registerButton.setOnClickListener {

            val nom = firstNameUpdate.text.toString().trim()
            val prenom = lastNameUpdate.text.toString().trim()
            val email = usertNameUpdate.text.toString().trim()
            val password = passwordUpdate.text.toString().trim()
            val confirmPassword = confirmePasswordUpdate.text.toString().trim()


            if (selectedImageUri == null) {
                Toast.makeText(this@register, "Select an Image First ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return@setOnClickListener
            doRegister(
                nom,prenom,
                email,
                password,
            )
            print(parcelFileDescriptor);
        }
    }


private fun doRegister(nom: String, prenom: String, email: String, motdepasse: String){
    if (validate()) {

        if (selectedImageUri == null) {
            println("image null")
            Toast.makeText(this@register, "image null ", Toast.LENGTH_SHORT).show()


            return
        }


        val stream = contentResolver.openInputStream(selectedImageUri!!)
        println("-------------------------------------" + stream)
        val request =
            stream?.let {
                RequestBody.create(
                    "image/jpg".toMediaTypeOrNull(),
                    it.readBytes()
                )
            } // read all bytes using kotlin extension
        val image = request?.let {
            MultipartBody.Part.createFormData(
                "image",
                "image.jpg",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")

        val apiInterface = RetrofiteInstance.api(this)
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["nom"] = nom.toRequestBody(MultipartBody.FORM)
        data["prenom"] = prenom.toRequestBody(MultipartBody.FORM)
        data["userName"] = email.toRequestBody(MultipartBody.FORM)
        data["password"] = motdepasse.toRequestBody(MultipartBody.FORM)
        if (image?.body != null) {

            println("++++++++++++++++++++++++++++++++++++" + image)
            apiInterface.register(data, image).enqueue(object :
                Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        Log.i("onResponse goooood", response.body().toString())
                        //showAlertDialog()
                        Toast.makeText(this@register, "welcome ", Toast.LENGTH_SHORT).show()
                        navigateRegister()

                    } else {
                        Log.i("OnResponse not good", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    //progress_bar.progress = 0
                    Toast.makeText(this@register, "Connexion error!", Toast.LENGTH_SHORT).show()

                }

            })
        }
        else{
            Toast.makeText(this@register, "Choisir une image!", Toast.LENGTH_SHORT).show()

        }
    }
}

private fun pickImageFromGallery() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, IMAGE_REQUEST_CODE)
}


override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
        selectedImageUri = data?.data

        imagePicker?.setImageURI(selectedImageUri)
        fab.hide()

    }else{
        Toast.makeText(this@register, "image null ", Toast.LENGTH_SHORT).show()

    }
}







    private fun validate(): Boolean {
        selectedImageUri?:  Toast.makeText(this@register, "image null ", Toast.LENGTH_SHORT).show()


        lastNameUpdate.error = null
        usertNameUpdate.error = null
        confirmePasswordUpdate.error = null
        firstNameUpdate.error = null
        passwordUpdate.error = null

        if (firstNameUpdate.text!!.isEmpty()) {
            firstNameUpdate.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        if (lastNameUpdate.text!!.isEmpty()) {
            lastNameUpdate.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        if (confirmePasswordUpdate.text!!.isEmpty()) {
            confirmePasswordUpdate.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        if (usertNameUpdate.text!!.isEmpty()) {
            usertNameUpdate.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        if (passwordUpdate.text!!.isEmpty()) {
            passwordUpdate.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        // checking minimum password Length
        if (passwordUpdate.text.length < 8) {
            passwordUpdate.setError("Password Length must be more than " + 8 + "characters")
            return false
        }

        // Checking if repeat password is same
        if (!passwordUpdate.text.toString().equals(confirmePasswordUpdate.text.toString())) {
            confirmePasswordUpdate.setError("Password does not match")
            return false
        }

        return true
    }

    private fun navigateRegister(){
        val l = Intent(this,Login::class.java)
        startActivity(l)
        finish()

    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view: View? = currentFocus
        if (view != null && (ev.getAction() === MotionEvent.ACTION_UP || ev.getAction() === MotionEvent.ACTION_MOVE) && view is EditText
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x: Float = ev.getRawX() + view.getLeft() - scrcoords[0]
            val y: Float = ev.getRawY() + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }


}