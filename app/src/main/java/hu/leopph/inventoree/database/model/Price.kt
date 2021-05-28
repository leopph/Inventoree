package hu.leopph.inventoree.database.model

class Price(
    var taxRate : Float,
    var dutyFreeAmount: Money,
    var taxIncludedAmount: Money
)