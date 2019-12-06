package pl.edu.agh.kis.android.ships.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import pl.edu.agh.kis.android.ships.Options
import pl.edu.agh.kis.android.ships.R

class Audio {
    private var mp: MediaPlayer? = null

    private fun stop() {
        if (mp != null) {
            mp!!.release()
            mp = null
        }
    }

    fun playMiss(context: Context) {
        stop()
        if (Options.sound) {
            mp = MediaPlayer.create(context, R.raw.miss)
            mp!!.setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build())
            mp!!.setVolume(0f, 100f)
            mp!!.start()
        }
    }

    fun playHit(context: Context) {
        stop()
        if (Options.sound) {
            mp = MediaPlayer.create(context, R.raw.hit)
            mp!!.setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build())
            mp!!.setVolume(0f, 100f)
            mp!!.start()
        }

        if (Options.vibrations) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(500)
                }
            }
        }
    }
}