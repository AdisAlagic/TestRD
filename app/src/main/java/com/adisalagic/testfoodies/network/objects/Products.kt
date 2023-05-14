package com.adisalagic.testfoodies.network.objects


import com.google.gson.annotations.SerializedName

class Products : ArrayList<Products.ProductsItem>(){
    data class ProductsItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("category_id")
        val categoryId: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("price_current")
        val priceCurrent: Int,
        @SerializedName("price_old")
        val priceOld: Int?,
        @SerializedName("measure")
        val measure: Int,
        @SerializedName("measure_unit")
        val measureUnit: String,
        @SerializedName("energy_per_100_grams")
        val energyPer100Grams: Double,
        @SerializedName("proteins_per_100_grams")
        val proteinsPer100Grams: Double,
        @SerializedName("fats_per_100_grams")
        val fatsPer100Grams: Double,
        @SerializedName("carbohydrates_per_100_grams")
        val carbohydratesPer100Grams: Double,
        @SerializedName("tag_ids")
        val tagIds: List<Int>
    )
}