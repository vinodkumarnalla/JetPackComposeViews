package com.app.jetpackcomposeviews
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A composable Swipe-to-Unlock switch that allows the user to drag a thumb horizontally to unlock an action.
 *
 * This is similar to a "slide to unlock" pattern seen in mobile UIs.
 *
 * @param modifier Modifier to be applied to the SwipeToUnlockSwitch container.
 * @param backgroundColor The background color of the switch.
 * @param thumbColor The color of the draggable thumb.
 * @param text The label displayed in the center when locked.
 * @param textColor The color of the label text.
 * @param icon The icon shown on the thumb when locked.
 * @param unlockedIcon The icon shown on the thumb once unlocked.
 * @param cornerRadius Corner radius of the switch container.
 * @param onUnlock Callback triggered when the thumb is swiped beyond the unlock threshold.
 */
@Composable
fun SwipeToUnlockSwitch(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    thumbColor: Color = Color.White,
    text: String = "Swipe to Unlock",
    textColor: Color = Color.DarkGray,
    icon: ImageVector = Icons.Default.Lock,
    unlockedIcon: ImageVector = Icons.Default.List,
    cornerRadius: Int = 32,
    onUnlock: () -> Unit
) {
    var containerWidth by remember { mutableIntStateOf(0) }
    var containerHeight by remember { mutableIntStateOf(0) }

    val unlocked = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val maxOffset: Float = (containerWidth - containerHeight).toFloat()

    val thumbOffsetX = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .onSizeChanged {
                containerWidth = it.width
                containerHeight = it.height
            }
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        if (!unlocked.value && containerHeight > 0) {
                            val newOffset = (thumbOffsetX.value + dragAmount).coerceIn(0f, maxOffset)
                            coroutineScope.launch {
                                thumbOffsetX.snapTo(newOffset)
                            }
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            if (thumbOffsetX.value >= maxOffset * 0.9f) {
                                unlocked.value = true
                                thumbOffsetX.animateTo(maxOffset, tween(300))
                                onUnlock()
                            } else {
                                thumbOffsetX.animateTo(0f, tween(300))
                            }
                        }
                    }
                )
            }
    ) {
        if (!unlocked.value) {
            Text(
                text = text,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(thumbOffsetX.value.roundToInt(), 0) }
                .size(with(LocalDensity.current) { containerHeight.toDp() })
                .clip(CircleShape)
                .background(thumbColor)
                .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                .padding(12.dp)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (unlocked.value) unlockedIcon else icon,
                contentDescription = "Swipe Thumb Icon",
                tint = Color.Black
            )
        }
    }
}



@Preview
@Composable
private fun PreviewSwipeToUnlock(){
    SwipeToUnlockSwitch(
        onUnlock = {  }
    )
}
