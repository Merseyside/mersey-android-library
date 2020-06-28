package com.merseyside.archy.di.qualifiers

import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass


@MapKey
annotation class WorkerKey(
    val value: KClass<out ListenableWorker>
)