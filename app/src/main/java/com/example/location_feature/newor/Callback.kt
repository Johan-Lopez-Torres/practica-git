package com.example.location_feature.newor



interface Callback<T> {
    fun onSuccess(result: T)
    fun onError(e: Exception)
}
