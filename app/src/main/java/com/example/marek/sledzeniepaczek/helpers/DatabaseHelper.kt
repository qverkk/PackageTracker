package com.example.marek.sledzeniepaczek.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.marek.sledzeniepaczek.data.Packages

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {

    companion object {
        val TABLE_NAME: String = "package_table"
        val COL1: String = "ID"
        val COL2: String = "courier"
        val COL3: String = "package_numer"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL1 INTEGER PRIMARY KEY AUTOINCREMENT, $COL2 TEXT, $COL3 TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP IF TABLE EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(courier: String, packageNumber: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL2, courier)
        contentValues.put(COL3, packageNumber)

        Log.d("[DatabaseHelper]", "addData: Adding $courier, $packageNumber")

        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun getData(): Cursor {
        val db = writableDatabase
        val data = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        return data
    }

    fun deleteData(packages: Packages): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_NAME,
            "courier=? and package_numer=?",
            arrayOf(packages.courier.courierName, packages.packageNumber)
        )
        return result != 0
    }
}