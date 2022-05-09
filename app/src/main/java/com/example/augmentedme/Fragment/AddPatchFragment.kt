package com.example.augmentedme.Fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.augmentedme.Model.Patch
import com.example.augmentedme.R
import com.example.augmentedme.api.RetrofiteInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.LinkedHashMap

class AddPatchFragment : Fragment() {
    private var selectedImageUri0: Uri? = null
    var imagePicker0: ImageView?=null
    private lateinit var fab2: FloatingActionButton


    lateinit var nameEditText: EditText

    lateinit var descriptionEditText: EditText

    //lateinit var dateInput: EditText
    lateinit var date: TextView

    lateinit var addPatchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_patch, container, false)
        nameEditText = rootView.findViewById(R.id.inputName1)
        date = rootView.findViewById(R.id.textView5)
        imagePicker0 = rootView.findViewById(R.id.imageView6)
        val button : Button = rootView.findViewById(R.id.button)
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        descriptionEditText = rootView.findViewById(R.id.inputdescription)
        //dateInput = rootView.findViewById(R.id.editTextDate)

        addPatchButton = rootView.findViewById(R.id.btnAdd111)
        fab2 = rootView.findViewById(R.id.floatingActionButton2)
        button.setOnClickListener {
            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->  date.setText("" + dayOfMonth +"/"+ month+"/"+year )},year,month,day)
            dpd.show()
        }


        fab2.setOnClickListener{
            pickImageFromGallery()
        }

        addPatchButton.setOnClickListener {
            if (selectedImageUri0 == null) {
                Toast.makeText(context!!, "Select an Image First ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val nom = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val date = date.text.toString().trim()
            var resolver = requireActivity().contentResolver.openFileDescriptor(selectedImageUri0!!, "r", null) ?: return@setOnClickListener


            doAddPatch(
                nom,description,date
            )
        }



        return rootView
    }
    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    private fun doAddPatch(nom: String, description: String, date: String){
        if (validate()) {




            //val stream = contentResolver.openInputStream(selectedImageUri0!!)
            val stream = requireActivity().contentResolver.openInputStream(selectedImageUri0!!)
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

            val apiInterface = RetrofiteInstance.api(context)
            val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

            data["nom"] = nom.toRequestBody(MultipartBody.FORM)
            data["description"] = description.toRequestBody(MultipartBody.FORM)
            data["date"] = date.toRequestBody(MultipartBody.FORM)
           // if (image?.body != null) {

                println("++++++++++++++++++++++++++++++++++++" + image)
                apiInterface.addPatch(data, image!!).enqueue(object :
                    Callback<Patch> {
                    override fun onResponse(
                        call: Call<Patch>,
                        response: Response<Patch>
                    ) {
                        if (response.isSuccessful) {
                            Log.i("onResponse goooood", response.body().toString())
                            //showAlertDialog()
                            Toast.makeText(context, "Patch Ajoutee ", Toast.LENGTH_SHORT).show()
                            //val action =
                            //findNavController().navigate(action)
                            nameEditText.clearComposingText()
                            descriptionEditText.clearComposingText()
                            val action = AddPatchFragmentDirections.actionAddPatchFragmentToPatchFragment()
                            findNavController().navigate(action)


                        } else {
                            Log.i("OnResponse not good", response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<Patch>, t: Throwable) {
                        //progress_bar.progress = 0
                        //Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()
                        println("rrrrrrrrrrrrrrrrrr")
                        println(t)
                        println("rrrrrrrrrrrrrrrrrr")

                        Toast.makeText(context, "Chose an image!", Toast.LENGTH_SHORT).show()

                    }

                })
           // }
            //else{

            //}
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            selectedImageUri0 = data?.data

            imagePicker0?.setImageURI(selectedImageUri0)
            fab2.hide()

        }
        else{
            Toast.makeText(context!!, "image null ", Toast.LENGTH_SHORT).show()

        }
    }    private fun validate(): Boolean {
        nameEditText.error = null
        descriptionEditText.error = null
        date.error = null

        if (nameEditText.text!!.isEmpty()) {
            nameEditText.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        if (descriptionEditText.text!!.isEmpty()) {
            descriptionEditText.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        if (date.text!!.isEmpty()) {
            date.error = getString(R.string.mustNotBeEmpty)
            return false
        }

        return true
    }


}