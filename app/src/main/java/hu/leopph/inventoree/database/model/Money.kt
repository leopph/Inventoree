package hu.leopph.inventoree.database.model

import java.util.*

class Money(
    var unit: Currency,
    value: Float
) {
    var value: Float = value
        set(value) {
            field = if (value >= 0.0f) value else 0.0f
        }
}