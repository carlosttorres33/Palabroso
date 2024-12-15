package com.carlostorres.wordsgame.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavBackStackEntry

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick(enabled: Boolean = true) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.70f else 1f)

    if (!enabled) {
        return@composed this
    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Press) {
                        buttonState = ButtonState.Pressed
                    } else if (event.type == PointerEventType.Release) {
                        buttonState = ButtonState.Idle
                    }
                }
            }
        }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.verticalSlideEnterTransition(): EnterTransition {

    return slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(durationMillis = 300))
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.verticalSlideExitTransition(): ExitTransition {

    return slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(durationMillis = 300))
}