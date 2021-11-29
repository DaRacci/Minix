@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.PatternPane
import com.github.stefvanschie.inventoryframework.pane.util.Pattern

inline fun PatternPane(
    x: Int,
    y: Int,
    length: Int,
    height: Int,
    priority: Pane.Priority,
    pattern: Pattern,
    unit: PatternPane.() -> Unit
) = PatternPane(x,y,length,height,priority,pattern).also(unit)

inline fun ChestGui(
    rows: Int,
    title: String,
    unit: ChestGui.() -> Unit
) = ChestGui(rows, title).also(unit)