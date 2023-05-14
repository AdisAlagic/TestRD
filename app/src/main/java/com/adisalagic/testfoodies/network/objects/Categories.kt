package com.adisalagic.testfoodies.network.objects


import com.google.gson.annotations.SerializedName

class Categories : ArrayList<Categories.CategoriesItem>(){
    data class CategoriesItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}