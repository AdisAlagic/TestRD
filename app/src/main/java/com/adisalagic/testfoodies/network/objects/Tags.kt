package com.adisalagic.testfoodies.network.objects


import com.google.gson.annotations.SerializedName

class Tags : ArrayList<Tags.TagsItem>(){
    data class TagsItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}