package dev.franckyi.mcfx.impl

import dev.franckyi.mcfx.api.*
import net.minecraft.text.Text
import kotlin.math.max
import kotlin.math.min

abstract class AbstractNode : Node {
    override var x: Int = 0
    override var y: Int = 0
    override var width: Int = 0
    override var height: Int = 0

    override var minWidth: Int = 0
    override var minHeight: Int = 0
    override var prefWidth: Int = Node.USE_COMPUTED_SIZE
    override var prefHeight: Int = Node.USE_COMPUTED_SIZE
    override var maxWidth: Int = Int.MAX_VALUE
    override var maxHeight: Int = Int.MAX_VALUE

    override val updatedWidth: Int
        get() = max(
            minWidth,
            min(if (prefWidth == Node.USE_COMPUTED_SIZE) computedWidth else prefWidth, maxWidth)
        )
    override val updatedHeight: Int
        get() = max(
            minHeight,
            min(if (prefHeight == Node.USE_COMPUTED_SIZE) computedHeight else prefHeight, maxHeight)
        )

    override var padding: Box = Box.EMPTY
    override var parent: Parent? = null
    override var visible: Boolean = true
    override val disabled: Boolean get() = disable || (parent is Node && (parent as Node).disabled)
    override var disable: Boolean = false
    override var focused: Boolean = false

    override var tooltip: List<Text> = mutableListOf()

    override val eventFilters: MutableList<EventHandlerData<*>> = mutableListOf()
    override val eventHandlers: MutableList<EventHandlerData<*>> = mutableListOf()

    override fun hovered(mouseX: Int, mouseY: Int): Boolean =
        mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height

    override fun findEventTarget(event: ScreenEvent): EventTarget? {
        if (!visible || disabled) return null
        if (event is KeyEvent && focused) return this
        if (event is MouseEvent && hovered(event.mouseX.toInt(), event.mouseY.toInt())) return this
        return null
    }

    override fun preRender(ctx: RenderContext) {
    }

    override fun render(ctx: RenderContext) {
        if (!visible) return
        renderBackground(ctx)
        renderContent(ctx)
        renderTooltip(ctx)
    }

    override fun postRender(ctx: RenderContext) {
    }

    protected open fun renderBackground(ctx: RenderContext) {
    }

    protected abstract fun renderContent(ctx: RenderContext)

    protected open fun renderTooltip(ctx: RenderContext) {
        if (tooltip.isEmpty()) return
        currentScreen.renderTooltip(ctx.matrices, tooltip, ctx.mouseX, ctx.mouseY)
    }

    override fun mouseClicked(event: MouseClickEvent) {
        if (this == event.target && event.isLeftButton) {
            event.screen.focused = this
        }
    }
}