package com.merseyside.kmpMerseyLib.domain

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

object DBHelper {

    fun getDriver(context: Context, sqlDriverSchema: SqlDriver.Schema, dbName: String): SqlDriver {
        val helper = SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(dbName)
            .callback(object : SupportSQLiteOpenHelper.Callback(1) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val driver = AndroidSqliteDriver(db)
                    sqlDriverSchema.create(driver)
                }

                override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}

            })
            .build()

        val sqlHelper = FrameworkSQLiteOpenHelperFactory().create(helper)

        return AndroidSqliteDriver(sqlHelper)
    }
}