package com.example.studymaterial.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ImageInstance{
    private val baseUrl = "https://picsum.photos/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //Companion Property
    companion object {
        private val retrofit = ImageInstance().getInstance()
        val imageApi: ImageApi = retrofit.create(ImageApi::class.java)
    }

}