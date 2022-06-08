package dev.franckyi.mcfx.impl

import dev.franckyi.mcfx.api.*
import kotlin.math.min

abstract class AbstractLayout : AbstractNode(), Layout {
    override val children: MutableList<Node> = mutableListOf()

    override fun findEventTarget(event: ScreenEvent): EventTarget? {
        if (!visible || disabled) return null
        if (event is KeyEvent && focused) return this
        for (child in children) {
            val target = child.findEventTarget(event)
            if (target != null) {
                return target
            }
        }
        if (event is MouseEvent && hovered(event.mouseY.toInt(), event.mouseY.toInt())) return this
        return null
    }

    override fun tick() {
        children.forEach { it.tick() }
    }

    override fun preRender(ctx: RenderContext) {
        layoutChildren(ctx)
    }

    abstract fun layoutChildren(ctx: RenderContext)

    override fun renderContent(ctx: RenderContext) {
        children.forEach { it.render(ctx) }
    }
}

abstract class AbstractSpacedLayout(override var spacing: Int = 0) : AbstractLayout(), SpacedLayout

class HBoxImpl(spacing: Int = 0) : AbstractSpacedLayout(spacing), HBox {
    override var valign: VAlign = VAlign.TOP

    override val computedWidth: Int get() = children.sumOf { it.computedWidth } + (children.size - 1) * spacing + padding.horizontal
    override val computedHeight: Int get() = children.maxOf { it.computedHeight } + padding.vertical

    override fun layoutChildren(ctx: RenderContext) {
        var currentX = x + padding.left
        val currentY = y + padding.top
        for (child in children) {
            child.parent = this
            child.width = min((x + width - padding.right) - currentX, child.updatedWidth)
            child.height = min(height, child.updatedHeight)
            child.x = currentX
            child.y = currentY + valign.getAlignedY(height, child.height)
            currentX += child.width + spacing
        }
    }
}

class VBoxImpl(spacing: Int = 0) : AbstractSpacedLayout(spacing), VBox {
    override var halign: HAlign = HAlign.LEFT

    override val computedWidth: Int get() = children.maxOf { it.computedWidth } + padding.horizontal
    override val computedHeight: Int get() = children.sumOf { it.computedHeight } + (children.size - 1) * spacing + padding.vertical

    override fun layoutChildren(ctx: RenderContext) {
        val currentX = x + padding.left
        var currentY = y + padding.top
        for (child in children) {
            child.parent = this
            child.width = min(width, child.updatedWidth)
            child.height = min((y + height - padding.bottom) - currentY, child.updatedHeight)
            child.x = currentX + halign.getAlignedX(width, child.width)
            child.y = currentY
            currentY += child.height + spacing
        }
    }
}