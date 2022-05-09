package com.example.augmentedme.Fragment

import android.app.DatePickerDialog
import android.content.Intent
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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.augmentedme.Model.Passage
import com.example.augmentedme.Model.Patch
import com.example.augmentedme.R
import com.example.augmentedme.api.RetrofiteInstance
import com.example.augmentedme.unityActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import java.util.*

const val PREF_NAME = "LOGIN_PREF"
const val PATCHID = "PATCHID"

class detailsPatchFragment : Fragment() {
    private  val args : detailsPatchFragmentArgs by navArgs()
    lateinit var mSharedPref : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_details_patch, container, false)
        val imageDetailsPatch : ImageView = rootView.findViewById(R.id.imageView2)
        val nomDetailsPatch : TextView = rootView.findViewById(R.id.textView2)
        val date : TextView = rootView.findViewById(R.id.textView3)
        val descriptionDetailsStade : TextView = rootView.findViewById(R.id.textView4)
        val buttonMap : Button = rootView.findViewById(R.id.button11)
        val apiInterface = RetrofiteInstance.api(context)
        apiInterface.getPatchByid(args.idPatch).enqueue(object : Callback<Patch> {
            override fun onFailure(call: Call<Patch>, t: Throwable) {
                Toast.makeText(context, "Probleme de connection", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<Patch>, response: Response<Patch>) {
                if (response.isSuccessful){
                    val patch : Patch = response.body()!!
                    println("8888888888888888888888////////////////////////////////////////////////")
                    println(patch.corY)
                    println("888888888888888////////////////////////////////////////////////")

                    nomDetailsPatch.text = patch.nom
                     //var id_patchs : String = patch._id!!
                    mSharedPref = requireActivity().getSharedPreferences("LOGIN_PREF", AppCompatActivity.MODE_PRIVATE);
                    mSharedPref.edit().apply{
                        putString("PATCHID",patch._id)

                    }.apply()
                    val id_patchss: String = mSharedPref.getString("PATCHID", null).toString()

                    val nomm = id_patchss.trim()

                    val apiInterface = RetrofiteInstance.api(context)

                    var patch1 = Patch()

                    apiInterface.doUpPatch(
                        args.idPatch
                    ).enqueue(object :
                        Callback<Patch> {

                        override fun onResponse(call: Call<Patch>, response: Response<Patch>) {

                            Toast.makeText(context, "Update Success A", Toast.LENGTH_SHORT).show()



                        }

                        override fun onFailure(call: Call<Patch>, t: Throwable) {

                            Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                        }

                    })
                    apiInterface.doUpPatchx(
                        args.idPatch
                    ).enqueue(object :
                        Callback<Patch> {

                        override fun onResponse(call: Call<Patch>, response: Response<Patch>) {

                            Toast.makeText(context, "Update Success B", Toast.LENGTH_SHORT).show()



                        }

                        override fun onFailure(call: Call<Patch>, t: Throwable) {

                            Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                        }

                    })
                    apiInterface.doUpPatchy(
                        args.idPatch
                    ).enqueue(object :
                        Callback<Patch> {

                        override fun onResponse(call: Call<Patch>, response: Response<Patch>) {

                            Toast.makeText(context, "Update Success C", Toast.LENGTH_SHORT).show()



                        }

                        override fun onFailure(call: Call<Patch>, t: Throwable) {

                            Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                        }

                    })
                    //id_patchs = patch._id!!
                    nomDetailsPatch.text = patch.nom
                    date.text = patch.date
                    descriptionDetailsStade.text = patch.description
                    val replaced = patch.image!!.replace("\\", "/")

                    Glide.with(this@detailsPatchFragment).load(RetrofiteInstance.BASE_URL + replaced).into(imageDetailsPatch)




                }
            }


        })
        buttonMap.setOnClickListener {

            val apiInterface = RetrofiteInstance.api(context)


            var passage = Passage()
            val id_patchs: String = mSharedPref.getString("PATCHID", null).toString()

            val nom = id_patchs.trim()
            passage.image_id = args.idPatch

            println("popopopopoppoppopopo////////////////////////////////////////////////")
            //println(args.idPatch)
            println(passage.image_id)
            println("popopopopoppoppopopo////////////////////////////////////////////////")

            apiInterface.doPassage(

                "62758afa740bc57a4a559bfd", passage
            ).enqueue(object :
                Callback<Passage> {

                override fun onResponse(call: Call<Passage>, response: Response<Passage>) {

                    val passage = response.body()




                    Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context!!, unityActivity::class.java)
                    startActivity(intent)


                }

                override fun onFailure(call: Call<Passage>, t: Throwable) {

                    Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                }

            })

        }
        return rootView
    }

}