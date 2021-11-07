package me.racci.raccicore.utils

object InventoryUtils {

    /**
     * Convert 0-53 slot into a row and column pair
     *
     * @param slot the slot
     * @return The pair
     */
    fun convertSlotToRowColumn(slot: Int): Pair<Int, Int> {
        val row = Math.floorDiv(slot, 9)
        val column = slot - row * 9
        return (row + 1) to (column + 1)
    }

}