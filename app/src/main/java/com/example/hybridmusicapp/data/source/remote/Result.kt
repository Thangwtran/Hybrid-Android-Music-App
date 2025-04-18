package com.example.hybridmusicapp.data.source.remote

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Failure<T>(val exception: Exception?) : Result<T>()
}