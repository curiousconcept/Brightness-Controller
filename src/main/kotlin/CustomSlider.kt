import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomSlider(
    initialSliderValue: Float=1F,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    trackHeight: Dp = 30.dp,
    thumbRadius: Dp = 30.dp,
    trackColor: Color = Color.Gray,
    thumbColor: Color = Color.Blue,
) {
    val density = LocalDensity.current
    val trackHeightPx = with(density) { trackHeight.toPx() }
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }

    val internalValue = remember { mutableStateOf(initialSliderValue) }
    val sliderWidth = remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbRadius * 3)
            .padding(horizontal = 16.dp)
            .onSizeChanged { size -> sliderWidth.value = size.width.toFloat() }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val trackPositionRatio = change.position.x / sliderWidth.value  // Get the ratio of the drag position or where we are relatively on the track
                    val boundarySafePosition = trackPositionRatio.coerceIn(0f, 1f)
                    val newValueWithAdjustedOffset = boundarySafePosition * (valueRange.endInclusive - valueRange.start) + valueRange.start

                    internalValue.value = newValueWithAdjustedOffset

                    onValueChange(newValueWithAdjustedOffset)
                }

            }
            .pointerInput(Unit){
                detectTapGestures { offset ->
                    val newValue = ((offset.x / sliderWidth.value).coerceIn(0f, 1f)) * (valueRange.endInclusive - valueRange.start) + valueRange.start
                    internalValue.value = newValue
                    onValueChange(newValue)
                }
            } .drawBehind { // This is where we draw the outline around the Box itself

                    val strokeWidth = 1.dp.toPx()
                    drawRect(
                        color = Color.Red, // Red color to make the boundary visible
                        size = Size(size.width, size.height),
                        topLeft = Offset(0f, 0f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                    )

            }

    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val sliderX = ((internalValue.value - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * canvasWidth

            val trackVerticalCenter = (canvasHeight - trackHeightPx) / 2 // Vertical center of the track
            val thumbVerticalCenter = trackVerticalCenter + trackHeightPx / 2  // Vertical center of the thumb

            // Draw track
            drawRoundRect(
                color = trackColor,
                size = Size(canvasWidth, trackHeightPx),
                topLeft = Offset(0f, trackVerticalCenter),
                cornerRadius = CornerRadius(trackHeightPx/4)
            )

            // Draw thumb
            drawCircle(
                color = thumbColor,
                radius = thumbRadiusPx,
                center = Offset(sliderX, thumbVerticalCenter)

            )
        }
    }
}

