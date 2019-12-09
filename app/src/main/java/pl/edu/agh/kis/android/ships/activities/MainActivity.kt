package pl.edu.agh.kis.android.ships.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import pl.edu.agh.kis.android.ships.R
import pl.edu.agh.kis.android.ships.components.Score
import pl.edu.agh.kis.android.ships.fragments.HelpFragment
import pl.edu.agh.kis.android.ships.fragments.MainMenuFragment
import pl.edu.agh.kis.android.ships.fragments.OptionsFragment
import pl.edu.agh.kis.android.ships.fragments.ScoresFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fragment_main_menu)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        subscribeToFirebaseTopic()

        if(!resources.getBoolean(R.bool.twoPaneMode)){
            if (savedInstanceState != null) {
                // cleanup any existing fragments in case we are in detailed mode
                supportFragmentManager.executePendingTransactions()
                val fragmentById = supportFragmentManager.findFragmentById(R.id.main_menu_fragment_container)
                if (fragmentById != null) {
                    supportFragmentManager.beginTransaction().remove(fragmentById).commit()
                }
            }
            val mainMenuFragment = MainMenuFragment()
            supportFragmentManager.beginTransaction().replace(R.id.main_menu_fragment_container, mainMenuFragment)
                .commit()
        }
    }

    private fun subscribeToFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("ships-game-app")
        println("Subscribed to ships-game-app")
    }

    fun helpClick() {

        val newFragment = HelpFragment()
        val transaction = supportFragmentManager.beginTransaction()

        if(resources.getBoolean(R.bool.twoPaneMode)){
            val fragmentById = supportFragmentManager.findFragmentByTag(HelpFragment::TAG.toString())
            if (fragmentById == null) {
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.detail_right_fragment, newFragment)
                Log.i("FRAG","helpClick")
            }else{
                return
            }
        }else{
            transaction.replace(R.id.main_menu_fragment_container, newFragment)
        }

        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(HelpFragment::TAG.toString())

        // Commit the transaction
        transaction.commit()
    }

    fun playClick() {
        startActivity(Intent(this@MainActivity, GameActivity::class.java))
    }

    fun optionsClick() {
        val newFragment = OptionsFragment()
        val transaction = supportFragmentManager.beginTransaction()

        if(resources.getBoolean(R.bool.twoPaneMode)){
            val fragmentById = supportFragmentManager.findFragmentByTag(OptionsFragment::TAG.toString())
            if (fragmentById == null) {
                transaction.replace(R.id.detail_right_fragment, newFragment)
                Log.i("FRAG","optionsClick")
            }else{
                return
            }
        }else{
            transaction.replace(R.id.main_menu_fragment_container, newFragment)
        }
        transaction.addToBackStack(OptionsFragment::TAG.toString())
        transaction.commit()
    }
    fun scoresClick() {
        val newFragment = ScoresFragment()
        val transaction = supportFragmentManager.beginTransaction()

        if(resources.getBoolean(R.bool.twoPaneMode)){
            val fragmentById = supportFragmentManager.findFragmentByTag(ScoresFragment::TAG.toString())
            if (fragmentById == null) {
                transaction.replace(R.id.detail_right_fragment, newFragment)
                Log.i("FRAG","scoresClick")
            }else{
                return
            }
        }else{
            transaction.replace(R.id.main_menu_fragment_container, newFragment)
        }
        transaction.addToBackStack(ScoresFragment::TAG.toString())
        transaction.commit()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            Log.i("logger", "popping backstack")
            fm.popBackStack()
        } else {
            Log.i("logger", "nothing on backstack, calling super")
            super.onBackPressed()
        }
    }

}
