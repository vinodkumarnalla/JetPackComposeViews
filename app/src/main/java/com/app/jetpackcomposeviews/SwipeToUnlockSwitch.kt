package com.app.jetpackcomposeviews
import androidx.compose.animation.core.animateFloatAsState
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
import kotlin.math.roundToInt

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

    var thumbOffsetX by remember { mutableFloatStateOf(0f) }
    val unlocked = remember { mutableStateOf(false) }

    val maxOffset = remember(containerWidth, containerHeight) {
        (containerWidth - containerHeight).toFloat()
    }

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (unlocked.value) maxOffset else thumbOffsetX,
        animationSpec = tween(300),
        label = "thumbOffset"
    )

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
                            thumbOffsetX = (thumbOffsetX + dragAmount).coerceIn(0f, maxOffset)
                        }
                    },
                    onDragEnd = {
                        if (thumbOffsetX >= maxOffset * 0.9f) {
                            unlocked.value = true
                            onUnlock()
                        } else {
                            thumbOffsetX = 0f
                        }
                    }
                )
            }
    ) {
        // Text when locked
        if (!unlocked.value) {
            Text(
                text = text,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Thumb (lock icon)
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
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
