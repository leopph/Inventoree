package hu.leopph.inventoree.database.model


import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp


class Product(
    var id: String = "",
    var description: String = "",
    var isBundle: Boolean = false,
    var name: String = "",
    var orderDate: Timestamp = Timestamp.now(),
    var productSerialNumber: String = "",
    var startDate: Timestamp = Timestamp.now(),
    var terminationDate: Timestamp = Timestamp.now(),
    var status: ProductStatusType = ProductStatusType.created,
    var price: Price = Price()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean(),
        parcel.readString()!!,
        parcel.readParcelable(Timestamp::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readParcelable(Timestamp::class.java.classLoader)!!,
        parcel.readParcelable(Timestamp::class.java.classLoader)!!,
        ProductStatusType.values()[parcel.readInt()],
        parcel.readParcelable(Price::class.java.classLoader)!!)


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeBoolean(isBundle)
        parcel.writeString(name)
        parcel.writeParcelable(orderDate, 0)
        parcel.writeString(productSerialNumber)
        parcel.writeParcelable(startDate, 0)
        parcel.writeParcelable(terminationDate, 0)
        parcel.writeInt(status.ordinal)
        parcel.writeParcelable(price, 0)
    }


    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}