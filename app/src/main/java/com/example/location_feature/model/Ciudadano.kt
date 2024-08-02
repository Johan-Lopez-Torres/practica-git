package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable

import java.util.Date

data class Ciudadano(
    var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var correo: String = "",
    var clave: String = "",
    var creado_en: Date = Date(),
    var actualizado_en: Date = Date()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        nombres = parcel.readString() ?: "",
        apellidos = parcel.readString() ?: "",
        correo = parcel.readString() ?: "",
        clave = parcel.readString() ?: "",
        creado_en = Date(parcel.readLong()),
        actualizado_en = Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombres)
        parcel.writeString(apellidos)
        parcel.writeString(correo)
        parcel.writeString(clave)
        parcel.writeLong(creado_en.time)
        parcel.writeLong(actualizado_en.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ciudadano> {
        override fun createFromParcel(parcel: Parcel): Ciudadano {
            return Ciudadano(parcel)
        }

        override fun newArray(size: Int): Array<Ciudadano?> {
            return arrayOfNulls(size)
        }
    }
}
