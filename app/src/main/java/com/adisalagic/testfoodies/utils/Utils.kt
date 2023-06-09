package com.adisalagic.testfoodies.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.adisalagic.testfoodies.network.Api
import com.adisalagic.testfoodies.network.ApiNotRealImpl
import com.adisalagic.testfoodies.network.objects.Categories
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.ui.components.TabInfo
import com.adisalagic.testfoodies.ui.theme.getColors

lateinit var localDensity: Density
lateinit var api: Api
lateinit var colors: ColorScheme
lateinit var viewModelStore: ViewModelStoreOwner
lateinit var applicationContext: Context

/**
 * Get composable-only vars for non-composable use
 */
@SuppressLint("ComposableNaming")
@Composable
fun initUtils() {
    localDensity = LocalDensity.current
    api = ApiNotRealImpl(LocalContext.current)
    colors = getColors()
    applicationContext = LocalContext.current.applicationContext
    viewModelStore = LocalViewModelStoreOwner.current!!
}

@Composable
fun rememberEmptyString(): MutableState<String> {
    return remember {
        mutableStateOf("")
    }
}

@Composable
fun rememberBoolean(boolean: Boolean): MutableState<Boolean> {
    return remember {
        mutableStateOf(boolean)
    }
}

@Composable
fun rememberFalse(): MutableState<Boolean> {
    return rememberBoolean(boolean = false)
}

@Composable
fun rememberTrue(): MutableState<Boolean> {
    return rememberBoolean(boolean = true)
}

/**
 * Standard box with
 *
 * ```contentAlignment = Alignment.Center```
 */
@Composable
fun BoxCentered(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(contentAlignment = Alignment.Center, content = content, modifier = modifier)
}


/**
 * Standard row with
 *
 * ```verticalAlignment = Alignment.CenterVertically```
 */
@Composable
fun RowCentered(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        content = content
    )
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

val Int.px: Dp
    get() {
        var result: Dp
        with(localDensity) {
            result = this@px.toDp()
        }
        return result
    }

fun Categories.toTabsList(isActive:(Int) -> Boolean, onTabClicked: (Int) -> Unit): List<TabInfo> {
    val list = mutableListOf<TabInfo>()
    this.forEach {
        list.add(
            TabInfo(
                it.name,
                isActive(it.id),
                categoryId = it.id,
                onTabClicked = onTabClicked
            )
        )
    }
    return list
}

fun List<Products.ProductsItem>.asProducts(): Products {
    return Products().apply { addAll(this@asProducts) }
}