package com.example.augmentedme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.augmentedme.Model.User
import com.example.augmentedme.api.RetrofiteInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val PREF_NAME = "LOGIN_PREF"
const val USERNAME = "USERNAME"
const val NOM = "NOM"
const val PRENOM = "PRENOM"
//const val IMAGE2 = "IMAGE2"
const val PASSWORD = "PASSWORD"
const val TOKEN = "TOKEN"
const val USER_ID = "USER_ID"
const val IS_REMEMBRED = "IS_REMEMBRED"


class Login : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var checkBox: CheckBox

    lateinit var passwordEditText: EditText

    lateinit var loginButton: Button
    lateinit var registerButton: TextView
    //lateinit var progBar: CircularProgressIndicator
    lateinit var  sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailEditText = findViewById(R.id.inputEmail)

        passwordEditText = findViewById(R.id.inputPassword)

        checkBox = findViewById(R.id.checkBox)
        loginButton = findViewById(R.id.btnAdd111)
        registerButton = findViewById(R.id.gotoRegister)
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        //progBar = findViewById(R.id.progressBar)

        if (sharedPreferences.getBoolean(IS_REMEMBRED, false)){
            navigate()
        }
        registerButton.setOnClickListener {
            navigateRegister()
        }
        loginButton.setOnClickListener {
            doLogin()
        }

    }


    private fun doLogin() {
        if (validate()) {
            //val apiInterface = UserApi.create()
            val apiInterface = RetrofiteInstance.api(this)
            //progBar.visibility = View.VISIBLE

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            var user = User()
            user.userName = emailEditText.text.toString()
            user.password = passwordEditText.text.toString()
            apiInterface.seConnecter(
                user
            ).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    if (user != null) {

                        if (checkBox.isChecked){
                            //TODO 4 "Edit the SharedPreferences by putting all the data"
                            sharedPreferences.edit().apply{
                                putBoolean("IS_REMEMBRED", true)
                                putString("USERNAME",user.userName)
                                putString("NOM",user.nom)
                                putString("PRENOM",user.prenom)
                                putString("TOKEN", user.token)
                                putString("USER_ID", user._id)

                            }.apply()

                        }else{
                            sharedPreferences.edit().apply{
                                putString("USERNAME",user.userName)
                                putString("TOKEN", user.token)
                                putString("NOM",user.nom)
                                putString("PRENOM",user.prenom)
                                putString("USER_ID", user._id)

                            }.apply()

                        }

                        Toast.makeText(this@Login, "Login Success", Toast.LENGTH_SHORT).show()
                        navigate()
                    } else {
                        Toast.makeText(this@Login, "User not found", Toast.LENGTH_SHORT).show()
                    }

                    //progBar.visibility = View.INVISIBLE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {

                    Toast.makeText(this@Login, "Connexion error!", Toast.LENGTH_SHORT).show()

                    //progBar.visibility = View.INVISIBLE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            })

        }
    }

    private fun validate(): Boolean {
        emailEditText.error = null
        passwordEditText.error = null

        if (emailEditText.text!!.isEmpty()) {
            emailEditText.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        if (passwordEditText.text!!.isEmpty()) {
            emailEditText.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        return true
    }
    private fun navigate(){
        val i = Intent(this,MainActivityhome::class.java)
        startActivity(i)
        finish()
    }
    private fun navigateRegister(){
        val l = Intent(this,register::class.java)
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