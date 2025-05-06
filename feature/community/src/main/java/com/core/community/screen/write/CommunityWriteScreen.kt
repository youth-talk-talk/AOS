package com.core.community.screen.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.core.navigation.CommunityWriteNavigation
import com.youthtalk.component.picture.PictureScreen
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.CommunityType

@Composable
fun CommunityWriteScreen(modifier: Modifier = Modifier, communityType: CommunityType) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CommunityWriteNavigation.Write,
    ) {
        composable<CommunityWriteNavigation.Write> {
            WriteScreen(
                communityType = communityType,
                onClickPolicySearch = { navController.navigate(CommunityWriteNavigation.PolicySearch) },
                onClickPicture = { navController.navigate(CommunityWriteNavigation.Picture) },
            )
        }

        composable<CommunityWriteNavigation.PolicySearch> {
            PolicySearchScreen()
        }

        composable<CommunityWriteNavigation.Picture> {
            PictureScreen()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

@Composable
fun Modifier.onClickNoIndicator(click: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember {
            MutableInteractionSource()
        },
        indication = null,
        onClick = click,
    )
}

fun Modifier.onEmptyHeight(isEmpty: Boolean): Modifier = composed {
    if (isEmpty) {
        height(100.dp)
    } else {
        this
    }
}

@Preview
@Composable
private fun CommunityWriteFreeScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            communityType = CommunityType.FREE,
        )
    }
}

@Preview
@Composable
private fun CommunityWriteReviewScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            communityType = CommunityType.REVIEW,
        )
    }
}
