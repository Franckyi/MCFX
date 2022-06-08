package dev.franckyi.mcfx.api

import net.minecraft.text.LiteralText
import net.minecraft.text.Text

fun Any.label(text: Text = LiteralText.EMPTY, init: Label.() -> Unit = {}): Label = build(DI.get(text), init)

fun Any.button(text: Text = LiteralText.EMPTY, init: Button.() -> Unit = {}): Button = build(DI.get(text), init)

fun Any.checkBox(text: Text = LiteralText.EMPTY, selected: Boolean = false, init: CheckBox.() -> Unit = {}): CheckBox =
    build(DI.get(text, selected), init)

fun Any.textField(text: String = "", init: TextField.() -> Unit = {}): TextField = build(DI.get(text), init)

fun Any.hbox(spacing: Int = 0, init: HBox.() -> Unit = {}): HBox = build(DI.get(spacing), init)

fun Any.vbox(spacing: Int = 0, init: VBox.() -> Unit = {}): VBox = build(DI.get(spacing), init)

internal fun <N : Node> Any.build(node: N, init: N.() -> Unit = {}): N {
    init.invoke(node)
    when (this) {
        is Layout -> children += node
        is FXScreenBase -> root = node
    }
    return node
}