package com.adisalagic.testfoodies.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adisalagic.testfoodies.network.ApiNotRealImpl
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.network.objects.Tags
import com.adisalagic.testfoodies.utils.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _productState = MutableStateFlow(ProductsData.EMPTY)
    val productState = _productState.asStateFlow()

    private val _uiState = MutableStateFlow(HomeUiState(isRefreshing = false, errorDialog = false))
    val uiState = _uiState.asStateFlow()

    fun getProducts(onError: (Throwable) -> Unit) {
        var products: Result<Products>
        viewModelScope.launch {
            withContext(Dispatchers.IO) { products = api.getProducts() }
            if (products.isFailure) {
                val error = products.exceptionOrNull()!!
                Log.e("Network", error.message ?: "")
                onError(error)
                _productState.update { it.copy(lastException = error) }
            }
            if (products.isSuccess) {
                _productState.update { it.copy(products = products.getOrDefault(Products())) }
            }
            checkHaveEverything()
        }
    }

    fun cancelSearch() {
        _productState.update { it.copy(searchProducts = null) }
    }

    fun searchByEverything(search: String) {
        val products = _productState.value.products
        val tags = _productState.value.tags
        var result = products.filter {
            it.name.contains(search, ignoreCase = true)
                    || it.priceCurrent.toString().contains(search, ignoreCase = true)
                    || it.measure.toString().contains(search, ignoreCase = true)
        }
        val tagsResult = tags.filter {
            it.name.contains(search, ignoreCase = true)
        }
        tagsResult.forEach { tagsItem ->
            val another = products.filter {
                it.tagIds.contains(tagsItem.id)
            }
            result = result.toMutableList().apply {
                addAll(another)
                distinct()
            }
        }
        _productState.update { it.copy(searchProducts = Products().apply { addAll(result) }) }
    }


    fun getTags(onError: () -> Unit) {
        var tags: Result<Tags>
        viewModelScope.launch {
            withContext(Dispatchers.IO) { tags = api.getTags() }
            if (tags.isFailure) {
                onError()
            }
            if (tags.isSuccess) {
                _productState.update { it.copy(tags = tags.getOrDefault(Tags())) }
            }
            checkHaveEverything()
        }
    }

    private fun checkHaveEverything() {
        val value = _productState.value
        if (!value.isProductsEmpty() && value.tags != Tags()) {
            updateRefreshState(false)
        }
    }

    fun getImage(path: String): String {
        return "${(api as ApiNotRealImpl).baseUrl}images/$path"
    }

    fun updateRefreshState(isRefreshing: Boolean) {
        _uiState.update { it.copy(isRefreshing = isRefreshing) }
    }

    fun updateErrorState(errorDialog: Boolean) {
        _uiState.update { it.copy(errorDialog = errorDialog) }
    }

    data class HomeUiState(val isRefreshing: Boolean, val errorDialog: Boolean)
    data class ProductsData(
        val products: Products,
        val searchProducts: Products? = null,
        val tags: Tags,
        val lastException: Throwable?,
    ) {
        companion object {
            val EMPTY = ProductsData(
                products = Products(),
                tags = Tags(),
                lastException = null
            )
        }

        fun isProductsEmpty(): Boolean {
            return this.products == Products()
        }
    }
}