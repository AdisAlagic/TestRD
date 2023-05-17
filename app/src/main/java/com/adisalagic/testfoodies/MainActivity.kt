package com.adisalagic.testfoodies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.adisalagic.testfoodies.ui.components.AppBarTitle
import com.adisalagic.testfoodies.ui.navigation.NavigationGraph
import com.adisalagic.testfoodies.ui.theme.TestFoodiesTheme
import com.adisalagic.testfoodies.ui.viewmodels.HomeViewModel
import com.adisalagic.testfoodies.utils.initUtils

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            initUtils()
            val model = viewModel<HomeViewModel>(com.adisalagic.testfoodies.utils.viewModelStore)
            TestFoodiesTheme {
                Scaffold(
                    topBar = {
                        AppBarTitle(
                            onFiltersClick = { /*TODO*/ },
                            onSearch = {
                                if (it.isBlank()) {
                                    model.cancelSearch()
                                }else {
                                    model.searchByEverything(it)
                                }
                            })
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        NavigationGraph(navHostController = navHostController)
                    }
                }
            }
        }
    }
}