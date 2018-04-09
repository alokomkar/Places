package com.alokomkar.porter.network

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Alok on 09/04/18.
 */
class Address_components(
        var long_name: String? = null,
        var types: Array<String>? = null,
        var short_name: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.createStringArray(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(long_name)
        writeStringArray(types)
        writeString(short_name)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Address_components> = object : Parcelable.Creator<Address_components> {
            override fun createFromParcel(source: Parcel): Address_components = Address_components(source)
            override fun newArray(size: Int): Array<Address_components?> = arrayOfNulls(size)
        }
    }
}