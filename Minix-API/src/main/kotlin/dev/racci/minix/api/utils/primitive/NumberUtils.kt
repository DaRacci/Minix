package dev.racci.minix.api.utils.primitive

import dev.racci.minix.api.utils.UtilObject
import java.util.TreeMap

object NumberUtils : UtilObject {

    private val map by lazy {
        object : TreeMap<Int, String>() {
            init {
                this[1000000] = "M"
                this[900000] = "CM"
                this[500000] = "D"
                this[100000] = "C"
                this[90000] = "XC"
                this[50000] = "L"
                this[10000] = "X"
                this[9000] = "MX"
                this[5000] = "v"
                this[1000] = "M"
                this[900] = "CM"
                this[500] = "D"
                this[400] = "CD"
                this[100] = "C"
                this[90] = "XC"
                this[50] = "L"
                this[40] = "XL"
                this[10] = "X"
                this[9] = "IX"
                this[5] = "V"
                this[4] = "IV"
                this[1] = "I"
            }
        }
    }

    fun toRoman(number: Int): String? {
        return if (number > 0) {
            val l = map.floorKey(number)
            if (number == l) {
                map[number]
            } else map[l]
                .toString() + toRoman(number - l)
        } else {
            number.toString()
        }
    }
}
