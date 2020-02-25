package com.jvr.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by OA-JomRafa on 25/02/2020.
 */

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $AGE TEXT, $USERNAME TEXT, $PASSWORD TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addUser(user: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, user.name)
        values.put(AGE, user.age)
        values.put(USERNAME, user.username)
        values.put(PASSWORD, user.password)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$success")
        return (Integer.parseInt("$success") != -1)
    }

    fun getUser(id: Int): User {
        val user = User()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        user.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        user.name = cursor.getString(cursor.getColumnIndex(NAME))
        user.age = cursor.getString(cursor.getColumnIndex(AGE))
        user.username = cursor.getString(cursor.getColumnIndex(USERNAME))
        user.password = cursor.getString(cursor.getColumnIndex(PASSWORD))
        cursor.close()
        return user
    }

    fun users(): List<User> {
        val userList = ArrayList<User>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val user = User()
                    user.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    user.name = cursor.getString(cursor.getColumnIndex(NAME))
                    user.age = cursor.getString(cursor.getColumnIndex(AGE))
                    user.username = cursor.getString(cursor.getColumnIndex(USERNAME))
                    user.password = cursor.getString(cursor.getColumnIndex(PASSWORD))
                    userList.add(user)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return userList
    }

    fun updateUser(user: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, user.name)
        values.put(AGE, user.age)
        values.put(USERNAME, user.username)
        values.put(PASSWORD, user.password)
        val success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(user.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteUser(id: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$ID=?", arrayOf(id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteAllUser(): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    companion object {
        private const val DB_VERSION = 2
        private const val DB_NAME = "SampleDB"
        private const val TABLE_NAME = "Users"

        //column name
        private const val ID = "Id"
        private const val NAME = "Name"
        private const val AGE = "Age"
        private const val USERNAME = "Username"
        private const val PASSWORD = "Password"
    }
}