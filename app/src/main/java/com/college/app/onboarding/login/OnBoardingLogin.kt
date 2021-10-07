package com.college.app.onboarding.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.college.app.R
import com.college.app.network.login.GoogleApiContract
import com.college.app.theme.getAppColorScheme
import com.google.android.gms.common.api.ApiException

@Composable
fun OnBoardingLogin() {

    val signInRequestCode = 1

    val context = LocalContext.current
    val colorScheme = getAppColorScheme(isSystemInDarkTheme())

    val userRawRes = if (isSystemInDarkTheme()) {
        R.raw.login_light_user_lottie
    } else {
        R.raw.login_dark_user_lottie
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(userRawRes))

    val viewModel = hiltViewModel<OnBoardingLoginViewModel>()

    val loginViewState = viewModel.loginViewState.observeAsState()
    val command = viewModel.command.observeAsState()

    when (command.value) {
        OnBoardingLoginViewModel.Command.StartGoogleLogin -> {
            val launcher = rememberLauncherForActivityResult(contract = GoogleApiContract()) {
                try {
                    val googleUser = it?.getResult(ApiException::class.java)
                    if (googleUser != null) {
                        viewModel.loginSuccess(googleUser)
                    } else {
                        // todo show error
                        viewModel.loginFail(it?.exception?.message.orEmpty())
                    }

                } catch (e: ApiException) {
                    viewModel.loginFail(e.toString())
                }
            }
            SideEffect {
                launcher.launch(signInRequestCode)
            }
        }
    }

    Scaffold(backgroundColor = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = context.getString(R.string.login_hello),
                    fontSize = 30.sp,
                    color = colorScheme.primaryTextColor
                )
                Text(
                    text = context.getString(R.string.login_footer),
                    fontSize = 18.sp,
                    color = colorScheme.primaryTextColor
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(onClick = {
                viewModel.userClickedForLogin()
            }) {
                Text(text = context.getString(R.string.login))
            }
        }
    }
}

@Composable
@Preview
fun PreviewOnBoardingLogin() {
    OnBoardingLogin()
}