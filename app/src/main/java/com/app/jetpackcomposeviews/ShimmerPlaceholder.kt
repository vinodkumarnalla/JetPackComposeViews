package com.app.jetpackcomposeviews

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Displays a shimmer animation placeholder using a linear gradient brush.
 *
 * This composable is useful as a loading indicator, typically used in place of UI content
 * while waiting for data to load (e.g., similar to Facebook-style shimmer loading).
 *
 * @param modifier Modifier to be applied to the placeholder layout.
 * @param shape The shape of the placeholder, default is [RoundedCornerShape(8.dp)].
 *
 * ### Example usage:
 * ```
 * ShimmerPlaceholder(
 *     modifier = Modifier
 *         .size(100.dp, 16.dp)
 * )
 * ```
 */
@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    shimmerColors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )
) {

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateX, 0f),
        end = Offset(translateX + 200f, 0f)
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(shimmerBrush)
    )

}


@Preview
@Composable
private fun PreviewShimmerPlaceholder() {
    ShimmerPlaceholder()
}