package com.example.hybridmusicapp

interface ResultCallback<T> {
    fun onResult(result: T)
}