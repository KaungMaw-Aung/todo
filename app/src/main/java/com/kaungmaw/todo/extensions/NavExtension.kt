package com.kaungmaw.todo.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun <T> NavController.getNavResult(key: String): LiveData<T>? {
    return currentBackStackEntry?.savedStateHandle?.getLiveData(key)
}

fun <T> NavController.setNavResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> Fragment.getNavResult(key: String): LiveData<T>? {
    return this.findNavController().getNavResult(key)
}

fun <T> Fragment.setNavResult(key: String, value: T) {
    this.findNavController().setNavResult(key, value)
}

fun Fragment.clearResult(key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, null)
}