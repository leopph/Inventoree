package hu.leopph.inventoree.database.model

class Price(
    var taxRate : Float,
    dutyFreeAmount: Money,
    taxIncludedAmount: Money
)