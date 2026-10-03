package com.example.proband

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proband.ui.theme.ProBANDTheme
import kotlinx.coroutines.launch
import kotlin.math.min

class DrumActivity : ComponentActivity() {

    private lateinit var soundPool: SoundPool

    // ============================================================
    // SOUND IDS
    // ============================================================

    private var kickSound = 0
    private var snareSound = 0
    private var tomMidHighSound = 0
    private var tomHighSound = 0
    private var tomFloorSound = 0
    private var hiHatOpenSound = 0
    private var hiHatClosedSound = 0
    private var crashSound = 0
    private var rideSound = 0
    private var splashSound = 0


    // ============================================================
    // ACTIVITY
    // ============================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createSoundPool()

        setContent {
            DrumScreen(
                onBack = {
                    finish()
                },
                onPlayDrum = { type ->
                    playDrumSound(type)
                }
            )
        }
    }


    // ============================================================
    // CREATE SOUND POOL
    // ============================================================

    private fun createSoundPool() {

        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        soundPool =
            SoundPool.Builder()
                .setMaxStreams(16)
                .setAudioAttributes(audioAttributes)
                .build()

        kickSound = loadSound("kick")
        snareSound = loadSound("snare")
        tomMidHighSound = loadSound("tom_midhigh")
        tomHighSound = loadSound("tom_high")
        tomFloorSound = loadSound("tom_floor")
        hiHatOpenSound = loadSound("hihat_open")
        hiHatClosedSound = loadSound("hihat_closed")
        crashSound = loadSound("crash")
        rideSound = loadSound("ride")
        splashSound = loadSound("splash")
    }


    // ============================================================
    // LOAD SOUND
    // ============================================================

    private fun loadSound(fileName: String): Int {
        val resourceId = resources.getIdentifier(fileName, "raw", packageName)
        return if (resourceId != 0) {
            soundPool.load(this, resourceId, 1)
        } else {
            android.util.Log.e("PROBAND_DRUM", "WAV NOT FOUND: $fileName.wav")
            0
        }
    }


    // ============================================================
    // PLAY DRUM SOUND
    // ============================================================

    private fun playDrumSound(type: DrumType) {
        if (!::soundPool.isInitialized) return

        val soundId = when (type) {
            DrumType.KICK -> kickSound
            DrumType.SNARE -> snareSound
            DrumType.TOM_MIDHIGH -> tomMidHighSound
            DrumType.TOM_HIGH -> tomHighSound
            DrumType.TOM_FLOOR -> tomFloorSound
            DrumType.HIHAT_OPEN -> hiHatOpenSound
            DrumType.HIHAT_CLOSED -> hiHatClosedSound
            DrumType.CRASH_LEFT -> crashSound
            DrumType.CRASH_RIGHT -> crashSound
        }

        if (soundId == 0) {
            android.util.Log.e("PROBAND_DRUM", "Sound ID is 0 for type: $type")
            return
        }

        val finalVolume = SoundSettings.drumFinalVolume()

        soundPool.play(soundId, finalVolume, finalVolume, 1, 0, 1.0f)
    }

    override fun onDestroy() {
        if (::soundPool.isInitialized) {
            soundPool.release()
        }
        super.onDestroy()
    }
}

// ============================================================
// DRUM TYPES
// ============================================================

enum class DrumType {
    KICK, SNARE, TOM_MIDHIGH, TOM_HIGH, TOM_FLOOR, HIHAT_OPEN, HIHAT_CLOSED, CRASH_LEFT, CRASH_RIGHT
}

// ============================================================
// DRUM SCREEN
// ============================================================

@Composable
fun DrumScreen(
    onBack: () -> Unit,
    onPlayDrum: (DrumType) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Animation scales/rotations for drum elements
    val animKickScale = remember { Animatable(1f) }
    val animSnareScale = remember { Animatable(1f) }
    val animLeftTomScale = remember { Animatable(1f) }
    val animCenterTomScale = remember { Animatable(1f) }
    val animRightTomScale = remember { Animatable(1f) }
    val animFloorTomScale = remember { Animatable(1f) }
    val animLeftCrashRotation = remember { Animatable(0f) }
    val animRightCrashRotation = remember { Animatable(0f) }
    val animLeftHihatRotation = remember { Animatable(0f) }
    val animRightHihatRotation = remember { Animatable(0f) }

    fun triggerDrumAnimation(type: DrumType) {
        coroutineScope.launch {
            when (type) {
                DrumType.KICK -> {
                    animKickScale.animateTo(0.88f, animationSpec = tween(60))
                    animKickScale.animateTo(1f, animationSpec = tween(120))
                }
                DrumType.SNARE -> {
                    animSnareScale.animateTo(0.91f, animationSpec = tween(50))
                    animSnareScale.animateTo(1f, animationSpec = tween(110))
                }
                DrumType.TOM_MIDHIGH -> {
                    animLeftTomScale.animateTo(0.91f, animationSpec = tween(50))
                    animLeftTomScale.animateTo(1f, animationSpec = tween(110))
                }
                DrumType.TOM_HIGH -> {
                    animCenterTomScale.animateTo(0.91f, animationSpec = tween(50))
                    animCenterTomScale.animateTo(1f, animationSpec = tween(110))
                }
                DrumType.TOM_FLOOR -> {
                    animFloorTomScale.animateTo(0.91f, animationSpec = tween(50))
                    animFloorTomScale.animateTo(1f, animationSpec = tween(110))
                }
                DrumType.CRASH_LEFT -> {
                    animLeftCrashRotation.animateTo(7f, animationSpec = tween(55))
                    animLeftCrashRotation.animateTo(-5f, animationSpec = tween(65))
                    animLeftCrashRotation.animateTo(3f, animationSpec = tween(50))
                    animLeftCrashRotation.animateTo(0f, animationSpec = tween(90))
                }
                DrumType.CRASH_RIGHT -> {
                    animRightCrashRotation.animateTo(7f, animationSpec = tween(55))
                    animRightCrashRotation.animateTo(-5f, animationSpec = tween(65))
                    animRightCrashRotation.animateTo(3f, animationSpec = tween(50))
                    animRightCrashRotation.animateTo(0f, animationSpec = tween(90))
                }
                DrumType.HIHAT_CLOSED -> {
                    animLeftHihatRotation.animateTo(7f, animationSpec = tween(55))
                    animLeftHihatRotation.animateTo(-5f, animationSpec = tween(65))
                    animLeftHihatRotation.animateTo(3f, animationSpec = tween(50))
                    animLeftHihatRotation.animateTo(0f, animationSpec = tween(90))
                }
                DrumType.HIHAT_OPEN -> {
                    animRightHihatRotation.animateTo(7f, animationSpec = tween(55))
                    animRightHihatRotation.animateTo(-5f, animationSpec = tween(65))
                    animRightHihatRotation.animateTo(3f, animationSpec = tween(50))
                    animRightHihatRotation.animateTo(0f, animationSpec = tween(90))
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.drum_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Drum kit placement using responsive scaling bounding box
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val screenWidth = constraints.maxWidth.toFloat()
            val screenHeight = constraints.maxHeight.toFloat()
            val density = LocalDensity.current

            val referenceWidth = 1600f
            val referenceHeight = 900f

            val scale = min(screenWidth / referenceWidth, screenHeight / referenceHeight)

            val usedWidth = referenceWidth * scale
            val usedHeight = referenceHeight * scale

            val offsetX = (screenWidth - usedWidth) / 2f
            val offsetY = (screenHeight - usedHeight) / 2f

            @Composable
            fun DrumInstrument(
                resId: Int,
                x: Float,
                y: Float,
                width: Float,
                height: Float,
                contentDescription: String,
                type: DrumType,
                scaleX: Float = 1f,
                scaleY: Float = 1f,
                rotation: Float = 0f
            ) {
                val finalWidth = with(density) { (width * scale).toDp() }
                val finalHeight = with(density) { (height * scale).toDp() }
                val leftMargin = with(density) { (offsetX + x * scale).toDp() }
                val topMargin = with(density) { (offsetY + y * scale).toDp() }

                Image(
                    painter = painterResource(id = resId),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .offset(x = leftMargin, y = topMargin)
                        .size(width = finalWidth, height = finalHeight)
                        .graphicsLayer(
                            scaleX = scaleX,
                            scaleY = scaleY,
                            rotationZ = rotation
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onPlayDrum(type)
                            triggerDrumAnimation(type)
                        },
                    contentScale = ContentScale.Fit
                )
            }

            // Left Crash
            DrumInstrument(
                resId = R.drawable.left_crash,
                x = 10f, y = 20f, width = 385f, height = 385f,
                contentDescription = "Left Crash Cymbal",
                type = DrumType.CRASH_LEFT,
                rotation = animLeftCrashRotation.value
            )

            // Left Hi-hat
            DrumInstrument(
                resId = R.drawable.left_hihat,
                x = 0f, y = 400f, width = 275f, height = 275f,
                contentDescription = "Left Hi-Hat",
                type = DrumType.HIHAT_CLOSED,
                rotation = animLeftHihatRotation.value
            )

            // Snare Drum
            DrumInstrument(
                resId = R.drawable.snare_drum,
                x = 330f, y = 210f, width = 360f, height = 360f,
                contentDescription = "Snare Drum",
                type = DrumType.SNARE,
                scaleX = animSnareScale.value,
                scaleY = animSnareScale.value
            )

            // Left Tom
            DrumInstrument(
                resId = R.drawable.left_tom,
                x = 245f, y = 480f, width = 360f, height = 360f,
                contentDescription = "Left Tom",
                type = DrumType.TOM_MIDHIGH,
                scaleX = animLeftTomScale.value,
                scaleY = animLeftTomScale.value
            )

            // Center Tom
            DrumInstrument(
                resId = R.drawable.center_tom,
                x = 625f, y = 30f, width = 360f, height = 360f,
                contentDescription = "Center Tom",
                type = DrumType.TOM_HIGH,
                scaleX = animCenterTomScale.value,
                scaleY = animCenterTomScale.value
            )

            // Right Tom
            DrumInstrument(
                resId = R.drawable.right_tom,
                x = 910f, y = 210f, width = 360f, height = 360f,
                contentDescription = "Right Tom",
                type = DrumType.TOM_HIGH,
                scaleX = animRightTomScale.value,
                scaleY = animRightTomScale.value
            )

            // Right Crash
            DrumInstrument(
                resId = R.drawable.right_crash,
                x = 1195f, y = 20f, width = 395f, height = 395f,
                contentDescription = "Right Crash Cymbal",
                type = DrumType.CRASH_RIGHT,
                rotation = animRightCrashRotation.value
            )

            // Right Hi-hat
            DrumInstrument(
                resId = R.drawable.right_hihat,
                x = 1380f, y = 400f, width = 275f, height = 275f,
                contentDescription = "Right Hi-Hat",
                type = DrumType.HIHAT_OPEN,
                rotation = animRightHihatRotation.value
            )

            // Floor Tom
            DrumInstrument(
                resId = R.drawable.floor_tom,
                x = 1000f, y = 480f, width = 360f, height = 360f,
                contentDescription = "Floor Tom",
                type = DrumType.TOM_FLOOR,
                scaleX = animFloorTomScale.value,
                scaleY = animFloorTomScale.value
            )

            // Kick Drum
            DrumInstrument(
                resId = R.drawable.kick_drum,
                x = 545f, y = 315f, width = 510f, height = 500f,
                contentDescription = "Kick Drum",
                type = DrumType.KICK,
                scaleX = animKickScale.value,
                scaleY = animKickScale.value
            )
        }

        // Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 10.dp)
                .size(50.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
fun DrumScreenPreview() {
    ProBANDTheme {
        DrumScreen(
            onBack = {},
            onPlayDrum = {}
        )
    }
}
