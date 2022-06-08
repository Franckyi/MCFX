package dev.franckyi.mcfx.impl

import dev.franckyi.mcfx.api.*

abstract class AbstractScreenEvent(override val screen: FXScreenBase) : ScreenEvent {
    final override var consumed: Boolean = false
        private set
    override lateinit var target: EventTarget

    override fun consume() {
        consumed = true
    }
}

abstract class AbstractInputEvent(screen: FXScreenBase) : AbstractScreenEvent(screen), InputEvent

abstract class AbstractMouseEvent(screen: FXScreenBase, override val mouseX: Double, override val mouseY: Double) :
    AbstractInputEvent(screen), MouseEvent

class MouseMoveEventImpl(screen: FXScreenBase, mouseX: Double, mouseY: Double) :
    AbstractMouseEvent(screen, mouseX, mouseY), MouseMoveEvent

abstract class AbstractMouseButtonEvent(
    screen: FXScreenBase,
    mouseX: Double,
    mouseY: Double,
    override val button: Int
) : AbstractMouseEvent(screen, mouseX, mouseY), MouseButtonEvent

class MouseClickEventImpl(screen: FXScreenBase, mouseX: Double, mouseY: Double, button: Int) :
    AbstractMouseButtonEvent(screen, mouseX, mouseY, button), MouseClickEvent

class MouseReleaseEventImpl(screen: FXScreenBase, mouseX: Double, mouseY: Double, button: Int) :
    AbstractMouseButtonEvent(screen, mouseX, mouseY, button), MouseReleaseEvent

class MouseDragEventImpl(
    screen: FXScreenBase,
    mouseX: Double,
    mouseY: Double,
    button: Int,
    override val deltaX: Double,
    override val deltaY: Double
) : AbstractMouseButtonEvent(screen, mouseX, mouseY, button), MouseDragEvent

class MouseScrollEventImpl(screen: FXScreenBase, mouseX: Double, mouseY: Double, override val amount: Double) :
    AbstractMouseEvent(screen, mouseX, mouseY), MouseScrollEvent

abstract class AbstractKeyEvent(screen: FXScreenBase, override val modifiers: Int) : AbstractInputEvent(screen),
    KeyEvent

abstract class AbstractKeyInputEvent(
    screen: FXScreenBase,
    override val keyCode: Int,
    override val scanCode: Int,
    modifiers: Int
) : AbstractKeyEvent(screen, modifiers), KeyInputEvent

class KeyPressEventImpl(screen: FXScreenBase, keyCode: Int, scanCode: Int, modifiers: Int) :
    AbstractKeyInputEvent(screen, keyCode, scanCode, modifiers), KeyPressEvent

class KeyReleaseEventImpl(screen: FXScreenBase, keyCode: Int, scanCode: Int, modifiers: Int) :
    AbstractKeyInputEvent(screen, keyCode, scanCode, modifiers), KeyReleaseEvent

class CharTypeEventImpl(screen: FXScreenBase, override val chr: Char, modifiers: Int) :
    AbstractKeyEvent(screen, modifiers), CharTypeEvent

class ActionEventImpl(screen: FXScreenBase, target: EventTarget) : AbstractScreenEvent(screen), ActionEvent {
    init {
        this.target = target
    }
}