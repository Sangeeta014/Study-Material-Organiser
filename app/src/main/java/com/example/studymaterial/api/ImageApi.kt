package com.example.studymaterial.api

import com.example.studymaterial.models.ImageItem
import retrofit2.Response
import retrofit2.http.GET

interface ImageApi {

    @GET("v2/list")
    suspend fun getImageList(): Response<List<ImageItem>>
}
