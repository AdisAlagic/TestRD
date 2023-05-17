package com.adisalagic.testfoodies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adisalagic.testfoodies.R
import com.adisalagic.testfoodies.ui.theme.StandardRoundedCorner
import com.adisalagic.testfoodies.utils.colors
import com.adisalagic.testfoodies.utils.initUtils
import okio.IOException

@Composable
fun ErrorDialog(e: Throwable, onDismissListener: () -> Unit) {
    Dialog(onDismissRequest = onDismissListener) {
        Box(modifier = Modifier
            .heightIn(max = 150.dp)
            .clip(StandardRoundedCorner)) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0x7FFFFFFF))
                .blur(10.dp))
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RText(
                    text = stringResource(id = R.string.error_occurred),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                RText(text = getReason(e), fontSize = 18.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 50.dp),
                    onClick = onDismissListener
                ) {
                    RText(text = stringResource(id = R.string.k), color = Color.White)
                }
            }

        }
    }
}

@Preview
@Composable
fun ErrorDialogPrev() {
    initUtils()
    ErrorDialog(e = Exception("Smth")) {

    }
}

@Composable
private fun getReason(e: Throwable): String {
    return when (e) {
        is IOException -> stringResource(id = R.string.ioe)
        is IllegalArgumentException -> stringResource(id = R.string.iae)
        else -> stringResource(id = R.string.ue)
    }
}