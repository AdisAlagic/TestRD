package com.adisalagic.testfoodies.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adisalagic.testfoodies.R
import com.adisalagic.testfoodies.network.objects.Products
import com.adisalagic.testfoodies.ui.theme.ColorBackgroundCard
import com.adisalagic.testfoodies.ui.theme.ColorOldPrice
import com.adisalagic.testfoodies.ui.theme.StandardRoundedCorner
import com.adisalagic.testfoodies.utils.colors
import com.adisalagic.testfoodies.utils.toBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCard(
    productsItem: Products.ProductsItem,
    image: String,
    onCardClick: (Int) -> Unit,
    height: Dp = Dp.Unspecified,
    onPriceClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 370.dp),
        shape = StandardRoundedCorner,
        onClick = { onCardClick(productsItem.id) },
        colors = CardDefaults.cardColors(
            containerColor = ColorBackgroundCard,
            contentColor = colors.onSurface
        )
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Column {
                Box {
                    LazyRow( //Tags
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        userScrollEnabled = false
                    ) {
                        items(productsItem.tagIds) {
                            Tag(id = it)
                        }
                    }
                    AsyncImage(
                        model = image,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = stringResource(
                            id = R.string.preview
                        )
                    )
                }
                RText(text = productsItem.name, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(5.dp))
                RText(text = "${productsItem.measure} ${productsItem.measureUnit}")
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CenteredButton(
                        price = productsItem.priceCurrent,
                        oldPrice = productsItem.priceOld,
                        onPriceClicked = onPriceClicked
                    )
                }
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

@Composable
private fun CenteredButton(price: Int, oldPrice: Int? = null, onPriceClicked: (Int) -> Unit) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onPriceClicked(price) },
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.background,
        )
    ) {
        RText(text = "$price ₽", fontWeight = FontWeight.Bold)
        if (oldPrice == null) {
            return@TextButton
        }
        Spacer(modifier = Modifier.width(10.dp))
        RText(
            text = "$oldPrice ₽",
            color = ColorOldPrice,
            textDecoration = TextDecoration.LineThrough
        )
    }
}