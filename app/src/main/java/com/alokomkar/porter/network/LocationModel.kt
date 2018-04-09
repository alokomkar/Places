package com.alokomkar.porter.network

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Alok on 09/04/18.
 */
class LocationModel(
        @SerializedName("results")
        var resultsList: List<LocationResults>,
        var status: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(LocationResults.CREATOR),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(resultsList)
        writeString(status)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LocationModel> = object : Parcelable.Creator<LocationModel> {
            override fun createFromParcel(source: Parcel): LocationModel = LocationModel(source)
            override fun newArray(size: Int): Array<LocationModel?> = arrayOfNulls(size)
        }
    }
}