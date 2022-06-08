package dev.franckyi.mcfx.api

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

interface Tickable {
    fun tick() {}
}

interface Parent : EventTarget, EventHandler, Tickable {
    val parent: Parent?
    override fun buildEventDispatchChain(): EventDispatchChain = mutableListOf<EventHandler>(this).also {
        var parent = this.parent
        while (parent != null) {
            it.add(parent)
            parent = parent.parent
        }
        it.reverse()
    }
}

interface Node : Renderable, Parent {
    companion object {
        const val USE_COMPUTED_SIZE = -1
    }

    var x: Int
    var y: Int
    var width: Int
    var height: Int
    var minWidth: Int
    var minHeight: Int
    var prefWidth: Int
    var prefHeight: Int
    var maxWidth: Int
    var maxHeight: Int
    val computedWidth: Int
    val computedHeight: Int
    val updatedWidth: Int
    val updatedHeight: Int
    override var parent: Parent?
    var padding: Box
    var visible: Boolean
    val disabled: Boolean
    var disable: Boolean
    var focused: Boolean
    var tooltip: List<Text>
    fun hovered(mouseX: Int, mouseY: Int): Boolean
}

interface FXScreen : Parent, Renderable {
    val focused: Node?
    fun dispatchEvent(event: ScreenEvent): Boolean
}

open class FXScreenBase(var root: Node? = null, title: Text? = null) : Screen(title), FXScreen {
    override val parent: Parent? = null
    private var _focused: Node? = null
    override var focused: Node?
        get() = _focused
        internal set(value) {
            _focused?.focused = false
            value?.let { it.focused = true }
            _focused = value
        }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        val ctx = RenderContext(matrices, mouseX, mouseY, delta)
        preRender(ctx)
        render(ctx)
        postRender(ctx)
    }

    override fun render(ctx: RenderContext) {
        renderBackground(ctx.matrices)
        renderContent(ctx)
    }

    protected open fun renderContent(ctx: RenderContext) = root?.also {
        it.parent = this
        it.x = 0
        it.y = 0
        it.width = width
        it.height = height
        it.preRender(ctx)
        it.render(ctx)
        it.postRender(ctx)
    }

    override fun tick() {
        root?.tick()
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        //dispatchEvent(DI.get<MouseMoveEvent>(this, mouseX, mouseY))
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return dispatchEvent(DI.get<MouseClickEvent>(this, mouseX, mouseY, button))
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return dispatchEvent(DI.get<MouseReleaseEvent>(this, mouseX, mouseY, button))
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return dispatchEvent(DI.get<MouseDragEvent>(this, mouseX, mouseY, button, deltaX, deltaY))
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return dispatchEvent(DI.get<MouseScrollEvent>(this, mouseX, mouseY, amount))
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (super<Screen>.keyPressed(keyCode, scanCode, modifiers)) return true
        return dispatchEvent(DI.get<KeyPressEvent>(this, keyCode, scanCode, modifiers))
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return dispatchEvent(DI.get<KeyReleaseEvent>(this, keyCode, scanCode, modifiers))
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        return dispatchEvent(DI.get<CharTypeEvent>(this, chr, modifiers))
    }

    fun dispatchActionEvent(target: EventTarget) {
        dispatchEvent(DI.get<ActionEvent>(this, target))
    }

    override fun dispatchEvent(event: ScreenEvent): Boolean {
        if (event !is ActionEvent) event.target = findEventTarget(event)
        val captureChain = event.target.buildEventDispatchChain()
        val bubbleChain = mutableListOf<EventHandler>()
        for (node in captureChain) {
            bubbleChain += node
            node.captureEvent(event)
            if (event.consumed) break
        }
        bubbleChain.reverse()
        for (node in bubbleChain) {
            node.bubbleEvent(event)
            if (event.consumed) break
        }
        return event.consumed
    }

    override val eventFilters: MutableList<EventHandlerData<*>> = mutableListOf()
    override val eventHandlers: MutableList<EventHandlerData<*>> = mutableListOf()

    override fun findEventTarget(event: ScreenEvent): EventTarget = root?.findEventTarget(event) ?: this

    override fun buildEventDispatchChain(): EventDispatchChain = listOf(this)
}