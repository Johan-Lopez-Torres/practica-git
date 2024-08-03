package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Horario(
    var id: String = "",
    var horarioInicio: Date = Date(),
    var horarioFin: Date = Date(),
    var idCamion: String = "",
    var idRuta: String = "",
    var idZona: String = "",
    var dias: String = "",


) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        horarioInicio= Date(parcel.readLong()),
        horarioFin = Date(parcel.readLong()),
        idCamion = parcel.readString() ?: "",
        idRuta = parcel.readString() ?: "",
        idZona = parcel.readString() ?: "",
        dias = parcel.readString() ?: "",

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(idCamion)
        parcel.writeString(idRuta)
        parcel.writeString(idZona)
        parcel.writeString(dias)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Horario> {
        override fun createFromParcel(parcel: Parcel): Horario {
            return Horario(parcel)
        }

        override fun newArray(size: Int): Array<Horario?> {
            return arrayOfNulls(size)
        }
    }
}
