package com.adisalagic.testfoodies.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adisalagic.testfoodies.network.ApiNotRealImpl
import com.adisalagic.testfoodies.network.objects.Categories
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.network.objects.Tags
import com.adisalagic.testfoodies.utils.api
import com.adisalagic.testfoodies.utils.asProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var lastSearch = ""

    private val _productState = MutableStateFlow(ProductsData.EMPTY)
    val productState = _productState.asStateFlow()

    private val _uiState =
        MutableStateFlow(HomeUiState(isRefreshing = false, errorDialog = false, activeId = -1))
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
        lastSearch = search
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
        val activeId = _uiState.value.activeId
        if (activeId != -1) {
            result = result.filter {
                it.categoryId == activeId
            }
        }
        _productState.update { it.copy(searchProducts = result.asProducts()) }
    }

    fun getCategories(onError: () -> Unit) {
        var categories: Result<Categories>
        viewModelScope.launch {
            withContext(Dispatchers.IO) { categories = api.getCategories() }
            if (categories.isFailure) {
                onError()
            }
            if (categories.isSuccess) {
                _productState.update { it.copy(categories = categories.getOrDefault(Categories())) }
            }
            checkHaveEverything()
        }
    }

    fun setActiveTab(id: Int) {
        if (id == _uiState.value.activeId) {
            _uiState.update { it.copy(activeId = -1) }
            _productState.update { it.copy(searchProducts = null) }
            searchByEverything(lastSearch)
            return
        }
        _uiState.update { it.copy(activeId = id) }
        val result: List<Products.ProductsItem> = if (_productState.value.searchProducts != null) {
            _productState.value.searchProducts!!.filter {
                it.categoryId == id
            }
        } else {
            _productState.value.products.filter {
                it.categoryId == id
            }
        }
        _productState.update { it.copy(searchProducts = result.asProducts()) }
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
        if (!value.isProductsEmpty() && value.tags != Tags() && value.categories != Categories()) {
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

    data class HomeUiState(val isRefreshing: Boolean, val errorDialog: Boolean, val activeId: Int)
    data class ProductsData(
        val products: Products,
        val searchProducts: Products? = null,
        val tags: Tags,
        val lastException: Throwable?,
        val categories: Categories,
    ) {
        companion object {
            val EMPTY = ProductsData(
                products = Products(),
                tags = Tags(),
                lastException = null,
                categories = Categories()
            )
        }

        fun isProductsEmpty(): Boolean {
            return this.products == Products()
        }
    }
}