package com.example.location_feature.network



interface Callback<T> {
    fun onSuccess(result: T)
    fun onError(e: Exception)
}
