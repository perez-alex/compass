package com.example.compass.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface CompassApi {

    @GET("/about/")
    suspend fun getAboutPage(): Response<ResponseBody>
}