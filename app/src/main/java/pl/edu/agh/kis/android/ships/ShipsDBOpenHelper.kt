package pl.edu.agh.kis.android.ships

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pl.edu.agh.kis.android.ships.components.Score

class ShipsDBOpenHelper(context: Context,
                        factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_username
                + " TEXT," +
                COLUMN_NAME_scoreValue
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addScore(score: Score) {
        val values = ContentValues()
        values.put(COLUMN_NAME_username, score.userName)
        values.put(COLUMN_NAME_scoreValue, score.scoreValue)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllScore(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    companion object {
        private val DATABASE_VERSION = 4
        private val DATABASE_NAME = "userscore.db"
        val TABLE_NAME = "userscore"
        val COLUMN_ID = "_id"
        val COLUMN_NAME_username = "username"
        val COLUMN_NAME_scoreValue = "scoreValue"
    }
}