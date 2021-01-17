package com.lentimosystems.swipevideos

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

data class VideoItem(
        var resourceLocation: String,
        var title: String,
        var description: String)
: Parcelable {
    constructor(p: Parcel): this(
            p.readString()!!,
            p.readString()!!,
            p.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resourceLocation)
        parcel.writeString(title)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<VideoItem> {
        override fun createFromParcel(parcel: Parcel): VideoItem {
            return VideoItem(parcel)
        }

        override fun newArray(size: Int): Array<VideoItem?> {
            return arrayOfNulls(size)
        }
    }
}
