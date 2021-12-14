package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitMultipleValues(dataCount: Int = 1): List<T> {
    val data = mutableListOf<T>()
    val latch = CountDownLatch(dataCount)

    val observer = object : Observer<T> {
        override fun onChanged(o: T) {
            data.add(o)
            latch.countDown()
        }
    }

    this.observeForever(observer)

    if (!latch.await(5, TimeUnit.SECONDS)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData never gets its value")
    }

    latch.await()
    this.removeObserver(observer)

    return data.toList()
}