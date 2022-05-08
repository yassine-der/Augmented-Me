package com.example.augmentedme.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.augmentedme.Fragment.adapter.patchBaseAdapter
import com.example.augmentedme.Model.Patch
import com.example.augmentedme.R
import com.example.augmentedme.ViewModel.ViewModelPatch
import com.example.augmentedme.api.RetrofiteInstance
import com.example.augmentedme.unityActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchFragment : Fragment(), AdapterView.OnItemClickListener{
    lateinit var gridEquipe: GridView
    private var arrayList: List<Patch>? = null
    lateinit var equipeBaseAdapter : patchBaseAdapter
    lateinit var buttonGridAddEquipe : Button
    lateinit var id_patch : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.patches, container, false)
        gridEquipe  = rootView.findViewById(R.id.gridPatch)
        val viewModel: ViewModelPatch = ViewModelProvider(this).get(ViewModelPatch::class.java)
        viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if(it != null) {
                //adapter2.setLigueList(it)
                //adapter2.notifyDataSetChanged()
                arrayList = it
                equipeBaseAdapter = patchBaseAdapter(context!!,it)
                gridEquipe.adapter = equipeBaseAdapter
                gridEquipe.onItemClickListener = this@PatchFragment

            } else {
                Toast.makeText(context, "Error in getting list", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeApiCall2(context)

        return  rootView
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var patch : Patch = arrayList!!.get(position)
        Toast.makeText(context, "Error in getting list", Toast.LENGTH_SHORT).show()

        id_patch = patch._id!!
        val apiInterface = RetrofiteInstance.api(context)

        var patch1 = Patch()

        apiInterface.doUpPatch(
            id_patch
        ).enqueue(object :
            Callback<Patch> {

            override fun onResponse(call: Call<Patch>, response: Response<Patch>) {

                val passage = response.body()



                Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(context!!, unityActivity::class.java)
                startActivity(intent)


            }

            override fun onFailure(call: Call<Patch>, t: Throwable) {

                Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })


        val action = PatchFragmentDirections.actionPatchFragmentToDetailsPatchFragment(id_patch)
        findNavController().navigate(action)



    }

}