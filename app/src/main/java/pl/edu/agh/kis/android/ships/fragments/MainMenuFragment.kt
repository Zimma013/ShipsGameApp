package pl.edu.agh.kis.android.ships.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

import pl.edu.agh.kis.android.ships.R
import pl.edu.agh.kis.android.ships.activities.MainActivity

class MainMenuFragment : Fragment() {

    public final val TAG = MainMenuFragment::class.java.simpleName

    lateinit var mainActivity: MainActivity

    lateinit var mBtnHelp: Button
    lateinit var mBtnPlay: Button
    lateinit var mBtnOptions: Button
    lateinit var mBtnScores: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        mainActivity = activity as MainActivity
        mBtnPlay = view.findViewById<Button>(R.id.playButton)
        mBtnPlay.setOnClickListener {
            mainActivity.playClick()
        }
        mBtnHelp = view.findViewById<Button>(R.id.helpButton)
        mBtnHelp.setOnClickListener {
            mainActivity.helpClick()
        }
        mBtnOptions = view.findViewById<Button>(R.id.optionsButton)
        mBtnOptions.setOnClickListener {
            mainActivity.optionsClick()
        }
        mBtnScores = view.findViewById<Button>(R.id.socresButton)
        mBtnScores.setOnClickListener {
            mainActivity.scoresClick()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}