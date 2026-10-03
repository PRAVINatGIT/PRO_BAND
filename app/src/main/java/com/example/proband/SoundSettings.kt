package com.example.proband

object SoundSettings {

    // ==========================================================
    // MASTER VOLUME
    // ==========================================================

    var masterVolume: Float = 1.0f


    // ==========================================================
    // KEYBOARD VOLUME
    // ==========================================================

    var keyboardVolume: Float = 1.0f


    // ==========================================================
    // GUITAR VOLUME
    // ==========================================================

    var guitarVolume: Float = 1.0f


    // ==========================================================
    // DRUM KIT VOLUME
    // ==========================================================

    var drumVolume: Float = 1.0f


    // ==========================================================
    // GET FINAL VOLUME
    // ==========================================================

    fun keyboardFinalVolume(): Float {
        return (
                masterVolume *
                        keyboardVolume
                ).coerceIn(0.0f, 1.0f)
    }

    fun guitarFinalVolume(): Float {
        return (
                masterVolume *
                        guitarVolume
                ).coerceIn(0.0f, 1.0f)
    }

    fun drumFinalVolume(): Float {
        return (
                masterVolume *
                        drumVolume
                ).coerceIn(0.0f, 1.0f)
    }
}