package com.adisalagic.testfoodies.network

import android.content.Context
import com.adisalagic.testfoodies.R
import com.adisalagic.testfoodies.network.objects.Categories
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.network.objects.Tags
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

class ApiNotRealImpl(val context: Context) : Api {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient()
    private val url = "http://foodies.com/api"
    private val gson = Gson()

    init {
        mockWebServer.url(url)
        val dis = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val response = when (request.path) {
                    "/categories" -> getRawJson(R.raw.Categories)
                    "/tags" -> getRawJson(R.raw.Tags)
                    "/products" -> getRawJson(R.raw.Products)
                    else -> {
                        return MockResponse().setResponseCode(404)
                    }
                }
                return MockResponse().apply {
                    this.setBody(response)
                    this.http2ErrorCode = 200
                }
            }
        }
        mockWebServer.dispatcher = dis
        mockWebServer.start(8000)
    }


    private fun getRawJson(id: Int): String {
        return String(context.resources.openRawResource(id).readBytes())
    }

    override fun getCategories(): Result<Categories> {
        val request = Request.Builder()
            .get()
            .url("$url/categories")
            .build()
        val call = client.newCall(request)
        try {
            call.execute().use {
                val body = it.body?.string()
                if (body != null) {
                    return Result.success(gson.fromJson(body, Categories::class.java))
                }
                return Result.failure(Exception("Empty body"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun getTags(): Result<Tags> {
        val request = Request.Builder()
            .get()
            .url("$url/tags")
            .build()
        val call = client.newCall(request)
        try {
            call.execute().use {
                val body = it.body?.string()
                if (body != null) {
                    return Result.success(gson.fromJson(body, Tags::class.java))
                }
                return Result.failure(Exception("Empty body"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun getProducts(): Result<Products> {
        val request = Request.Builder()
            .get()
            .url("$url/products")
            .build()
        val call = client.newCall(request)
        try {
            call.execute().use {
                val body = it.body?.string()
                if (body != null) {
                    return Result.success(gson.fromJson(body, Products::class.java))
                }
                return Result.failure(Exception("Empty body"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}