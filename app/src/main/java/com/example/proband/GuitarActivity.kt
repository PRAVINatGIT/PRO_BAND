package com.example.proband

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proband.ui.theme.ProBANDTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow


class GuitarActivity : ComponentActivity() {

    // ============================================================
    // SOUND POOL
    // ============================================================

    private lateinit var soundPool: SoundPool

    // Six guitar string sound IDs
    private val stringSoundIds =
        IntArray(6)

    // Currently playing stream for each string
    private val activeStreams =
        IntArray(6)


    // ============================================================
    // ACTIVITY
    // ============================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        initAudio()

        setContent {

            GuitarScreen(
                onBack = {
                    finish()
                },

                onPlayString = {
                        stringIndex,
                        fretOffset,
                        volume ->

                    playGuitarString(
                        stringIndex =
                            stringIndex,

                        fretOffset =
                            fretOffset,

                        volume =
                            volume
                    )
                }
            )
        }
    }


    // ============================================================
    // INITIALIZE AUDIO
    // ============================================================

    private fun initAudio() {

        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(
                    AudioAttributes.USAGE_GAME
                )
                .setContentType(
                    AudioAttributes.CONTENT_TYPE_MUSIC
                )
                .build()


        soundPool =
            SoundPool.Builder()
                .setMaxStreams(16)
                .setAudioAttributes(
                    audioAttributes
                )
                .build()


        // ========================================================
        // GUITAR STRING FILES
        // ========================================================

        val rawFiles =
            listOf(

                // String 6 - Low E
                "guitar_e2",

                // String 5 - A
                "guitar_a2",

                // String 4 - D
                "guitar_d3",

                // String 3 - G
                "guitar_g3",

                // String 2 - B
                "guitar_b3",

                // String 1 - High E
                "guitar_e4"
            )


        // ========================================================
        // LOAD FILES
        // ========================================================

        rawFiles.forEachIndexed {
                index,
                fileName ->

            val resourceId =
                resources.getIdentifier(
                    fileName,
                    "raw",
                    packageName
                )


            if (resourceId != 0) {

                stringSoundIds[index] =
                    soundPool.load(
                        this,
                        resourceId,
                        1
                    )

            } else {

                android.util.Log.e(
                    "PROBAND_GUITAR",
                    "WAV NOT FOUND: $fileName.wav"
                )
            }
        }
    }


    // ============================================================
    // PLAY GUITAR STRING
    // ============================================================

    private fun playGuitarString(
        stringIndex: Int,
        fretOffset: Int,
        volume: Float = 1.0f
    ) {

        if (!::soundPool.isInitialized) {
            return
        }


        // ========================================================
        // VALID STRING
        // ========================================================

        if (stringIndex !in 0..5) {
            return
        }


        // ========================================================
        // MUTED STRING
        // ========================================================

        if (fretOffset < 0) {
            return
        }


        val soundId =
            stringSoundIds[stringIndex]


        if (soundId == 0) {
            return
        }


        // ========================================================
        // FRET -> PITCH
        //
        // Every 12 semitones = one octave.
        // ========================================================

        val playbackRate =
            2.0
                .pow(
                    fretOffset / 12.0
                )
                .toFloat()
                .coerceIn(
                    0.5f,
                    2.0f
                )


        // ========================================================
        // VOLUME
        //
        // Master × Guitar × String/Strum volume
        // ========================================================

        val finalVolume =
            (
                    SoundSettings.masterVolume *
                            SoundSettings.guitarVolume *
                            volume
                    )
                .coerceIn(
                    0.0f,
                    1.0f
                )


        // ========================================================
        // STOP PREVIOUS SOUND
        // ========================================================

        if (
            activeStreams[stringIndex] != 0
        ) {

            soundPool.stop(
                activeStreams[stringIndex]
            )
        }


        // ========================================================
        // PLAY
        // ========================================================

        val streamId =
            soundPool.play(

                soundId,

                finalVolume,

                finalVolume,

                1,

                0,

                playbackRate
            )


        activeStreams[stringIndex] =
            streamId


        android.util.Log.d(
            "PROBAND_GUITAR",
            "String=$stringIndex " +
                    "Fret=$fretOffset " +
                    "Volume=$finalVolume " +
                    "Stream=$streamId"
        )
    }


    // ============================================================
    // CLEAN UP
    // ============================================================

    override fun onDestroy() {

        if (::soundPool.isInitialized) {

            soundPool.release()
        }

        super.onDestroy()
    }
}


// ============================================================
// CHORD DATA
// ============================================================

data class GuitarChord(

    val name: String,

    // Order:
    //
    // Low E
    // A
    // D
    // G
    // B
    // High E
    //
    // -1 = muted
    //  0 = open
    //  1+ = fret number

    val frets: List<Int>
)


// ============================================================
// PRESET CHORDS
// ============================================================

val PRESET_CHORDS = listOf(

    GuitarChord(
        "Open",
        listOf(
            0,
            0,
            0,
            0,
            0,
            0
        )
    ),

    GuitarChord(
        "C",
        listOf(
            -1,
            3,
            2,
            0,
            1,
            0
        )
    ),

    GuitarChord(
        "G",
        listOf(
            3,
            2,
            0,
            0,
            0,
            3
        )
    ),

    GuitarChord(
        "D",
        listOf(
            -1,
            -1,
            0,
            2,
            3,
            2
        )
    ),

    GuitarChord(
        "Em",
        listOf(
            0,
            2,
            2,
            0,
            0,
            0
        )
    ),

    GuitarChord(
        "Am",
        listOf(
            -1,
            0,
            2,
            2,
            1,
            0
        )
    ),

    GuitarChord(
        "F",
        listOf(
            1,
            3,
            3,
            2,
            1,
            1
        )
    ),

    GuitarChord(
        "A",
        listOf(
            -1,
            0,
            2,
            2,
            2,
            0
        )
    )
)


// ============================================================
// GUITAR SCREEN
// ============================================================

@Composable
fun GuitarScreen(
    onBack: () -> Unit,

    onPlayString: (
        stringIndex: Int,
        fretOffset: Int,
        volume: Float
    ) -> Unit
) {

    // ========================================================
    // SELECTED CHORD
    // ========================================================

    var selectedChordIndex by remember {

        mutableIntStateOf(0)
    }


    val activeChord =
        PRESET_CHORDS[
            selectedChordIndex
        ]


    // ========================================================
    // COROUTINE
    // ========================================================

    val coroutineScope =
        rememberCoroutineScope()


    // ========================================================
    // STRING ANIMATIONS
    // ========================================================

    val stringVibrations =
        List(6) {

            remember {
                Animatable(0f)
            }
        }


    // ========================================================
    // TRIGGER ONE STRING
    // ========================================================

    fun triggerString(
        index: Int,
        volume: Float = 0.9f
    ) {

        if (index !in 0..5) {
            return
        }


        val fret =
            activeChord.frets[index]


        // Muted string
        if (fret < 0) {
            return
        }


        // Play audio
        onPlayString(
            index,
            fret,
            volume
        )


        // ======================================================
        // STRING VIBRATION
        // ======================================================

        coroutineScope.launch {

            val amplitude =
                volume.coerceIn(
                    0.4f,
                    1.0f
                )


            // Strong movement
            stringVibrations[index]
                .animateTo(
                    amplitude,
                    animationSpec =
                        tween(35)
                )


            // Reverse
            stringVibrations[index]
                .animateTo(
                    -amplitude * 0.85f,
                    animationSpec =
                        tween(45)
                )


            // Smaller movement
            stringVibrations[index]
                .animateTo(
                    amplitude * 0.50f,
                    animationSpec =
                        tween(45)
                )


            // Smaller movement
            stringVibrations[index]
                .animateTo(
                    -amplitude * 0.25f,
                    animationSpec =
                        tween(45)
                )


            // Rest
            stringVibrations[index]
                .animateTo(
                    0f,
                    animationSpec =
                        tween(70)
                )
        }
    }


    // ========================================================
    // STRUM ALL STRINGS
    // ========================================================

    fun strumAllStrings(
        downwards: Boolean = true
    ) {

        coroutineScope.launch {

            val range =
                if (downwards) {

                    0..5

                } else {

                    5 downTo 0
                }


            for (index in range) {

                if (
                    activeChord.frets[index] >= 0
                ) {

                    triggerString(
                        index,
                        1.0f
                    )


                    delay(35)
                }
            }
        }
    }


    // ========================================================
    // MAIN SCREEN
    // ========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF1E140F)
            )
    ) {


        // ======================================================
        // TOP BAR
        // ======================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(
                    Color(0xFF2C1810)
                )
                .padding(
                    horizontal = 12.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // ==================================================
            // BACK BUTTON
            // ==================================================

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,

                    contentDescription =
                        "Back",

                    tint =
                        Color.White,

                    modifier =
                        Modifier.size(30.dp)
                )
            }


            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )


            // ==================================================
            // TITLE
            // ==================================================

            Text(
                text =
                    "Acoustic Guitar",

                color =
                    Color.White,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.width(16.dp)
            )


            // ==================================================
            // CHORD SELECTOR
            // ==================================================

            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(
                        rememberScrollState()
                    ),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                PRESET_CHORDS.forEachIndexed {

                        index,
                        chord ->

                    val isSelected =
                        index ==
                                selectedChordIndex


                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    16.dp
                                )
                            )
                            .background(
                                if (isSelected) {

                                    Color(
                                        0xFFE28743
                                    )

                                } else {

                                    Color(
                                        0xFF4A2818
                                    )
                                }
                            )
                            .clickable {

                                selectedChordIndex =
                                    index

                                strumAllStrings(
                                    true
                                )
                            }
                            .padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            )
                    ) {

                        Text(
                            text =
                                chord.name,

                            color =
                                if (isSelected) {

                                    Color.Black

                                } else {

                                    Color.White
                                },

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                15.sp
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )


            // ==================================================
            // STRUM BUTTON
            // ==================================================

            Button(
                onClick = {

                    strumAllStrings(
                        true
                    )
                },

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFE28743)
                    ),

                shape =
                    RoundedCornerShape(
                        10.dp
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.PlayArrow,

                    contentDescription =
                        "Strum",

                    tint =
                        Color.Black,

                    modifier =
                        Modifier.size(18.dp)
                )


                Spacer(
                    modifier =
                        Modifier.width(4.dp)
                )


                Text(
                    text =
                        "Strum",

                    color =
                        Color.Black,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        // ========================================================
        // GUITAR AREA
        // ========================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(

                    Brush.verticalGradient(

                        colors = listOf(

                            Color(0xFF3B1E12),

                            Color(0xFF28140B),

                            Color(0xFF3B1E12)
                        )
                    )
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp
                )
        ) {


            // ==================================================
            // FRETS
            // ==================================================

            Row(
                modifier =
                    Modifier.fillMaxSize(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                repeat(8) {

                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight()
                            .background(
                                Color(0xFF9E8E81)
                            )
                    )
                }
            }


            // ==================================================
            // PEARL INLAYS
            // ==================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(
                        Alignment.Center
                    ),

                horizontalArrangement =
                    Arrangement.SpaceEvenly
            ) {

                repeat(4) {

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(
                                CircleShape
                            )
                            .background(
                                Color(
                                    0x66E5D3B3
                                )
                            )
                    )
                }
            }


            // ==================================================
            // STRINGS
            // ==================================================

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(
                        selectedChordIndex
                    ) {

                        var lastString =
                            -1

                        var lastTimeMs =
                            System.currentTimeMillis()


                        detectDragGestures(

                            // ==================================
                            // DRAG START
                            // ==================================

                            onDragStart = {
                                    offset ->

                                val stringHeight =
                                    size.height /
                                            6f


                                val stringIndex =
                                    (
                                            offset.y /
                                                    stringHeight
                                            )
                                        .toInt()
                                        .coerceIn(
                                            0,
                                            5
                                        )


                                lastString =
                                    stringIndex


                                lastTimeMs =
                                    System.currentTimeMillis()


                                triggerString(
                                    stringIndex,
                                    0.9f
                                )
                            },


                            // ==================================
                            // DRAG
                            // ==================================

                            onDrag = {
                                    change,
                                    _ ->

                                change.consume()


                                val stringHeight =
                                    size.height /
                                            6f


                                val stringIndex =
                                    (
                                            change.position.y /
                                                    stringHeight
                                            )
                                        .toInt()
                                        .coerceIn(
                                            0,
                                            5
                                        )


                                if (
                                    stringIndex !=
                                    lastString
                                ) {

                                    val now =
                                        System.currentTimeMillis()


                                    val deltaTime =
                                        (
                                                now -
                                                        lastTimeMs
                                                )
                                            .coerceAtLeast(
                                                10
                                            )


                                    lastTimeMs =
                                        now


                                    // Faster swipe =
                                    // louder strum

                                    val strumVolume =
                                        (
                                                1.15f -
                                                        (
                                                                deltaTime /
                                                                        250f
                                                                )
                                                )
                                            .coerceIn(
                                                0.5f,
                                                1.0f
                                            )


                                    lastString =
                                        stringIndex


                                    triggerString(
                                        stringIndex,
                                        strumVolume
                                    )
                                }
                            },


                            // ==================================
                            // DRAG END
                            // ==================================

                            onDragEnd = {

                                lastString =
                                    -1
                            },


                            // ==================================
                            // CANCEL
                            // ==================================

                            onDragCancel = {

                                lastString =
                                    -1
                            }
                        )
                    },

                verticalArrangement =
                    Arrangement.SpaceEvenly
            ) {


                // ==================================================
                // STRING LABELS
                // ==================================================

                val stringLabels =
                    listOf(
                        "6 - E",
                        "5 - A",
                        "4 - D",
                        "3 - G",
                        "2 - B",
                        "1 - E"
                    )


                // ==================================================
                // STRING THICKNESS
                // ==================================================

                val stringGauges =
                    listOf(
                        5.5.dp,
                        4.5.dp,
                        3.5.dp,
                        2.5.dp,
                        1.8.dp,
                        1.2.dp
                    )


                // ==================================================
                // STRING COLORS
                // ==================================================

                val stringColors =
                    listOf(
                        Color(0xFFB8860B),
                        Color(0xFFCD9B1D),
                        Color(0xFFDAA520),
                        Color(0xFFE8C15A),
                        Color(0xFFCCCCCC),
                        Color(0xFFEAEAEA)
                    )


                // ==================================================
                // CREATE ALL SIX STRINGS
                // ==================================================

                stringLabels.forEachIndexed {

                        index,
                        label ->


                    val isMuted =
                        activeChord.frets[index] <
                                0


                    val fretNumber =
                        activeChord.frets[index]


                    val vibration =
                        stringVibrations[
                            index
                        ].value


                    val vibrationOffset =
                        vibration *
                                4.dp.value


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .pointerInput(
                                index,
                                selectedChordIndex
                            ) {

                                detectTapGestures(

                                    onTap = {

                                        triggerString(
                                            index,
                                            0.9f
                                        )
                                    }
                                )
                            },

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {


                        // ==================================================
                        // STRING LABEL
                        // ==================================================

                        Box(
                            modifier = Modifier
                                .width(65.dp)
                                .height(26.dp)
                                .clip(
                                    RoundedCornerShape(
                                        4.dp
                                    )
                                )
                                .background(

                                    if (isMuted) {

                                        Color(0xFF6B1A1A)

                                    } else {

                                        Color(0xFF4A2E1E)
                                    }
                                )
                                .border(
                                    1.dp,
                                    Color(0xFF8B5E3C),
                                    RoundedCornerShape(
                                        4.dp
                                    )
                                ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(

                                text =

                                    if (isMuted) {

                                        "X ($label)"

                                    } else {

                                        "$label ($fretNumber)"
                                    },

                                color =

                                    if (isMuted) {

                                        Color.LightGray

                                    } else {

                                        Color.White
                                    },

                                fontSize =
                                    11.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }


                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )


                        // ==================================================
                        // GUITAR STRING
                        // ==================================================

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .offset(
                                    y =
                                        vibrationOffset.dp
                                )
                                .height(
                                    stringGauges[
                                        index
                                    ]
                                )
                                .background(

                                    if (isMuted) {

                                        Color.Gray.copy(
                                            alpha =
                                                0.4f
                                        )

                                    } else {

                                        stringColors[
                                            index
                                        ]
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
fun GuitarScreenPreview() {
    ProBANDTheme {
        GuitarScreen(
            onBack = {},
            onPlayString = { _, _, _ -> }
        )
    }
}
