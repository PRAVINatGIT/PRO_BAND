package com.example.proband

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proband.ui.theme.ProBANDTheme


class KeyboardActivity : ComponentActivity() {

    // ==========================================================
    // SOUND POOL
    // ==========================================================

    private lateinit var soundPool: SoundPool

    // Example:
    // "c4"  -> SoundPool ID
    // "cs4" -> SoundPool ID
    // "as4" -> SoundPool ID
    private val pianoSounds =
        mutableMapOf<String, Int>()

    // Sound IDs that have finished loading
    private val loadedSounds =
        mutableSetOf<Int>()

    // Currently playing stream for each note
    private val activeStreams =
        mutableMapOf<String, Int>()


    // ==========================================================
    // ACTIVITY
    // ==========================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        createPianoSoundPool()

        setContent {

            KeyboardScreen(
                onBack = {
                    finish()
                },

                onNotePressed = { note ->
                    playPianoNote(note)
                },

                onNoteReleased = { note, pedalOn ->
                    releasePianoNote(
                        note = note,
                        pedalOn = pedalOn
                    )
                }
            )
        }
    }


    // ==========================================================
    // CREATE SOUND POOL
    // ==========================================================

    private fun createPianoSoundPool() {

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
                .setMaxStreams(32)
                .setAudioAttributes(
                    audioAttributes
                )
                .build()


        // ======================================================
        // SOUND LOAD LISTENER
        // ======================================================

        soundPool.setOnLoadCompleteListener {
                _,
                sampleId,
                status ->

            if (status == 0) {

                loadedSounds.add(
                    sampleId
                )

                android.util.Log.d(
                    "PROBAND_AUDIO",
                    "Loaded sound ID: $sampleId"
                )
            }
        }


        // ======================================================
        // YOUR EXISTING FILE NAMES
        //
        // c4.wav
        // cs4.wav
        // d4.wav
        // ds4.wav
        // ...
        //
        // No "piano_" prefix.
        // ======================================================

        val pianoNotes =
            listOf(

                // ------------------------------------------------
                // OCTAVE 1
                // ------------------------------------------------

                "c1",
                "cs1",
                "d1",
                "ds1",
                "e1",
                "f1",
                "fs1",
                "g1",
                "gs1",
                "a1",
                "as1",
                "b1",


                // ------------------------------------------------
                // OCTAVE 2
                // ------------------------------------------------

                "c2",
                "cs2",
                "d2",
                "ds2",
                "e2",
                "f2",
                "fs2",
                "g2",
                "gs2",
                "a2",
                "as2",
                "b2",


                // ------------------------------------------------
                // OCTAVE 3
                // ------------------------------------------------

                "c3",
                "cs3",
                "d3",
                "ds3",
                "e3",
                "f3",
                "fs3",
                "g3",
                "gs3",
                "a3",
                "as3",
                "b3",


                // ------------------------------------------------
                // OCTAVE 4
                // ------------------------------------------------

                "c4",
                "cs4",
                "d4",
                "ds4",
                "e4",
                "f4",
                "fs4",
                "g4",
                "gs4",
                "a4",
                "as4",
                "b4",


                // ------------------------------------------------
                // OCTAVE 5
                // ------------------------------------------------

                "c5",
                "cs5",
                "d5",
                "ds5",
                "e5",
                "f5",
                "fs5",
                "g5",
                "gs5",
                "a5",
                "as5",
                "b5",


                // ------------------------------------------------
                // OCTAVE 6
                // ------------------------------------------------

                "c6",
                "cs6",
                "d6",
                "ds6",
                "e6",
                "f6",
                "fs6",
                "g6",
                "gs6",
                "a6",
                "as6",
                "b6",


                // ------------------------------------------------
                // OCTAVE 7
                // ------------------------------------------------

                "c7",
                "cs7",
                "d7",
                "ds7",
                "e7",
                "f7",
                "fs7",
                "g7",
                "gs7",
                "a7",
                "as7",
                "b7"
            )


        // ======================================================
        // LOAD WAV FILES
        // ======================================================

        for (note in pianoNotes) {

            val resourceId =
                resources.getIdentifier(
                    note,
                    "raw",
                    packageName
                )


            if (resourceId != 0) {

                val soundId =
                    soundPool.load(
                        this,
                        resourceId,
                        1
                    )

                pianoSounds[note] =
                    soundId

            } else {

                android.util.Log.e(
                    "PROBAND_AUDIO",
                    "WAV NOT FOUND: $note.wav"
                )
            }
        }
    }


    // ==========================================================
    // PLAY PIANO NOTE
    // ==========================================================

    private fun playPianoNote(
        note: String
    ) {

        if (!::soundPool.isInitialized) {
            return
        }


        // ======================================================
        // CONVERT UI NOTE TO FILE NAME
        //
        // C4  -> c4
        // C#4 -> cs4
        // D#4 -> ds4
        // F#4 -> fs4
        // G#4 -> gs4
        // A#4 -> as4
        // ======================================================

        val soundName =
            note
                .lowercase()
                .replace(
                    "#",
                    "s"
                )


        val soundId =
            pianoSounds[soundName]


        if (soundId == null) {

            android.util.Log.e(
                "PROBAND_AUDIO",
                "SOUND NOT FOUND: $soundName"
            )

            return
        }


        if (soundId == 0) {
            return
        }


        // ======================================================
        // CHECK LOAD STATUS
        // ======================================================

        if (!loadedSounds.contains(soundId)) {

            android.util.Log.d(
                "PROBAND_AUDIO",
                "SOUND STILL LOADING: $soundName"
            )

            return
        }


        // ======================================================
        // STOP PREVIOUS INSTANCE OF SAME NOTE
        // ======================================================

        val previousStream =
            activeStreams[soundName]


        if (previousStream != null) {

            soundPool.stop(
                previousStream
            )
        }


        // ======================================================
        // GET KEYBOARD VOLUME
        //
        // MASTER × KEYBOARD
        // ======================================================

        val finalVolume =
            SoundSettings.keyboardFinalVolume()


        // ======================================================
        // PLAY NOTE
        // ======================================================

        val streamId =
            soundPool.play(

                soundId,

                finalVolume,

                finalVolume,

                1,

                0,

                1.0f
            )


        // ======================================================
        // SAVE STREAM
        // ======================================================

        activeStreams[soundName] =
            streamId


        android.util.Log.d(
            "PROBAND_AUDIO",
            "PLAYED: $soundName " +
                    "volume=$finalVolume " +
                    "stream=$streamId"
        )
    }


    // ==========================================================
    // RELEASE NOTE
    // ==========================================================

    private fun releasePianoNote(
        note: String,
        pedalOn: Boolean
    ) {

        if (!::soundPool.isInitialized) {
            return
        }


        val soundName =
            note
                .lowercase()
                .replace(
                    "#",
                    "s"
                )


        val streamId =
            activeStreams[soundName]
                ?: return


        // ======================================================
        // PEDAL OFF
        // ======================================================

        if (!pedalOn) {

            soundPool.stop(
                streamId
            )

            activeStreams.remove(
                soundName
            )


            android.util.Log.d(
                "PROBAND_PEDAL",
                "PEDAL OFF - stopped: $soundName"
            )
        }


        // ======================================================
        // PEDAL ON
        // ======================================================

        else {

            // Keep playing.
            // The WAV sample is allowed to finish naturally.

            android.util.Log.d(
                "PROBAND_PEDAL",
                "PEDAL ON - sustaining: $soundName"
            )
        }
    }


    // ==========================================================
    // STOP ALL NOTES
    // ==========================================================

    private fun stopAllNotes() {

        if (!::soundPool.isInitialized) {
            return
        }


        for (streamId in activeStreams.values) {

            soundPool.stop(
                streamId
            )
        }


        activeStreams.clear()


        android.util.Log.d(
            "PROBAND_PEDAL",
            "All notes stopped"
        )
    }


    // ==========================================================
    // DESTROY
    // ==========================================================

    override fun onDestroy() {

        stopAllNotes()


        if (::soundPool.isInitialized) {
            soundPool.release()
        }


        pianoSounds.clear()
        loadedSounds.clear()
        activeStreams.clear()


        super.onDestroy()
    }
}


// =================================================================
// KEYBOARD SCREEN
// =================================================================

@Composable
fun KeyboardScreen(
    onBack: () -> Unit,

    onNotePressed: (String) -> Unit,

    onNoteReleased: (
        String,
        Boolean
    ) -> Unit
) {

    // ==========================================================
    // VISIBLE KEYS
    // ==========================================================

    var visibleKeys by remember {
        mutableIntStateOf(8)
    }


    // ==========================================================
    // PEDAL
    // ==========================================================

    var pedalOn by remember {
        mutableStateOf(false)
    }


    // ==========================================================
    // 49 WHITE KEYS
    // ==========================================================

    val notes =
        listOf(

            "C1",
            "D1",
            "E1",
            "F1",
            "G1",
            "A1",
            "B1",

            "C2",
            "D2",
            "E2",
            "F2",
            "G2",
            "A2",
            "B2",

            "C3",
            "D3",
            "E3",
            "F3",
            "G3",
            "A3",
            "B3",

            "C4",
            "D4",
            "E4",
            "F4",
            "G4",
            "A4",
            "B4",

            "C5",
            "D5",
            "E5",
            "F5",
            "G5",
            "A5",
            "B5",

            "C6",
            "D6",
            "E6",
            "F6",
            "G6",
            "A6",
            "B6",

            "C7",
            "D7",
            "E7",
            "F7",
            "G7",
            "A7",
            "B7"
        )


    // ==========================================================
    // SCREEN WIDTH
    // ==========================================================

    val screenWidth =
        LocalConfiguration
            .current
            .screenWidthDp
            .dp


    // ==========================================================
    // KEY WIDTH
    // ==========================================================

    val whiteKeyWidth =
        screenWidth /
                visibleKeys


    // ==========================================================
    // MAIN SCREEN
    // ==========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF101010)
            )
    ) {


        // ======================================================
        // TOP BAR
        // ======================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(
                    Color(0xFF202020)
                )
                .padding(
                    horizontal = 10.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // ==================================================
            // BACK
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
                        Modifier.size(32.dp)
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
                    "Keyboard",

                color =
                    Color.White,

                fontSize =
                    25.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.weight(1f)
            )


            // ==================================================
            // PEDAL BUTTON
            // ==================================================

            Box(

                modifier = Modifier
                    .height(44.dp)
                    .width(125.dp)

                    .border(
                        width = 1.dp,

                        color =
                            if (pedalOn) {

                                Color(0xFF80FF80)

                            } else {

                                Color(0xFF777777)
                            },

                        shape =
                            RoundedCornerShape(
                                5.dp
                            )
                    )

                    .background(

                        color =
                            if (pedalOn) {

                                Color(
                                    0xFF185C2B
                                )

                            } else {

                                Color(
                                    0xFF303030
                                )
                            },

                        shape =
                            RoundedCornerShape(
                                5.dp
                            )
                    )

                    .pointerInput(
                        pedalOn
                    ) {

                        detectTapGestures {

                            pedalOn =
                                !pedalOn
                        }
                    },

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        if (pedalOn) {
                            "PEDAL ON"
                        } else {
                            "PEDAL OFF"
                        },

                    color =
                        if (pedalOn) {

                            Color(
                                0xFF80FF80
                            )

                        } else {

                            Color.White
                        },

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )


            // ==================================================
            // VISIBLE KEYS
            // ==================================================

            Text(
                text =
                    "Keyboard Visible Keys : $visibleKeys",

                color =
                    Color.White,

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )


            // ==================================================
            // MINUS
            // ==================================================

            Box(

                modifier = Modifier
                    .size(44.dp)

                    .border(
                        width = 1.dp,
                        color = Color(0xFF777777),
                        shape =
                            RoundedCornerShape(
                                3.dp
                            )
                    )

                    .background(
                        color =
                            Color(0xFF303030),

                        shape =
                            RoundedCornerShape(
                                3.dp
                            )
                    )

                    .pointerInput(
                        Unit
                    ) {

                        detectTapGestures {

                            if (
                                visibleKeys > 5
                            ) {

                                visibleKeys--
                            }
                        }
                    },

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        "−",

                    color =
                        if (visibleKeys > 5) {

                            Color.White

                        } else {

                            Color(0xFF777777)
                        },

                    fontSize =
                        28.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.width(2.dp)
            )


            // ==================================================
            // PLUS
            // ==================================================

            Box(

                modifier = Modifier
                    .size(44.dp)

                    .border(
                        width = 1.dp,
                        color = Color(0xFF777777),
                        shape =
                            RoundedCornerShape(
                                3.dp
                            )
                    )

                    .background(
                        color =
                            Color(0xFF303030),

                        shape =
                            RoundedCornerShape(
                                3.dp
                            )
                    )

                    .pointerInput(
                        Unit
                    ) {

                        detectTapGestures {

                            if (
                                visibleKeys < 16
                            ) {

                                visibleKeys++
                            }
                        }
                    },

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        "+",

                    color =
                        if (visibleKeys < 16) {

                            Color.White

                        } else {

                            Color(0xFF777777)
                        },

                    fontSize =
                        27.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )
        }


        // ======================================================
        // PIANO
        // ======================================================

        PianoKeyboard(
            notes =
                notes,

            whiteKeyWidth =
                whiteKeyWidth,

            onNotePressed =
                onNotePressed,

            onNoteReleased =
                onNoteReleased,

            pedalOn =
                pedalOn
        )
    }
}


// =================================================================
// PIANO KEYBOARD
// =================================================================

@Composable
fun PianoKeyboard(
    notes: List<String>,
    whiteKeyWidth: Dp,
    onNotePressed: (String) -> Unit,
    onNoteReleased: (
        String,
        Boolean
    ) -> Unit,
    pedalOn: Boolean
) {

    val scrollState =
        rememberScrollState()


    val whiteKeyHeight =
        460.dp

    val blackKeyHeight =
        280.dp


    val totalWidth =
        whiteKeyWidth *
                notes.size


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                whiteKeyHeight
            )
            .horizontalScroll(
                scrollState
            )
            .background(
                Color.Black
            )
    ) {


        // ======================================================
        // WHITE KEYS
        // ======================================================

        Row(
            modifier = Modifier
                .width(
                    totalWidth
                )
                .height(
                    whiteKeyHeight
                )
        ) {

            notes.forEach { note ->

                WhitePianoKey(
                    note =
                        note,

                    width =
                        whiteKeyWidth,

                    height =
                        whiteKeyHeight,

                    onNotePressed =
                        onNotePressed,

                    onNoteReleased =
                        onNoteReleased,

                    pedalOn =
                        pedalOn
                )
            }
        }


        // ======================================================
        // BLACK KEYS
        // ======================================================

        notes.forEachIndexed {
                index,
                note ->

            val letter =
                note.first()

            val octave =
                note.drop(1)


            val blackNote =
                when (letter) {

                    'C' ->
                        "C#$octave"

                    'D' ->
                        "D#$octave"

                    'F' ->
                        "F#$octave"

                    'G' ->
                        "G#$octave"

                    'A' ->
                        "A#$octave"

                    else ->
                        null
                }


            if (
                blackNote != null &&
                index < notes.lastIndex
            ) {

                BlackPianoKey(
                    position =
                        index,

                    width =
                        whiteKeyWidth,

                    height =
                        blackKeyHeight,

                    note =
                        blackNote,

                    onNotePressed =
                        onNotePressed,

                    onNoteReleased =
                        onNoteReleased,

                    pedalOn =
                        pedalOn
                )
            }
        }
    }
}


// =================================================================
// WHITE PIANO KEY
// =================================================================

@Composable
fun WhitePianoKey(
    note: String,
    width: Dp,
    height: Dp,
    onNotePressed: (String) -> Unit,
    onNoteReleased: (
        String,
        Boolean
    ) -> Unit,
    pedalOn: Boolean
) {

    var isPressed by remember {
        mutableStateOf(false)
    }


    Box(

        modifier = Modifier
            .width(width)
            .height(height)

            .offset(

                y =
                    if (isPressed) {

                        8.dp

                    } else {

                        0.dp
                    }
            )

            .background(

                color =
                    if (isPressed) {

                        Color(0xFFD8D8D8)

                    } else {

                        Color.White
                    }
            )

            .border(
                width = 1.dp,
                color = Color.Black
            )

            .pointerInput(
                note,
                pedalOn
            ) {

                detectTapGestures(

                    onPress = {

                        // ======================================
                        // KEY DOWN
                        // ======================================

                        isPressed =
                            true


                        onNotePressed(
                            note
                        )


                        try {

                            tryAwaitRelease()

                        } finally {

                            // ==================================
                            // KEY UP
                            // ==================================

                            isPressed =
                                false


                            onNoteReleased(
                                note,
                                pedalOn
                            )
                        }
                    }
                )
            },

        contentAlignment =
            Alignment.BottomCenter
    ) {


        // ======================================================
        // NOTE LABEL
        // ======================================================

        Box(

            modifier = Modifier
                .padding(
                    bottom = 14.dp
                )

                .background(
                    color =
                        noteColor(note),

                    shape =
                        RoundedCornerShape(
                            3.dp
                        )
                )

                .padding(
                    horizontal = 8.dp,
                    vertical = 5.dp
                )
        ) {

            Text(
                text =
                    note,

                color =
                    Color.Black,

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}


// =================================================================
// BLACK PIANO KEY
// =================================================================

@Composable
fun BlackPianoKey(
    position: Int,
    width: Dp,
    height: Dp,
    note: String,
    onNotePressed: (String) -> Unit,
    onNoteReleased: (
        String,
        Boolean
    ) -> Unit,
    pedalOn: Boolean
) {

    var isPressed by remember {
        mutableStateOf(false)
    }


    Box(

        modifier = Modifier

            // ==================================================
            // POSITION
            // ==================================================

            .offset(

                x =
                    width * position +
                            width * 0.62f,

                y =
                    if (isPressed) {

                        8.dp

                    } else {

                        0.dp
                    }
            )

            // ==================================================
            // SIZE
            // ==================================================

            .width(
                width * 0.50f
            )

            .height(
                height
            )

            // ==================================================
            // SHADOW
            // ==================================================

            .shadow(

                elevation =
                    if (isPressed) {

                        2.dp

                    } else {

                        8.dp
                    },

                shape =
                    RoundedCornerShape(
                        bottomStart = 5.dp,
                        bottomEnd = 5.dp
                    )
            )

            // ==================================================
            // COLOR
            // ==================================================

            .background(

                color =
                    if (isPressed) {

                        Color(0xFF555555)

                    } else {

                        Color(0xFF151515)
                    },

                shape =
                    RoundedCornerShape(
                        bottomStart = 5.dp,
                        bottomEnd = 5.dp
                    )
            )

            // ==================================================
            // BORDER
            // ==================================================

            .border(

                width = 1.dp,

                color =
                    if (isPressed) {

                        Color(0xFF888888)

                    } else {

                        Color(0xFF444444)
                    },

                shape =
                    RoundedCornerShape(
                        bottomStart = 5.dp,
                        bottomEnd = 5.dp
                    )
            )

            // ==================================================
            // TOUCH
            // ==================================================

            .pointerInput(
                note,
                pedalOn
            ) {

                detectTapGestures(

                    onPress = {

                        // ======================================
                        // KEY DOWN
                        // ======================================

                        isPressed =
                            true


                        onNotePressed(
                            note
                        )


                        try {

                            tryAwaitRelease()

                        } finally {

                            // ==================================
                            // KEY UP
                            // ==================================

                            isPressed =
                                false


                            onNoteReleased(
                                note,
                                pedalOn
                            )
                        }
                    }
                )
            }
    )
}


// =================================================================
// NOTE COLORS
// =================================================================

fun noteColor(
    note: String
): Color {

    return when (
        note.last()
    ) {

        '1' ->
            Color(0xFFBE7D29)

        '2' ->
            Color(0xFFFFC700)

        '3' ->
            Color(0xFFDEFF01)

        '4' ->
            Color(0xFFC9F7C5)

        '5' ->
            Color(0xFFB9E8F0)

        '6' ->
            Color(0xFFB9D5F0)

        '7' ->
            Color(0xFFE4C5EA)

        else ->
            Color.LightGray
    }
}


// =================================================================
// PREVIEWS
// =================================================================

@Preview(
    showBackground = true,
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun KeyboardScreenPreview() {
    ProBANDTheme {
        KeyboardScreen(
            onBack = {},
            onNotePressed = {},
            onNoteReleased = { _, _ -> }
        )
    }
}
