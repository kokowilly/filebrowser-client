package id.kokowilly.filebrowser.lib.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitFactory {
    private lateinit var retrofit: Retrofit

    fun initialize(
        baseUrl: String
    ) {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    fun <T> build(service: Class<T>): T {
        return retrofit.create(service)
    }

}
