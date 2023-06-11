package org.lazlab.caloriecounter.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.lazlab.caloriecounter.model.Meals
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://gist.githubusercontent.com/IrsalLaz/e45a9d95f8c517a4a43be2d06ab25257/raw/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("static-api.json")
    suspend fun getMeals(): List<Meals>
}

object MealsApi {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun getMealUrl(name: String): String {
        return "$BASE_URL$name.png"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }