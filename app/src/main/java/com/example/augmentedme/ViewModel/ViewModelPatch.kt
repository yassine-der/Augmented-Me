package com.example.augmentedme.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.augmentedme.Model.Patch
import com.example.augmentedme.api.RetrofiteInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelPatch : ViewModel() {
    lateinit var lifeDataList: MutableLiveData<List<Patch>>
    init {
        lifeDataList = MutableLiveData()

    }
    fun getLiveDataObserver(): MutableLiveData<List<Patch>> {
        return lifeDataList
    }
    fun makeApiCall2(context: Context?){
        val apiInterface = RetrofiteInstance.api(context)
        apiInterface.getPatch().enqueue(object : Callback<List<Patch>> {
            override fun onFailure(call: Call<List<Patch>>, t: Throwable) {
                lifeDataList.postValue(null)
            }

            override fun onResponse(call: Call<List<Patch>>, response: Response<List<Patch>>) {
                lifeDataList.postValue(response.body())
            }


        })

    }


}