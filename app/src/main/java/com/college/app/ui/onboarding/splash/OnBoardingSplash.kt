package com.college.app.ui.onboarding.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.college.app.R
import com.college.app.utils.extensions.getImageBitmapFromDrawable

@Composable
@Preview
fun OnBoardingSplash() {

    val context = LocalContext.current

    Surface(
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                getImageBitmapFromDrawable(
                    context,
                    R.mipmap.ic_launcher_round
                )?.let {
                    Image(
                        bitmap = it,
                        null,
                        Modifier.wrapContentHeight().wrapContentWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }

                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp
                )
            }
        }
    }
}