package com.dicoding.storyapp.data.remote.retrofit

import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.response.StoryResponse

import retrofit2.http.*

interface ApiService {

    @GET("stories")
    suspend fun getStories(
    ): StoryResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

//    @GET("stories/{id}")
//    fun getStoryDetail(@Path("id") storyId: String): Call<StoryByIDResponse>


//    @Multipart
//    @POST("stories")
//    suspend fun uploadImage(
//        @Part file: MultipartBody.Part,
//        @Part ("description") description: RequestBody,
//    ) : FileUploadResponse

}