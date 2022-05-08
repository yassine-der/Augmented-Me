package com.example.augmentedme.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.augmentedme.Model.User
import com.example.augmentedme.R
import com.example.augmentedme.api.RetrofiteInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class updateProfileFragment : Fragment() {
    lateinit var lastNameUpdate: EditText

    lateinit var firstNameUpdate: EditText
    lateinit var usertNameUpdate: EditText


    lateinit var confirmePasswordUpdate: EditText

    lateinit var passwordUpdate: EditText

    lateinit var imageUpdate: ImageView

    lateinit var updatebutton: Button



    lateinit var mSharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.fragment_update_profile, container, false)
        // Inflate the layout for this fragment
        lastNameUpdate = rootView.findViewById(R.id.lastName1)
        firstNameUpdate = rootView.findViewById(R.id.Firstname122)
        usertNameUpdate = rootView.findViewById(R.id.userName22)
        passwordUpdate = rootView.findViewById(R.id.inputPassword1133)
        confirmePasswordUpdate = rootView.findViewById(R.id.inputPassword222)
        passwordUpdate = rootView.findViewById(R.id.inputPassword1133)
        updatebutton = rootView.findViewById(R.id.button33)
        imageUpdate = rootView.findViewById(R.id.imageView4)
        mSharedPref = requireActivity().getSharedPreferences("LOGIN_PREF", AppCompatActivity.MODE_PRIVATE);
        val nomStr: String = mSharedPref.getString("NOM", null).toString()
        val prenomStr: String = mSharedPref.getString("PRENOM", null).toString()
        //val imageStr: String = mSharedPref.getString("IMAGE2", null).toString()
        val idUser: String = mSharedPref.getString("USER_ID", null).toString()
        //Glide.with(this).load(RetrofiteInstance.BASE_URL + imageStr).into(imageUpdate)

        updatebutton.setOnClickListener {

            val nom = lastNameUpdate.text.toString().trim()
            val prenom = firstNameUpdate.text.toString().trim()
            val password = passwordUpdate.text.toString().trim()
            val confirmPassword = confirmePasswordUpdate.text.toString().trim()

            if (validate()) {
                //val apiInterface = UserApi.create()
                val apiInterface = RetrofiteInstance.api(context)
                //progBar.visibility = View.VISIBLE

                var user = User()
                user.nom = nom
                user.prenom = prenom
                user.password = password
                apiInterface.updateProfile(
                    user
                ).enqueue(object :
                    Callback<User> {

                    override fun onResponse(call: Call<User>, response: Response<User>) {

                        val user = response.body()



                        Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
                        val action = updateProfileFragmentDirections.actionUpdateProfileFragmentToProfileFragment()
                        findNavController().navigate(action)


                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {

                        Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                    }

                })

            }



        }

        return rootView
    }
    private fun validate(): Boolean {
        lastNameUpdate.error = null
        usertNameUpdate.error = null
        confirmePasswordUpdate.error = null
        firstNameUpdate.error = null
        passwordUpdate.error = null

        if (lastNameUpdate.text!!.isEmpty()) {
            lastNameUpdate.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        if (firstNameUpdate.text!!.isEmpty()) {
            firstNameUpdate.error = getString(R.string.mustNotBeEmpty)
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

        return true
    }



}