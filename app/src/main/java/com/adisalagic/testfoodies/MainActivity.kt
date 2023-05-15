package com.adisalagic.testfoodies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.adisalagic.testfoodies.ui.components.AppBarTitle
import com.adisalagic.testfoodies.ui.navigation.NavigationGraph
import com.adisalagic.testfoodies.ui.theme.TestFoodiesTheme
import com.adisalagic.testfoodies.utils.initUtils

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            initUtils()
            TestFoodiesTheme {
                Scaffold(
                    topBar = {
                        AppBarTitle(
                            onFiltersClick = { /*TODO*/ },
                            onSearch = { })
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