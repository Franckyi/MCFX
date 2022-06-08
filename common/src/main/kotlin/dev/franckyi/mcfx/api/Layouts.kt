package dev.franckyi.mcfx.api

interface Layout : Node {
    val children: MutableList<Node>
}

interface SpacedLayout : Layout {
    var spacing: Int
}

interface HBox {
    var valign: VAlign
}

interface VBox {
    var halign: HAlign
}