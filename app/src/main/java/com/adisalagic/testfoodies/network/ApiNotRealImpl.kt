package com.adisalagic.testfoodies.network

import android.content.Context
import android.graphics.Bitmap
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
import java.util.concurrent.Executors

class ApiNotRealImpl(val context: Context) : Api {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient()
    private var url = "http://foodies.com/api"
    private val gson = Gson()

    init {
        url = mockWebServer.url("/").redact()
        val dis = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val response = when (request.path) {
                    "/categories" -> getRawJson(R.raw.categories)
                    "/tags" -> getRawJson(R.raw.tags)
                    "/products" -> getRawJson(R.raw.products)
                    "/images/1.png" -> getRawJson(R.raw.dish)
                    else -> {
                        return MockResponse().setResponseCode(404)
                    }
                }
                return MockResponse().apply {
                    this.setBody(response.toString())
                    this.http2ErrorCode = 200
                }
            }
        }
        mockWebServer.dispatcher = dis
        Executors.newSingleThreadExecutor().execute {
            mockWebServer.start(8000)
        }
    }

    /**
     * In real-life situation we would use `Picasso` or `Glide`.
     * There are easy to use and caches images. Also async.
     * This is the simple implementation of getting file from api.
     */
    private fun getImage(imageName: String): Result<ByteArray> {
        val request = Request.Builder()
            .get()
            .url("$url/images/$imageName")
            .build()
        val call = client.newCall(request)
        try {
            call.execute().use {
                val body = it.body?.bytes()
                if (body != null) {
                    return Result.success(body)
                }
                return Result.failure(IllegalArgumentException("Empty body"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private fun getRawJson(id: Int): String {
        var result: String
        context.resources.openRawResource(id).use {
            result = String(it.readBytes())
        }
        return result
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
                return Result.failure(IllegalArgumentException("Empty body"))
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
                return Result.failure(IllegalArgumentException("Empty body"))
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
                return Result.failure(IllegalArgumentException("Empty body"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


}