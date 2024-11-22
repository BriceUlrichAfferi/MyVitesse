package com.example.vitesse.adapters.room
/*
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "candidat_table")
data class Candidat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val picture: String = "", // Default empty string
    val identifiant: String,
    val description: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val anivDate: String,
    var isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Candidat> {
        override fun createFromParcel(parcel: Parcel): Candidat {
            return Candidat(parcel)
        }

        override fun newArray(size: Int): Array<Candidat?> {
            return arrayOfNulls(size)
        }
    }
}

*/