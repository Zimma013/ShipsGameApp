package pl.edu.agh.kis.android.ships

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_score.*

class ScoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_score)
        tvDisplayName.text = ""
        val dbHandler = ShipsDBOpenHelper(this, null)
        val cursor = dbHandler.getAllScore()
        cursor!!.moveToFirst()
        if(cursor.count != 0) {
            tvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_username))))
            tvDisplayName.append(" ")
            tvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_scoreValue))))
            tvDisplayName.append("\n")
            while (cursor.moveToNext()) {
                tvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_username))))
                tvDisplayName.append(" ")
                tvDisplayName.append((cursor.getString(cursor.getColumnIndex(ShipsDBOpenHelper.COLUMN_NAME_scoreValue))))
                tvDisplayName.append("\n")
            }
            cursor.close()
        }
    }
}