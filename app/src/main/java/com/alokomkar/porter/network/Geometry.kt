package com.alokomkar.porter.network

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Alok on 09/04/18.
 */
class Geometry(
        var location: Location? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<Location>(Location::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(location, 0)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Geometry> = object : Parcelable.Creator<Geometry> {
            override fun createFromParcel(source: Parcel): Geometry = Geometry(source)
            override fun newArray(size: Int): Array<Geometry?> = arrayOfNulls(size)
        }
    }
}