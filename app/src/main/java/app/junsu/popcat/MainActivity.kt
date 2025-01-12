package app.junsu.popcat

import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.junsu.popcat.ui.theme.PopCatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PopCatTheme {
                Popcat(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun Popcat(
    modifier: Modifier = Modifier,
) {
    var pressed by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val soundPool = remember { SoundPool.Builder().setMaxStreams(10).build() }
    var popSoundId: Int? = null

    LaunchedEffect(Unit) {
        popSoundId = soundPool.load(
            context,
            // resource ID
            R.raw.pop_sound,
            // priority
            1,
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(
                        if (pressed) {
                            R.drawable.popcat_open
                        } else {
                            R.drawable.popcat_closed
                        },
                    ),
                    contentDescription = "Popcat Icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    soundPool.play(popSoundId!!, 1f, 1f, 0, 0, 1f)
                                    clickCount++
                                    pressed = true
                                    try {
                                        awaitRelease()
                                    } finally {
                                        pressed = false
                                    }
                                },
                            )
                        }
                        .height(400.dp),
                )
                Text(
                    "Current count: $clickCount",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
