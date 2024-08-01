package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable

data class Conductor(
    var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var email: String = "",
    var clave: String = "",
    var camionid: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombres)
        parcel.writeString(apellidos)
        parcel.writeString(email)
        parcel.writeString(clave)
        parcel.writeString(camionid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Conductor> {
        override fun createFromParcel(parcel: Parcel): Conductor {
            return Conductor(parcel)
        }

        override fun newArray(size: Int): Array<Conductor?> {
            return arrayOfNulls(size)
        }
    }
}
