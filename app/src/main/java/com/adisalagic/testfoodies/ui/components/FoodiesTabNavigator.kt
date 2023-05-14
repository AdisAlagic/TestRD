package com.adisalagic.testfoodies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adisalagic.testfoodies.ui.theme.ColorPrimary
import com.adisalagic.testfoodies.ui.theme.StandardRoundedCorner
import com.adisalagic.testfoodies.ui.theme.getColors
import com.adisalagic.testfoodies.utils.BoxCentered

@Composable
fun FoodiesTabNavigator(tabInfos: List<TabInfo>) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(tabInfos) {
            TabBox(
                title = it.title,
                isActive = it.isActive,
                categoryId = it.categoryId,
                onTabClicked = it.onTabClicked
            )
        }
    }
}

@Preview
@Composable
fun TabBoxPrev() {
    TabBox(title = "Роллы", isActive = true, categoryId = 0, onTabClicked = {})
}

@Composable
private fun TabBox(
    title: String,
    categoryId: Int,
    isActive: Boolean,
    onTabClicked: (Int) -> Unit,
) {
    val colors = getColors()
    BoxCentered(
        modifier = Modifier
            .clip(StandardRoundedCorner)
            .background(if (isActive) ColorPrimary else colors.background)
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .clickable {
                onTabClicked(categoryId)
            }
    ) {
        RText(
            text = title,
            fontSize = 18.sp,
            color = if (isActive) Color.White else colors.onSurface
        )
    }
}

data class TabInfo(
    val title: String,
    val isActive: Boolean,
    val categoryId: Int,
    val onTabClicked: (Int) -> Unit,
)