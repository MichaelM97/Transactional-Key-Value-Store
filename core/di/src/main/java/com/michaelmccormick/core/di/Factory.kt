package com.michaelmccormick.core.di

interface Factory<T> {
    fun create(): T
}
