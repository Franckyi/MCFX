package dev.franckyi.mcfx.api

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.sound.SoundManager
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

val EMPTY_TEXT: Text = LiteralText.EMPTY

val mc: MinecraftClient get() = MinecraftClient.getInstance()
val font: TextRenderer get() = mc.textRenderer
val soundManager: SoundManager = mc.soundManager
val currentScreen: Screen get() = mc.currentScreen!!
val fxScreen: FXScreenBase get() = currentScreen as FXScreenBase

fun text(text: String): Text = LiteralText(text)

@JvmName("toText")
fun String.text(): Text = text(this)

fun translated(key: String, vararg args: Any): Text = TranslatableText(key, *args)

@JvmName("toTranslated")
fun String.translated(vararg args: Any): Text = translated(this, *args)

data class Box(val top: Int, val right: Int, val bottom: Int, val left: Int) {
    constructor(horizontal: Int, vertical: Int) : this(vertical, horizontal, vertical, horizontal)
    constructor(all: Int) : this(all, all, all, all)

    val vertical: Int get() = left + right
    val horizontal: Int get() = top + bottom

    companion object {
        val EMPTY = Box(0, 0, 0, 0)
        fun top(top: Int): Box = Box(top, 0, 0, 0)
        fun right(right: Int): Box = Box(0, right, 0, 0)
        fun bottom(bottom: Int): Box = Box(0, 0, bottom, 0)
        fun left(left: Int): Box = Box(0, 0, 0, left)
        fun horizontal(horizontal: Int): Box = Box(0, horizontal, 0, horizontal)
        fun vertical(vertical: Int): Box = Box(vertical, 0, vertical, 0)
    }
}

enum class HAlign {
    LEFT, CENTER, RIGHT;

    fun getAlignedX(totalWidth: Int, contentWidth: Int): Int = when (this) {
        LEFT -> 0
        CENTER -> (totalWidth - contentWidth) / 2
        RIGHT -> totalWidth - contentWidth
    }
}

enum class VAlign {
    TOP, CENTER, BOTTOM;

    fun getAlignedY(totalHeight: Int, contentHeight: Int): Int = when (this) {
        TOP -> 0
        CENTER -> (totalHeight - contentHeight) / 2
        BOTTOM -> totalHeight - contentHeight
    }
}

enum class Align(val halign: HAlign, val valign: VAlign) {
    TOP_LEFT(HAlign.LEFT, VAlign.TOP),
    TOP_CENTER(HAlign.CENTER, VAlign.TOP),
    TOP_RIGHT(HAlign.RIGHT, VAlign.TOP),
    CENTER_LEFT(HAlign.LEFT, VAlign.CENTER),
    CENTER(HAlign.CENTER, VAlign.CENTER),
    CENTER_RIGHT(HAlign.RIGHT, VAlign.CENTER),
    BOTTOM_LEFT(HAlign.LEFT, VAlign.BOTTOM),
    BOTTOM_CENTER(HAlign.CENTER, VAlign.BOTTOM),
    BOTTOM_RIGHT(HAlign.RIGHT, VAlign.BOTTOM);

    fun getAlignedX(totalWidth: Int, contentWidth: Int): Int = halign.getAlignedX(totalWidth, contentWidth)
    fun getAlignedY(totalHeight: Int, contentHeight: Int): Int = valign.getAlignedY(totalHeight, contentHeight)
}
