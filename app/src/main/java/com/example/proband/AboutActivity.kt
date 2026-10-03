package com.example.proband

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AboutProBandScreen(
                onBack = {
                    finish()
                }
            )
        }
    }
}


// ============================================================
// ABOUT PROBAND SCREEN
// ============================================================

@Composable
fun AboutProBandScreen(
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF07577D),
                        Color(0xFF0B3F58),
                        Color(0xFF082C3D)
                    )
                )
            )
    ) {

        // ====================================================
        // TOP BAR
        // ====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    Color(0xFF176B91)
                )
                .padding(horizontal = 12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // ==================================================
            // BACK BUTTON
            // ==================================================

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onBack()
                    },

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "‹",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "About ProBAND",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }


        // ====================================================
        // MAIN CONTENT
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // =================================================
            // MUSIC SYMBOL
            // =================================================

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(85.dp)
                    .background(
                        Color(0xFF1A82B0),
                        RoundedCornerShape(20.dp)
                    )
                    .border(
                        2.dp,
                        Color(0xFF65C8F0),
                        RoundedCornerShape(20.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "♪",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================
            // APP NAME
            // =================================================

            Text(
                text = "ProBAND",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(5.dp)
            )


            Text(
                text = "Virtual Musical Instruments",
                color = Color(0xFFB9E8F7),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // =================================================
            // ABOUT CARD
            // =================================================

            AboutCard(
                title = "About ProBAND"
            ) {

                Text(
                    text =
                        "ProBAND is an Android-based virtual musical " +
                                "instrument application designed to bring " +
                                "multiple musical instruments together in " +
                                "one simple and interactive application.",

                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // PURPOSE
            // =================================================

            AboutCard(
                title = "Purpose"
            ) {

                Text(
                    text =
                        "ProBAND provides a convenient way to explore, " +
                                "practice and play musical instruments using " +
                                "a smartphone. It is designed for learning, " +
                                "practice, entertainment and demonstrations.",

                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // AVAILABLE INSTRUMENTS
            // =================================================

            AboutCard(
                title = "Available Instruments"
            ) {

                InstrumentInfo(
                    title = "Keyboard / Piano",
                    description =
                        "Play piano notes using white and black keys with realistic piano sounds."
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                InstrumentInfo(
                    title = "Acoustic Guitar",
                    description =
                        "Play guitar strings, chords and strumming patterns with interactive controls."
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                InstrumentInfo(
                    title = "Drum Kit",
                    description =
                        "Play individual drums and cymbals with sound and visual animation."
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // KEY FEATURES
            // =================================================

            AboutCard(
                title = "Key Features"
            ) {

                FeatureText(
                    "• Touch-based musical instruments"
                )

                FeatureText(
                    "• Piano with white and black keys"
                )

                FeatureText(
                    "• Piano sustain pedal control"
                )

                FeatureText(
                    "• Guitar strings, chords and strumming"
                )

                FeatureText(
                    "• Individual drum and cymbal sounds"
                )

                FeatureText(
                    "• Interactive instrument animations"
                )

                FeatureText(
                    "• Landscape-oriented instrument interface"
                )

                FeatureText(
                    "• Simple and easy-to-use design"
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // UNIQUENESS
            // =================================================

            AboutCard(
                title = "What Makes ProBAND Different?"
            ) {

                Text(
                    text =
                        "ProBAND brings a keyboard, guitar and drum kit " +
                                "together in one application. Each instrument " +
                                "has its own interactive controls, sounds and " +
                                "visual feedback, providing a convenient " +
                                "multi-instrument experience on a smartphone.",

                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // USES
            // =================================================

            AboutCard(
                title = "Uses"
            ) {

                FeatureText(
                    "• Learning basic musical instruments"
                )

                FeatureText(
                    "• Practicing musical notes and chords"
                )

                FeatureText(
                    "• Casual music playing"
                )

                FeatureText(
                    "• Demonstrations and college projects"
                )

                FeatureText(
                    "• Exploring different instrument sounds"
                )
            }


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // =================================================
            // FOOTER
            // =================================================

            Text(
                text = "ProBAND",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Virtual Musical Instruments Android Application",
                color = Color(0xFFB9E8F7),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )
        }
    }
}


// ============================================================
// ABOUT CARD
// ============================================================

@Composable
fun AboutCard(
    title: String,
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF0A668F),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Color(0xFF3D9FC4),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        content()
    }
}


// ============================================================
// INSTRUMENT INFORMATION
// ============================================================

@Composable
fun InstrumentInfo(
    title: String,
    description: String
) {

    Column {

        Text(
            text = title,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = description,
            color = Color(0xFFD7F1FA),
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}


// ============================================================
// FEATURE TEXT
// ============================================================

@Composable
fun FeatureText(
    text: String
) {

    Text(
        text = text,
        color = Color.White,
        fontSize = 15.sp,
        lineHeight = 23.sp,
        modifier = Modifier.padding(
            vertical = 2.dp
        )
    )
}