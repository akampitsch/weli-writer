import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun App() {
    MaterialTheme {
        HomeScreen()
    }
}

expect fun getPlatformName(): String
