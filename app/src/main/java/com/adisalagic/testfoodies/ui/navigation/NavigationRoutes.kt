package com.adisalagic.testfoodies.ui.navigation

const val PREFIX = "http://foodies.com"

sealed class NavigationRoute(val route: String, val title: String) {
    object Home : NavigationRoute(route = "$PREFIX/", "Home")
    object Search : NavigationRoute(route = "$PREFIX/search", "Search")
    object Bucket : NavigationRoute(route = "$PREFIX/bucket", "Bucket")
}