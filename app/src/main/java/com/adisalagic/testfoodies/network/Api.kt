package com.adisalagic.testfoodies.network

import com.adisalagic.testfoodies.network.objects.Categories
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.network.objects.Tags

interface Api {
    fun getCategories(): Result<Categories>
    fun getTags(): Result<Tags>
    fun getProducts(): Result<Products>
    fun getImage(imageName: String): Result<ByteArray>
}