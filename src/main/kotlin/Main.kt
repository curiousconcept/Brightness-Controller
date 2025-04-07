import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.Logger

@Composable
@Preview
fun App() {
    val fontSize: TextUnit = 50.sp    // Adjust font size based on DPI scaling

    val brightnessController: BrightnessController = DccBrightnessControllerImpl()

    Column(modifier = Modifier.padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Brightness slider",
            fontSize = fontSize,
            color = Color.Black,
        )
        debouncedSlider(brightnessController)
    }
}

@Composable
fun debouncedSlider(brightnessController: BrightnessController) {

    var sliderValue by remember { mutableStateOf(0f) }

    var debounceJob: Job? = null

    val currentBrightness by remember { mutableStateOf(brightnessController.getCurrentBrightness().toFloat()) }

    Text(text = "Value: ${if (sliderValue == 0f) currentBrightness.toInt() else sliderValue.toInt()}")

    val coroutineScope = rememberCoroutineScope()

    CustomSlider(
        onValueChange = {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch(Dispatchers.IO) {
                sliderValue = it
                delay(500)  // 500ms delay to debounce the value change

                brightnessController.updateBrightness(sliderValue.toInt())
            }
        },
        initialSliderValue = currentBrightness,
        valueRange = 0f..100f, // Example range
        trackColor = Color.Gray,
        thumbColor = Color.Blue,
        thumbRadius = 30.dp,
        trackHeight = 50.dp
    )
}


fun main() = application {
//    System.setProperty("skiko.renderApi", "SOFTWARE")
//    setupLogging()

    Window(onCloseRequest = ::exitApplication, title = "Brightness Controller") {
        App()
    }

    val renderApi = System.getProperty("skiko.renderApi", "UNKNOWN")
    println("Current Render API: $renderApi")

    val gdkGl = System.getenv("GDK_GL") ?: "UNKNOWN"
    println("GDK_GL environment variable: $gdkGl")
}

fun setupLogging() {
    val rootLogger = Logger.getLogger("")
    rootLogger.level = Level.FINE

    rootLogger.handlers.forEach {
        it.level = Level.FINE
    }
}