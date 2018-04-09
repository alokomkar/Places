package com.alokomkar.porter.network

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Alok on 09/04/18.
 */
class Location(var lng: Double = 0.0,
               var lat: Double = 0.0) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(lng)
        writeDouble(lat)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Location> = object : Parcelable.Creator<Location> {
            override fun createFromParcel(source: Parcel): Location = Location(source)
            override fun newArray(size: Int): Array<Location?> = arrayOfNulls(size)
        }
    }
}