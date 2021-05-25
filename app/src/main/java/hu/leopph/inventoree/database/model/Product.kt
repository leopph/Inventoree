package hu.leopph.inventoree.database.model

import com.google.firebase.Timestamp

class Product (
    val id: String,
    var description: String,
    var isBundle: Boolean,
    var name: String,
    var orderDate: Timestamp,
    var productSerialNumber: String,
    var startDate: Timestamp,
    var terminationDate: Timestamp,
    var status: ProductStatusType) {

}