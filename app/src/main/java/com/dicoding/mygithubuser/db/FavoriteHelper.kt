package com.dicoding.mygithubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.dicoding.mygithubuser.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dicoding.mygithubuser.db.DatabaseContract.FavoriteColumns.Companion.HTML
import com.dicoding.mygithubuser.db.DatabaseContract.FavoriteColumns.Companion.ID
import com.dicoding.mygithubuser.db.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.dicoding.mygithubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.mygithubuser.model.UserGithubResponse

class FavoriteHelper(context: Context) {

    private lateinit var database: SQLiteDatabase
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): ArrayList<UserGithubResponse> {
        val arrayList: ArrayList<UserGithubResponse> = ArrayList()
        val cursor: Cursor = database.query(
            DATABASE_TABLE, null,
            null,
            null,
            null,
            null,
            "$ID ASC",
            null
        )
        cursor.moveToFirst()
        var listFav: UserGithubResponse
        if (cursor.count > 0) {
            do {
                listFav = UserGithubResponse()
                listFav.id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                listFav.avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR))
                listFav.htmlUrl = cursor.getString(cursor.getColumnIndexOrThrow(HTML))
                listFav.login = cursor.getString(cursor.getColumnIndexOrThrow(LOGIN))
                arrayList.add(listFav)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insert(response: UserGithubResponse): Long {
        val values = ContentValues()
        values.put(ID, response.id)
        values.put(AVATAR, response.avatarUrl)
        values.put(HTML, response.htmlUrl)
        values.put(LOGIN, response.login)

        return database.insert(DATABASE_TABLE, null, values)
    }

    fun delete(username: String): Int {
        return database.delete(TABLE_NAME, "$LOGIN = '$username'", null)
    }
}