package com.merseyside.utils.concurency

import kotlinx.coroutines.sync.Mutex

class LockerImpl(override val mutex: Mutex = Mutex()) : Locker