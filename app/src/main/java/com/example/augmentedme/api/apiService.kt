package com.example.augmentedme.api

import com.example.augmentedme.Model.Passage
import com.example.augmentedme.Model.Patch
import com.example.augmentedme.Model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface apiService {


        @POST("user/login")
        fun seConnecter(@Body user: User): Call<User>

        @Multipart
        @POST("user/")
        fun register( @PartMap data : LinkedHashMap<String, RequestBody>,
                      @Part image: MultipartBody.Part): Call<User>



        @Multipart
        @POST("patch/")
        fun addPatch(@PartMap data : LinkedHashMap<String, RequestBody>,
                     @Part image: MultipartBody.Part): Call<Patch>


        @GET("patch/")
        fun getPatch():Call<List<Patch>>

        @GET("patch/{id}")
        fun getPatchByid(@Path("id") id : String):Call<Patch>

        @PUT("passage/{id}")
        fun doPassage(@Path("id") id : String,@Body passage: Passage):Call<Passage>

        @PUT("patch/{id}")
        fun doUpPatch(@Path("id") id : String):Call<Patch>

        @GET("user/{id}")
        fun getProfile(@Path("id") id : String):Call<User>

        @PUT("user/profile")
        fun updateProfile(@Body user :User):Call<User>






}