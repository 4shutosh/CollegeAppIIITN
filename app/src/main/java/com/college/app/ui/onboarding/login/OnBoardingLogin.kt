package com.college.app.ui.onboarding.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        R.raw.login_dark_user_lottie
    } else {
        R.raw.login_light_user_lottie
    }

    val loadingRawRes = if (isSystemInDarkTheme()) {
        R.raw.lottie_loader_dark
    } else {
        R.raw.lottie_loader_light
    }

    val userComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(userRawRes))
    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(loadingRawRes))

    val viewModel = hiltViewModel<OnBoardingLoginViewModel>()

    val loginViewState = viewModel.loginViewState.observeAsState()
    val command = viewModel.command.observeAsState()

    when (command.value) {
        is OnBoardingLoginViewModel.Command.StartGoogleLogin -> {
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

//    if (loginViewState.value?.isLoading == true) {
//        Surface(
//            Modifier.fillMaxSize(1f),
//            color = MaterialTheme.colors.surface.copy(alpha = 0.6f)
//        ) {
//            LottieAnimation(
//                composition = loadingComposition,
//                iterations = LottieConstants.IterateForever,
//                modifier = Modifier
//                    .width(200.dp)
//                    .height(200.dp)
//            )
//        }
//    }else{
//
//    }

    Scaffold(backgroundColor = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            LottieAnimation(
                composition = userComposition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .width(350.dp)
                    .height(350.dp)
            )
            Text(
                text = context.getString(R.string.login_hello),
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primaryTextColor,
            )
            Text(
                text = context.getString(R.string.login_footer),
                fontSize = 35.sp,
                fontWeight = FontWeight.Normal,
                color = colorScheme.primaryTextColor
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            GoogleSignInButton(onClicked = {
                viewModel.userClickedForLogin()
            })
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = context.getString(R.string.login_footer_message),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                color = colorScheme.primaryTextColor
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    text: String = "Sign in with Google",
    textColor: Color = MaterialTheme.colors.surface,
    icon: Int = R.drawable.ic_google_logo,
    backgroundColor: Color = MaterialTheme.colors.onSurface,
) {
    var clicked by remember { mutableStateOf(false) }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(25),
        modifier = modifier
            .clickable { clicked = !clicked }
            .fillMaxWidth(0.7f)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Google Button",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (clicked) {
                onClicked()
                clicked = false
            }
        }
    }
}

@Composable
@Preview
private fun GoogleButtonPreview() {
    GoogleSignInButton(
        onClicked = {}
    )
}

@Composable
@Preview
fun PreviewOnBoardingLogin() {
    OnBoardingLogin()
}