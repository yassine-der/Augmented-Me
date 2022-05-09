package com.example.augmentedme.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.augmentedme.Model.User
import com.example.augmentedme.R
import com.example.augmentedme.api.RetrofiteInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    lateinit var nom : TextView
    lateinit var prenom : TextView
    lateinit var button: Button
    lateinit var email : TextView
    lateinit var imageView: ImageView
    lateinit var mSharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  rootView = inflater.inflate(R.layout.profile, container, false)
        nom = rootView.findViewById(R.id.profileNom)
        prenom = rootView.findViewById(R.id.profilePrenom)
        email = rootView.findViewById(R.id.profileEmail)
        button = rootView.findViewById(R.id.button23)
        imageView = rootView.findViewById(R.id.profileImage)
        button.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment()
            findNavController().navigate(action)

        }
        mSharedPref = requireActivity().getSharedPreferences("LOGIN_PREF", AppCompatActivity.MODE_PRIVATE);
        val idUser: String = mSharedPref.getString("USER_ID", null).toString()

        val apiInterface = RetrofiteInstance.api(context)
        apiInterface.getProfile(idUser).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Probleme de connection", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    val user : User = response.body()!!
                    nom.text = user.nom
                    prenom.text = user.prenom
                    email.text = user.userName
                    val replaced = user.image!!.replace("\\", "/")

                    Glide.with(this@ProfileFragment).load(RetrofiteInstance.BASE_URL + replaced).into(imageView)


                }
            }


        })


        return rootView
    }

}