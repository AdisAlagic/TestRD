package com.adisalagic.testfoodies.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adisalagic.testfoodies.R
import com.adisalagic.testfoodies.ui.theme.ColorPlaceholderText
import com.adisalagic.testfoodies.ui.theme.ColorPrimary
import com.adisalagic.testfoodies.ui.theme.RobotoFont
import com.adisalagic.testfoodies.utils.rememberEmptyString
import com.adisalagic.testfoodies.utils.rememberFalse

@Preview
@Composable
fun PreviewAppBarTitle() {
    AppBarTitle({}) {

    }
}

@Composable
fun AppBarTitle(
    onFiltersClick: () -> Unit,
    onSearch: (String) -> Unit,
) {
    var expended by rememberFalse()
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        AnimatedVisibility(visible = expended, enter = slideInHorizontally {
            it * 2
        }, exit = slideOutHorizontally { it * 2 }) {
            SearchAppBar(
                onBackClick = { expended = false },
                onSearch = onSearch
            )
        }
        AnimatedVisibility(visible = !expended, enter = fadeIn(), exit = fadeOut()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconFilters(onFiltersClick)
                Image(
                    painter = painterResource(id = R.drawable.foodies),
                    contentDescription = stringResource(
                        id = R.string.foodies_logo
                    )
                )
                IconSearchImage(false) {
                    expended = true
                }
            }
        }
    }
}


@Composable
private fun IconFilters(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.iconfilters),
            contentDescription = stringResource(
                id = R.string.filters_str
            )
        )
    }
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppBar({}, {})
}

@Composable
private fun SearchAppBar(onBackClick: () -> Unit, onSearch: (String) -> Unit) {
    var searchPhrase by rememberEmptyString()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fontSize = 18.sp
        IconSearchImage(true) {
            onBackClick()
        }
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchPhrase,
            textStyle = TextStyle(
                fontFamily = RobotoFont,
                fontSize = fontSize
            ),
            onValueChange = {
                searchPhrase = it
                onSearch(it)
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (searchPhrase.isBlank()) {
                        RText(
                            text = stringResource(id = R.string.placeholder_search),
                            fontSize = fontSize,
                            color = ColorPlaceholderText
                        )
                    }
                    it()
                }
                AnimatedVisibility(
                    visible = searchPhrase.isNotBlank(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { searchPhrase = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.iconerase),
                            contentDescription = stringResource(
                                id = R.string.erase_hint
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconSearchImage(isBack: Boolean = false, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(
                id = if (isBack) R.drawable.iconback else R.drawable.iconsearch
            ),
            tint = if (isBack) ColorPrimary else LocalContentColor.current,
            contentDescription = stringResource(
                id = R.string.search_icon
            )
        )
    }
}