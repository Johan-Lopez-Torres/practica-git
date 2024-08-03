package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Horario(
    var id: String = "",
    var horaInicio: Date = Date(),
    var horaFin: Date = Date(),
    var idRuta: String = "",
    var idZona:String = "",
    var dias: String = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        horaInicio = Date(parcel.readLong()),
        horaFin = Date(parcel.readLong()),
        idRuta = parcel.readString() ?: "",
        idZona = parcel.readString() ?: "",
        dias = parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(horaInicio.time)
        parcel.writeLong(horaFin.time)
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