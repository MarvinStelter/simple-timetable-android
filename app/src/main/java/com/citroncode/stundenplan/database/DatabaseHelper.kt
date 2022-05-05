package com.citroncode.stundenplan.database


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.citroncode.stundenplan.items.DatabaseItem

class DatabaseHelper(var activity: Activity) {
    var db: SQLiteDatabase? = null
    private lateinit var databaseHelper : DatabaseHelper

    fun iniDatabase() {
        db = activity.openOrCreateDatabase("database", 0, null)
        with(db) { this?.execSQL("CREATE TABLE IF NOT EXISTS timetable(id INTEGER PRIMARY KEY AUTOINCREMENT, subject VARCHAR, teacher VARCHAR, room VARCHAR, color VARCHAR, hour INTEGER, day INTEGER);") }
    }

    fun update(subject: String?, teacher: String?, room: String?, color: String?,hour: String?,day: String?) {
        val contentValues = ContentValues()
        contentValues.put("subject", subject)
        contentValues.put("teacher", teacher)
        contentValues.put("room", room)
        contentValues.put("color", color)
        contentValues.put("hour", hour)
        contentValues.put("day", day)
        db!!.update("timetable", contentValues, "hour = ? AND day = ?", arrayOf(hour,day))
    }
    fun checkIfExists(hour: Int, day: Int) : Boolean{
        var cursor: Cursor? = null
        val sql = "SELECT * FROM timetable WHERE hour=" + hour + " AND day=" + day
        cursor = db!!.rawQuery(sql, null)
        return cursor.count > 0
        cursor.close()
    }



    fun insert(subject: String?, teacher: String?, room: String?, color: String?,hour: String?,day: String?) {
        try {
            val contentValues = ContentValues()
            contentValues.put("subject", subject)
            contentValues.put("teacher", teacher)
            contentValues.put("room", room)
            contentValues.put("color", color)
            contentValues.put("hour", hour)
            contentValues.put("day", day)
            db!!.insert("timetable", null, contentValues)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }
    val list: ArrayList<DatabaseItem>
        @SuppressLint("Range")
        get() {
            val names = ArrayList<DatabaseItem>()
            try {
                val c = db!!.rawQuery("Select * from timetable", null)
                c.moveToFirst()
                do {
                    val item = DatabaseItem()
                    item.uid = c.getInt(c.getColumnIndex("id"))
                    item.subject = c.getString(c.getColumnIndex("subject"))
                    item.teacher = c.getString(c.getColumnIndex("teacher"))
                    item.room = c.getString(c.getColumnIndex("room"))
                    item.color = c.getString(c.getColumnIndex("color"))
                    item.hour = c.getInt(c.getColumnIndex("hour"))
                    item.day = c.getInt(c.getColumnIndex("day"))
                    names.add(item)
                } while (c.moveToNext())
            } catch (e: Exception) {
                //TODO Catch errors
            }
            return names
        }
        fun clearTimetable(){
            db?.execSQL("delete from timetable")
        }
}