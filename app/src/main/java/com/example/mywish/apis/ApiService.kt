package com.example.mywish.apis

import com.example.mywish.models.RequestAddWish
import com.example.mywish.models.RequestDeleteWish
import com.example.mywish.models.RequestRegisterOrLogin
import com.example.mywish.models.RequestUpdateWish
import com.example.mywish.models.ResponseMessage
import com.example.mywish.models.UserResponse
import com.example.mywish.models.Wish
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST

class Constants {
    companion object {
        private const val BASE_URL = "https://bestwishesserver-production.up.railway.app/api/"
        private const val BASE_URL_BACKUP = "http://bestwishes-ct274.vercel.app/api/"
        fun getInstance(): ApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiService::class.java)
        }
    }
}

interface ApiService {
    @POST("users/register")
    suspend fun registerUser(
        @Body request: RequestRegisterOrLogin
    ): Response<UserResponse>

    @POST("users/login")
    suspend fun loginUser(
        @Body request: RequestRegisterOrLogin
    ): Response<UserResponse>

    @GET("wishes")
    suspend fun getWishList() : Response<List<Wish>>

    @POST("wishes")
    suspend fun addWish (
        @Body addWish: RequestAddWish
    ) : Response<ResponseMessage>

    @PATCH("wishes")
    suspend fun updateWish (
        @Body addWish: RequestUpdateWish
    ) : Response<ResponseMessage>

    @HTTP(method = "DELETE", path = "wishes", hasBody = true)
    suspend fun deleteWish (
        @Body deleteWish: RequestDeleteWish
    ) : Response<ResponseMessage>
}