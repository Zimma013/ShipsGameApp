package pl.edu.agh.kis.android.ships.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_score.*
import pl.edu.agh.kis.android.ships.Options

import pl.edu.agh.kis.android.ships.R
import pl.edu.agh.kis.android.ships.ShipsDBOpenHelper

class ScoresFragment : Fragment() {

    public final val TAG = ScoresFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        val mTextViewtvDisplayName = view.findViewById<TextView>(R.id.tvDisplayName)

        val text = ""
        val dbHandler =
            ShipsDBOpenHelper(activity!!.applicationContext, null)
        val cursor = dbHandler.getAllScore()
        cursor!!.moveToFirst()
        if(cursor.count != 0) {
            mTextViewtvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_username))))
            mTextViewtvDisplayName.append(" ")
            mTextViewtvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_scoreValue))))
            mTextViewtvDisplayName.append("\n")
            while (cursor.moveToNext()) {
                mTextViewtvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_username))))
                mTextViewtvDisplayName.append(" ")
                mTextViewtvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_scoreValue))))
                mTextViewtvDisplayName.append("\n")
            }
            cursor.close()
        }
//        mTextViewtvDisplayName.text = text
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}