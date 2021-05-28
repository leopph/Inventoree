package hu.leopph.inventoree.database.model


import android.os.Parcel
import android.os.Parcelable
import java.util.*


class Money(
    var unit: Currency = Currency.getInstance(Locale.getDefault()),
    value: Float = 0f
) : Parcelable {
    var value: Float = value
        set(value) {
            field = if (value >= 0.0f) value else 0.0f
        }

    constructor(parcel: Parcel) : this(
        Currency.getInstance(parcel.readString()),
        parcel.readFloat())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(unit.currencyCode)
        dest?.writeFloat(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Money> {
        override fun createFromParcel(parcel: Parcel): Money {
            return Money(parcel)
        }

        override fun newArray(size: Int): Array<Money?> {
            return arrayOfNulls(size)
        }
    }
}