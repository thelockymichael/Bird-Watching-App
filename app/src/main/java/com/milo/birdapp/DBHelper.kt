package com.milo.birdapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_RARITY INT, $COLUMN_NOTES TEXT, $COLUMN_DATE DATETIME)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(name: String, rarity: String, notes: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_RARITY, rarity)
        values.put(COLUMN_NOTES, notes)
        values.put(COLUMN_DATE, getDateTime())

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateRow(row_id: String, name: String, rarity: String, notes: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_RARITY, rarity)
        values.put(COLUMN_NOTES, notes)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun deleteRow(row_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun getAllRow(orderBy: String): Cursor? {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME $orderBy", null)
    }

    private fun getDateTime(): String? {

        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss", Locale.getDefault()
        )
        val date = Date()
        return dateFormat.format(date)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "myDBfile.db"
        const val TABLE_NAME = "birds"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_RARITY = "rarity"
        const val COLUMN_NOTES = "notes"
        const val COLUMN_DATE = "date"
        //const val COLUMN_CONTENT = "content"
    }
}