package com.merseyside.kmpMerseyLib.domain

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

object DBHelper {

    fun getDriver(context: Context, sqlDriverSchema: SqlDriver.Schema, dbName: String): SqlDriver {

        val callback = object : SupportSQLiteOpenHelper.Callback(1) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                val driver = AndroidSqliteDriver(db)
                sqlDriverSchema.create(driver)
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
        }

        return AndroidSqliteDriver(
            schema = sqlDriverSchema,
            context = context,
            name = dbName,
            callback = callback
        )
    }
}