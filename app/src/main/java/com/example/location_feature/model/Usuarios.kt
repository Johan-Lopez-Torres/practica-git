package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable

data class Usuarios(
    var id : String = "",
    val email: String = "",
    val password: String = "",
    val role: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usuarios> {
        override fun createFromParcel(parcel: Parcel): Usuarios {
            return Usuarios(parcel)
        }

        override fun newArray(size: Int): Array<Usuarios?> {
            return arrayOfNulls(size)
        }
    }
}
