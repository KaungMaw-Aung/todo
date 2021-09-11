package com.kaungmaw.todo.util

sealed class NetworkCallResult<out R> {
    data class Success<T>(val data: T) : NetworkCallResult<T>()
    data class Error(val exception: Exception) : NetworkCallResult<Nothing>()
}