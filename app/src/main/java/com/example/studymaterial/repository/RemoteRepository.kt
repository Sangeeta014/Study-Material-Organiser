package com.example.studymaterial.repository

import android.util.Log
import com.example.studymaterial.api.ImageApi
import com.example.studymaterial.api.ImageInstance
import com.example.studymaterial.api.NetworkResponse
import com.example.studymaterial.models.ImageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RemoteRepository(){
    private val imageApi = ImageInstance.imageApi



    // Fetch images from the API
    fun getImages(): Flow<NetworkResponse<List<ImageItem>>> = flow {
        Log.d("imageAPI", "$imageApi")
        try {
            emit(NetworkResponse.Loading) // Emit loading state
            val response: Response<List<ImageItem>> = imageApi.getImageList()
            if (response.isSuccessful) {
                response.body()?.let { images ->
                    emit(NetworkResponse.Success(images))
                } ?: emit(NetworkResponse.Error("No data found"))
            } else {
                emit(NetworkResponse.Error("Failed to load data"))
            }
        } catch (e: Exception) {
            emit(NetworkResponse.Error("Failed to load the data: ${e.message}")) // Error
            Log.e("RemoteRepository", "Error fetching images: ${e.message}", e)
        }
    }
}
