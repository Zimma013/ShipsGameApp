package pl.edu.agh.kis.android.ships

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        subscribeToFirebaseTopic()
    }

    private fun subscribeToFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("ships-game-app")
        println("Subscribed to ships-game-app")
    }

    fun onHelpClick(v: View) {
        startActivity(Intent(this@MainActivity, HelpActivity::class.java))
    }

    fun onPlayClick(v: View) {
        startActivity(Intent(this@MainActivity, GameActivity::class.java))
    }

    fun onOptionsClick(v: View) {
        startActivity(Intent(this@MainActivity, OptionsActivity::class.java))
    }
    fun onScoresClick(v: View) {
        startActivity(Intent(this@MainActivity, ScoresActivity::class.java))
    }

}
