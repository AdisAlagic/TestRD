package com.adisalagic.testfoodies.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adisalagic.testfoodies.R
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.ui.theme.StandardRoundedCorner
import com.adisalagic.testfoodies.utils.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCard(productsItem: Products.ProductsItem, image: ByteArray, onCardClick: (Int) -> Unit) {
    Card(
        shape = StandardRoundedCorner,
        onClick = { onCardClick(productsItem.id) },
        colors = CardDefaults.cardColors(
            containerColor = colors.background,
            contentColor = colors.onSurface
        )
    ) {
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            Row() { //Tags

            }
        }
    }
}

@Composable
private fun Tag(id: Int) {
    val contentId = when (id) {
        2 -> R.drawable.type_vegan
        3 -> R.drawable.type_discount
        4 -> R.drawable.type_hot
        else -> null
    } ?: return
    Box {
        Image(
            painter = painterResource(id = contentId),
            contentDescription = ""
        )
    }
}