package me.racci.raccilib.utils

import java.util.TreeMap

object NumberUtils {

    private val map = TreeMap<Int, String>()
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

    init {
        map[1000000] = "M"
        map[900000] = "CM"
        map[500000] = "D"
        map[100000] = "C"
        map[90000] = "XC"
        map[50000] = "L"
        map[10000] = "X"
        map[9000] = "MX"
        map[5000] = "v"
        map[1000] = "M"
        map[900] = "CM"
        map[500] = "D"
        map[400] = "CD"
        map[100] = "C"
        map[90] = "XC"
        map[50] = "L"
        map[40] = "XL"
        map[10] = "X"
        map[9] = "IX"
        map[5] = "V"
        map[4] = "IV"
        map[1] = "I"
    }
}