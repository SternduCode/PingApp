package com.sterndu.pingonvolcano

import android.os.Parcel
import android.os.Parcelable

data class Message(val text : String, val type: Type) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        Type.valueOf(parcel.readString() as String)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }

}