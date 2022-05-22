package com.amrmedhatandroid.tahliluk_laboratory.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient {
    companion object {
        private var mRetrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/fcm/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
    }
}