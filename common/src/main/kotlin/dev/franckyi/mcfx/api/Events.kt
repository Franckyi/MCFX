package dev.franckyi.mcfx.api

import org.lwjgl.glfw.GLFW

interface ScreenEvent {
    val screen: FXScreenBase
    val consumed: Boolean
    var target: EventTarget
    fun consume()
}

interface InputEvent : ScreenEvent

interface MouseEvent : InputEvent {
    val mouseX: Double
    val mouseY: Double
}

interface MouseMoveEvent : MouseEvent

interface MouseButtonEvent : MouseEvent {
    val button: Int
    val isLeftButton: Boolean get() = button == GLFW.GLFW_MOUSE_BUTTON_LEFT
    val isRightButton: Boolean get() = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT
    val isMiddleButton: Boolean get() = button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE
}

interface MouseClickEvent : MouseButtonEvent

interface MouseReleaseEvent : MouseButtonEvent

interface MouseDragEvent : MouseButtonEvent {
    val deltaX: Double
    val deltaY: Double
}

interface MouseScrollEvent : MouseEvent {
    val amount: Double
}

interface KeyEvent : InputEvent {
    val modifiers: Int
    val isShiftDown: Boolean get() = (modifiers and GLFW.GLFW_MOD_SHIFT) != 0
    val isControlDown: Boolean get() = (modifiers and GLFW.GLFW_MOD_CONTROL) != 0
    val isAltDown: Boolean get() = (modifiers and GLFW.GLFW_MOD_ALT) != 0
    val isSuperDown: Boolean get() = (modifiers and GLFW.GLFW_MOD_SUPER) != 0
    val isCapsLock: Boolean get() = (modifiers and GLFW.GLFW_MOD_CAPS_LOCK) != 0
    val isNumLock: Boolean get() = (modifiers and GLFW.GLFW_MOD_NUM_LOCK) != 0
}

interface KeyInputEvent : KeyEvent {
    val keyCode: Int
    val scanCode: Int
}

interface KeyPressEvent : KeyInputEvent

interface KeyReleaseEvent : KeyInputEvent

interface CharTypeEvent : KeyEvent {
    val chr: Char
}

interface ActionEvent : ScreenEvent