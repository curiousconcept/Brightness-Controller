//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.onSizeChanged
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@Composable
//fun CustomIntSlider(
//    value: Int = 1,
//    onValueChange: (Int) -> Unit,
//    valueRange: IntRange,
//    modifier: Modifier = Modifier,
//    trackHeight: Dp = 15.dp,
//    thumbRadius: Dp = 14.dp,
//    trackColor: Color = Color.Gray,
//    thumbColor: Color = Color.Blue
//) {
//    val density = LocalDensity.current
//    val trackHeightPx = with(density) { trackHeight.toPx() }
//    val thumbRadiusPx = with(density) { thumbRadius.toPx() }
//
//    val internalValue = remember { mutableStateOf(value) }
//    val coroutineScope = rememberCoroutineScope()
//    val sliderWidth = remember { mutableStateOf(0f) }
//    var debounceJob: Job? = null
//
//    val min = valueRange.first
//    val max = valueRange.last
//    val range = max - min
//
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(thumbRadius * 3)
//            .padding(horizontal = 16.dp)
//            .onSizeChanged { sliderWidth.value = it.width.toFloat() }
//            .pointerInput(Unit) {
//                detectDragGestures { change, _ ->
//                    val ratio = (change.position.x / sliderWidth.value).coerceIn(0f, 1f)
//                    val newValue = (min + (ratio * range)).toInt().coerceIn(min, max)
//
//                    internalValue.value = newValue
//
//                    debounceJob?.cancel()
//
//                    debounceJob = coroutineScope.launch {
//                        delay(500)  // Delay after which we know dragging has stopped
//                        onValueChange(newValue)  // Execute once the dragging is complete
//                    }
//                }
//            }
//            .pointerInput(Unit) {
//                detectTapGestures { offset ->
//                    val ratio = (offset.x / sliderWidth.value).coerceIn(0f, 1f)
//                    val newValue = (min + (ratio * range)).toInt().coerceIn(min, max)
//                    internalValue.value = newValue
//                    onValueChange(newValue)
//                }
//            }
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val sliderX = ((internalValue.value - min).toFloat() / range.toFloat()) * size.width
//
//            drawRect(
//                color = trackColor,
//                size = Size(size.width, trackHeightPx),
////                cornerRadius = CornerRadius(trackHeightPx / 2)
//            )
//
//            drawCircle(
//                color = thumbColor,
//                radius = thumbRadiusPx,
//                center = Offset(sliderX, trackHeightPx / 2)
//            )
//        }
//    }
//}
