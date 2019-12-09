package pl.edu.agh.kis.android.ships.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import pl.edu.agh.kis.android.ships.Options

import pl.edu.agh.kis.android.ships.R

class OptionsFragment : Fragment() {

    public final val TAG = OptionsFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_options, container, false)

        val soundCheckbox = view.findViewById<CheckBox>(R.id.soundCheckbox)
        soundCheckbox.setOnClickListener {
            Options.sound = !Options.sound
        }
        soundCheckbox.isChecked =
            Options.sound
        val vibrationCheckbox = view.findViewById<CheckBox>(R.id.vibrationCheckbox)
        vibrationCheckbox.setOnClickListener{
            Options.vibrations = !Options.vibrations
        }
        vibrationCheckbox.isChecked =
            Options.vibrations

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}