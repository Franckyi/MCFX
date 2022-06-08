package dev.franckyi.mcfx.api

import net.minecraft.text.Text

interface Control : Node

interface Labeled : Control {
    var text: Text
    var textColor: Int
}

interface Label : Labeled {
    var textAlign: Align
}

interface Button : Labeled

interface CheckBox : Labeled {
    var selected: Boolean
    var boxSize: Int
    var textGap: Int
    var showLabel: Boolean
}

interface TextField : Control {
    var text: String
    var maxLength: Int
    var textRenderer: (String) -> Text
    var textOffset: Int
    var caretPosition: Int
    var selectionStart: Int
}