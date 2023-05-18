package com.adisalagic.testfoodies.ui.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.ui.components.ErrorDialog
import com.adisalagic.testfoodies.ui.components.FoodCard
import com.adisalagic.testfoodies.ui.components.FoodiesTabNavigator
import com.adisalagic.testfoodies.ui.viewmodels.HomeViewModel
import com.adisalagic.testfoodies.utils.toTabsList
import com.adisalagic.testfoodies.utils.viewModelStore
import kotlinx.coroutines.flow.StateFlow

private lateinit var model: HomeViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home() {
    model = viewModel(viewModelStore)
    val state by model.productState.collectAsState()
    val uiState by model.uiState.collectAsState()
    val refreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = ::refresh
    )
    LaunchedEffect(state.products) {
        refresh()
    }
    if (uiState.errorDialog) {
        ErrorDialog(e = state.lastException!!) {
            model.updateErrorState(false)
            refresh()
        }
    }
    Box(modifier = Modifier.fillMaxSize().pullRefresh(refreshState)) {
        Column {
            FoodiesTabNavigator(tabInfos = state.categories.toTabsList({
                return@toTabsList it == uiState.activeId
            }) {
                model.setActiveTab(id = it)
            })
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.getFilters()) {
                        FoodCard(
                            productsItem = it,
                            height = 330.dp,
                            image = model.getImage(it.image),
                            onCardClick = {

                            },
                        ) {

                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = uiState.isRefreshing,
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter).offset(y = (-100).dp)
                )
            }
        }
    }
}

private fun refresh() {
    model.updateRefreshState(true)
    model.getProducts {
        model.updateRefreshState(false)
        model.updateErrorState(true)
    }
    model.getTags {
        model.updateRefreshState(false)
        model.updateErrorState(true)
    }
    model.getCategories {
        model.updateRefreshState(false)
        model.updateErrorState(true)
    }
}

private fun HomeViewModel.ProductsData.getFilters(): Products {
    return this.searchProducts ?: this.products
}