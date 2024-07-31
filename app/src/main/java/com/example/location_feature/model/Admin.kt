package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable

data class Admin(
    var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var email: String = "",
    var clave: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        nombres = parcel.readString().toString()
        apellidos = parcel.readString().toString()
        email = parcel.readString().toString()
        clave = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombres)
        parcel.writeString(apellidos)
        parcel.writeString(email)
        parcel.writeString(clave)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Admin> {
        override fun createFromParcel(parcel: Parcel): Admin {
            return Admin(parcel)
        }

        override fun newArray(size: Int): Array<Admin?> {
            return arrayOfNulls(size)
        }
    }
}