package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable

data class Ruta(
    var id: String = "",
    var nombre: String = "",
    var imagen: String = "",
    var descripcion: String = "",
    var zonid: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(imagen)
        parcel.writeString(descripcion)
        parcel.writeString(zonid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ruta> {
        override fun createFromParcel(parcel: Parcel): Ruta {
            return Ruta(parcel)
        }

        override fun newArray(size: Int): Array<Ruta?> {
            return arrayOfNulls(size)
        }
    }
}

/*
package com.example.prueba

class Ruta{
    var id: String = ""
    var nombre: String = ""
    var imagen: String = ""
    var descripcion: String = ""
    var zonid: String = ""
}


        val jsonArr = JSONArray("[\n" +
                "    {\n" +
                "        'id' : 'ruta1',\n" +
                "        'nombre' : 'Ruta Bellamar',\n" +
                "        'imagen' : '',\n" +
                "        'descripcion' : 'Cubre áreas residenciales y comerciales en Bellamar, asegurando recolección eficiente y puntual de residuos.',\n" +
                "        'zonid' : 'zona1'" +
                "    },\n" +
                "    {\n" +
                "        'id' : 'ruta2',\n" +
                "        'nombre' : 'Ruta Los Héroes',\n" +
                "        'imagen' : '',\n" +
                "        'descripcion' : 'Sirve al barrio de Los Héroes, recolectando residuos de hogares, parques y comercios de manera sistemática.',\n" +
                "        'zonid' : 'zona2'" +
                "    },\n" +
                "    {\n" +
                "        'id' : 'ruta3',\n" +
                "        'nombre' : 'Ruta Garatea',\n" +
                "        'imagen' : '',\n" +
                "        'descripcion' : 'Atiende áreas residenciales, industriales y comerciales en Garatea, garantizando recolección eficiente en toda la zona.',\n" +
                "        'zonid' : 'zona3'" +
                "    }\n" +


                "]"
        )

        val firebaseFirestore = FirebaseFirestore.getInstance()

        for (i in 0 until jsonArr.length()) {
            val aux = jsonArr.get(i) as JSONObject
            val ruta = Ruta().apply {
                id = aux.getString("id")
                nombre = aux.getString("nombre")
                imagen = aux.getString("imagen")
                zonid = aux.getString("zonid")
                descripcion = aux.getString("descripcion")

            }

            firebaseFirestore.collection("Rutas").document().set(ruta)
        }




 */