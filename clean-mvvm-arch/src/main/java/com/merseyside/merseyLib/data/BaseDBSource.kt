package com.merseyside.merseyLib.data

import androidx.room.RoomDatabase

abstract class BaseDBSource<M: RoomDatabase>(val db: M) {

    protected val MB = (1024 * 1024).toLong()
}
