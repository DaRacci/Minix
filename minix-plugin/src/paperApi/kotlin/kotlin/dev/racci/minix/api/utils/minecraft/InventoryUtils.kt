package dev.racci.minix.api.utils.minecraft

/**
 * Utilities for Inventories.
 */
public object InventoryUtils {

    /**
     * Convert 0-53 slot into a row and column pair.
     *
     * @param slot the slot
     * @return The pair
     */
    public fun convertSlotToRowColumn(slot: Int): Pair<Int, Int> {
        val row = Math.floorDiv(slot, 9)
        val column = slot - row * 9
        return row + 1 to column + 1
    }

    /**
     * Convert a row and column pair into a slot.
     *
     * @param row the row
     * @param column the column
     * @return The slot
     */
    public fun convertRowColumnToSlot(row: Int, column: Int): Int =
        (row.takeIf { row > 0 } ?: (row * 9)) + (column - 1)

    /**
     * Convert a row and column pair into a slot.
     *
     * @param pair the row and column pair
     * @return The slot
     * @see convertRowColumnToSlot
     */
    public fun convertRowColumnToSlot(pair: Pair<Int, Int>): Int =
        convertRowColumnToSlot(pair.first, pair.second)
}
