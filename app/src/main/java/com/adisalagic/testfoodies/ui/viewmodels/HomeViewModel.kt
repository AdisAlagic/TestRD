package com.adisalagic.testfoodies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.network.objects.Tags
import com.adisalagic.testfoodies.utils.api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _productState = MutableStateFlow(ProductsData.EMPTY)
    val productState = _productState.asStateFlow()

    fun getProducts(onError: () -> Unit) {
        var products: Result<Products>
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) { products = api.getProducts() }
            if (products.isFailure) {
                onError()
            }
            if (products.isSuccess) {
                _productState.update { it.copy(products = products.getOrDefault(Products())) }
            }
        }
    }

    fun getTags(onError: () -> Unit) {
        var tags: Result<Tags>
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) { tags = api.getTags() }
            if (tags.isFailure) {
                onError()
            }
            if (tags.isSuccess) {
                _productState.update { it.copy(tags = tags.getOrDefault(Tags())) }
            }
        }
    }


    data class ProductsData(val products: Products, val tags: Tags) {
        companion object {
            val EMPTY = ProductsData(
                products = Products(),
                tags = Tags()
            )
        }

        fun isProductsEmpty(): Boolean {
            return this.products == Products()
        }
    }
}