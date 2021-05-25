package hu.leopph.inventoree.database.model

enum class ProductStatusType {
    created,
    pendingActive,
    cancelled,
    active,
    pendingTerminate,
    terminated,
    suspended,
    aborted,
}