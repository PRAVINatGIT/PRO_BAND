package com.example.proband

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proband.ui.theme.ProBANDTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ProBandScreen(

                // ================================================
                // KEYBOARD
                // ================================================

                onKeyboardClick = {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            KeyboardActivity::class.java
                        )
                    )
                },

                // ================================================
                // GUITAR
                // ================================================

                onGuitarClick = {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            GuitarActivity::class.java
                        )
                    )
                },

                // ================================================
                // DRUM KIT
                // ================================================

                onDrumClick = {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            DrumActivity::class.java
                        )
                    )
                },

                // ================================================
                // ABOUT
                // ================================================

                onAboutClick = {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            AboutActivity::class.java
                        )
                    )
                }
            )
        }
    }
}


// ============================================================
// PROBAND HOME SCREEN
// ============================================================

@Composable
fun ProBandScreen(
    onKeyboardClick: () -> Unit,
    onGuitarClick: () -> Unit,
    onDrumClick: () -> Unit,
    onAboutClick: () -> Unit
) {

    var showSoundControls by remember {
        mutableStateOf(false)
    }

    var showMoreMenu by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ====================================================
        // MAIN HOME SCREEN
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFF07577D)
                )
        ) {

            // ==================================================
            // TOP BAR
            // ==================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        Color(0xFF176B91)
                    )
                    .padding(
                        horizontal = 18.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // ==================================================
                // SOUND CONTROL BUTTON
                // ==================================================

                IconButton(
                    onClick = {
                        showSoundControls = true
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Menu,

                        contentDescription =
                            "Sound Controls",

                        tint =
                            Color.White,

                        modifier =
                            Modifier.size(30.dp)
                    )
                }


                Spacer(
                    modifier =
                        Modifier.width(18.dp)
                )


                // ==================================================
                // APP NAME
                // ==================================================

                Text(
                    text =
                        "ProBAND",

                    color =
                        Color.White,

                    fontSize =
                        28.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )


                // ==================================================
                // MORE BUTTON
                // ==================================================

                Box {

                    IconButton(
                        onClick = {
                            showMoreMenu = !showMoreMenu
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.MoreVert,

                            contentDescription =
                                "More",

                            tint =
                                Color.White,

                            modifier =
                                Modifier.size(30.dp)
                        )
                    }


                    // ==================================================
                    // ABOUT MENU
                    // ==================================================

                    DropdownMenu(
                        expanded =
                            showMoreMenu,

                        onDismissRequest = {
                            showMoreMenu = false
                        }
                    ) {

                        DropdownMenuItem(

                            text = {
                                Text(
                                    text =
                                        "About ProBAND"
                                )
                            },

                            onClick = {

                                showMoreMenu = false

                                onAboutClick()
                            }
                        )
                    }
                }
            }


            // ==================================================
            // INSTRUMENT CARDS
            // ==================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(30.dp),

                horizontalArrangement =
                    Arrangement.SpaceEvenly,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // ==============================================
                // KEYBOARD
                // ==============================================

                InstrumentCard(
                    imageRes =
                        R.drawable.piano,

                    name =
                        "Keyboard",

                    onClick =
                        onKeyboardClick
                )


                // ==============================================
                // GUITAR
                // ==============================================

                InstrumentCard(
                    imageRes =
                        R.drawable.guitar,

                    name =
                        "Guitar",

                    onClick =
                        onGuitarClick
                )


                // ==============================================
                // DRUM KIT
                // ==============================================

                InstrumentCard(
                    imageRes =
                        R.drawable.drumset,

                    name =
                        "Drum Kit",

                    onClick =
                        onDrumClick
                )
            }
        }


        // ====================================================
        // SOUND CONTROL OVERLAY
        // ====================================================

        if (showSoundControls) {

            // ==================================================
            // DARK BACKGROUND
            // ==================================================

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(
                            alpha = 0.45f
                        )
                    )
                    .clickable {
                        showSoundControls = false
                    }
            )


            // ==================================================
            // SOUND CONTROL PANEL
            // ==================================================

            SoundControlPanel(
                onClose = {
                    showSoundControls = false
                },

                modifier = Modifier
                    .width(370.dp)
                    .fillMaxSize()
                    .align(
                        Alignment.CenterStart
                    )
            )
        }
    }
}


// ============================================================
// SOUND CONTROL PANEL
// ============================================================

@Composable
fun SoundControlPanel(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {

    // ========================================================
    // CURRENT VOLUME VALUES
    // ========================================================

    var masterVolume by remember {
        mutableStateOf(
            SoundSettings.masterVolume
        )
    }

    var keyboardVolume by remember {
        mutableStateOf(
            SoundSettings.keyboardVolume
        )
    }

    var guitarVolume by remember {
        mutableStateOf(
            SoundSettings.guitarVolume
        )
    }

    var drumVolume by remember {
        mutableStateOf(
            SoundSettings.drumVolume
        )
    }


    Column(
        modifier = modifier
            .background(
                Color(0xFF102C3C)
            )
            .padding(22.dp)
    ) {

        // ====================================================
        // HEADER
        // ====================================================

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "Sound Controls",

                color =
                    Color.White,

                fontSize =
                    23.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.weight(1f)
            )


            // ==================================================
            // CLOSE BUTTON
            // ==================================================

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        Color(0xFF184A60),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onClose()
                    },

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        "×",

                    color =
                        Color.White,

                    fontSize =
                        28.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // ====================================================
        // MASTER VOLUME
        // ====================================================

        VolumeControl(
            title =
                "Master Volume",

            value =
                masterVolume,

            onValueChange = {

                masterVolume =
                    it

                SoundSettings.masterVolume =
                    it
            }
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // ====================================================
        // KEYBOARD VOLUME
        // ====================================================

        VolumeControl(
            title =
                "Keyboard Volume",

            value =
                keyboardVolume,

            onValueChange = {

                keyboardVolume =
                    it

                SoundSettings.keyboardVolume =
                    it
            }
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // ====================================================
        // GUITAR VOLUME
        // ====================================================

        VolumeControl(
            title =
                "Guitar Volume",

            value =
                guitarVolume,

            onValueChange = {

                guitarVolume =
                    it

                SoundSettings.guitarVolume =
                    it
            }
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // ====================================================
        // DRUM KIT VOLUME
        // ====================================================

        VolumeControl(
            title =
                "Drum Kit Volume",

            value =
                drumVolume,

            onValueChange = {

                drumVolume =
                    it

                SoundSettings.drumVolume =
                    it
            }
        )


        Spacer(
            modifier =
                Modifier.height(30.dp)
        )


        // ====================================================
        // INFORMATION
        // ====================================================

        Text(
            text =
                "Master Volume controls the overall volume.\n" +
                        "Individual sliders control each instrument.",

            color =
                Color(0xFFB9E8F7),

            fontSize =
                13.sp,

            lineHeight =
                20.sp
        )
    }
}


// ============================================================
// VOLUME CONTROL
// ============================================================

@Composable
fun VolumeControl(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    title,

                color =
                    Color.White,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.weight(1f)
            )


            Text(
                text =
                    "${(value * 100).toInt()}%",

                color =
                    Color(0xFF9DDEEF),

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }


        Slider(
            value =
                value,

            onValueChange =
                onValueChange,

            valueRange =
                0f..1f,

            modifier =
                Modifier.fillMaxWidth()
        )
    }
}


// ============================================================
// INSTRUMENT CARD
// ============================================================

@Composable
fun InstrumentCard(
    imageRes: Int,
    name: String,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .size(220.dp)
            .clickable {
                onClick()
            },

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        // ==================================================
        // IMAGE
        // ==================================================

        Box(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color =
                        Color(0xFF0A668F),

                    shape =
                        RoundedCornerShape(20.dp)
                )
                .padding(15.dp),

            contentAlignment =
                Alignment.Center
        ) {

            Image(
                painter =
                    painterResource(
                        id = imageRes
                    ),

                contentDescription =
                    name,

                modifier =
                    Modifier.fillMaxSize(),

                contentScale =
                    ContentScale.Fit
            )
        }


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        // ==================================================
        // NAME
        // ==================================================

        Text(
            text =
                name,

            color =
                Color.White,

            fontSize =
                22.sp,

            fontWeight =
                FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
fun ProBandScreenPreview() {
    ProBANDTheme {
        ProBandScreen(
            onKeyboardClick = {},
            onGuitarClick = {},
            onDrumClick = {},
            onAboutClick = {}
        )
    }
}
