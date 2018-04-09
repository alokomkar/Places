package com.alokomkar.porter.network

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Alok on 09/04/18.
 */
class LocationResults(
        var place_id: String? = null,
        @SerializedName("address_components")
        var address_components: List<Address_components>? = null,
        var formatted_address: String? = null,
        var geometry: Geometry? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(Address_components.CREATOR),
            source.readString(),
            source.readParcelable<Geometry>(Geometry::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(place_id)
        writeTypedList(address_components)
        writeString(formatted_address)
        writeParcelable(geometry, 0)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LocationResults> = object : Parcelable.Creator<LocationResults> {
            override fun createFromParcel(source: Parcel): LocationResults = LocationResults(source)
            override fun newArray(size: Int): Array<LocationResults?> = arrayOfNulls(size)
        }
    }
}