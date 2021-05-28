package hu.leopph.inventoree.database.model

import android.os.Parcel
import android.os.Parcelable

class Price(
    var taxRate : Float = 0f,
    var dutyFreeAmount: Money = Money(),
    var taxIncludedAmount: Money = Money()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readParcelable(Money::class.java.classLoader)!!,
        parcel.readParcelable(Money::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(taxRate)
        parcel.writeParcelable(dutyFreeAmount, flags)
        parcel.writeParcelable(taxIncludedAmount, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Price> {
        override fun createFromParcel(parcel: Parcel): Price {
            return Price(parcel)
        }

        override fun newArray(size: Int): Array<Price?> {
            return arrayOfNulls(size)
        }
    }
}