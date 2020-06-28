package com.merseyside.archy.domain.executor

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler?
}