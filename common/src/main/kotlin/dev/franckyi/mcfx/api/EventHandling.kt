package dev.franckyi.mcfx.api

import kotlin.reflect.KClass

interface EventTarget {
    fun findEventTarget(event: ScreenEvent): EventTarget?
    fun buildEventDispatchChain(): EventDispatchChain
}

interface EventHandler {
    companion object {
        private val priorities: MutableMap<KClass<out ScreenEvent>, Int> = mutableMapOf(
            ScreenEvent::class to 0,
            InputEvent::class to 1,
            MouseEvent::class to 2,
            MouseMoveEvent::class to 3,
            MouseClickEvent::class to 3,
            MouseReleaseEvent::class to 3,
            MouseDragEvent::class to 3,
            MouseScrollEvent::class to 3,
            KeyEvent::class to 2,
            KeyPressEvent::class to 3,
            KeyReleaseEvent::class to 3,
            CharTypeEvent::class to 3,
            ActionEvent::class to 1,
        )
    }

    val eventFilters: MutableList<EventHandlerData<*>>
    val eventHandlers: MutableList<EventHandlerData<*>>

    fun <E : ScreenEvent> captureEvent(event: E) = callListeners(event, eventFilters)
    fun <E : ScreenEvent> bubbleEvent(event: E) {
        when (event) {
            is MouseMoveEvent -> mouseMoved(event)
            is MouseClickEvent -> mouseClicked(event)
            is MouseReleaseEvent -> mouseReleased(event)
            is MouseDragEvent -> mouseDragged(event)
            is MouseScrollEvent -> mouseScrolled(event)
            is KeyPressEvent -> keyPressed(event)
            is KeyReleaseEvent -> keyReleased(event)
            is CharTypeEvent -> charTyped(event)
            is ActionEvent -> actionPerformed(event)
        }
        callListeners(event, eventHandlers)
    }

    @Suppress("unchecked_cast")
    private fun <E : ScreenEvent> callListeners(event: E, handlers: List<EventHandlerData<*>>) {
        handlers
            .filter { it.first.isInstance(event) }
            .map { it as EventHandlerData<E> }
            .sortedBy { priorities[it.first] }
            .forEach { it.second(event) }
    }

    fun <E : ScreenEvent> addEventFilter(eventType: KClass<E>, eventListener: EventListener<E>) {
        eventFilters += eventType to eventListener
    }

    fun <E : ScreenEvent> removeEventFilter(eventType: KClass<E>, eventListener: EventListener<E>) {
        eventFilters -= eventType to eventListener
    }

    fun <E : ScreenEvent> addEventHandler(eventType: KClass<E>, eventListener: EventListener<E>) {
        eventHandlers += eventType to eventListener
    }

    fun <E : ScreenEvent> removeEventHandler(eventType: KClass<E>, eventListener: EventListener<E>) {
        eventHandlers -= eventType to eventListener
    }

    fun mouseMoved(event: MouseMoveEvent) {}
    fun mouseClicked(event: MouseClickEvent) {}
    fun mouseReleased(event: MouseReleaseEvent) {}
    fun mouseDragged(event: MouseDragEvent) {}
    fun mouseScrolled(event: MouseScrollEvent) {}
    fun keyPressed(event: KeyPressEvent) {}
    fun keyReleased(event: KeyReleaseEvent) {}
    fun charTyped(event: CharTypeEvent) {}
    fun actionPerformed(event: ActionEvent) {}
}

inline fun <reified E : ScreenEvent> EventHandler.addEventFilter(noinline eventListener: EventListener<E>) =
    addEventFilter(E::class, eventListener)

inline fun <reified E : ScreenEvent> EventHandler.removeEventFilter(noinline eventListener: EventListener<E>) =
    removeEventFilter(E::class, eventListener)

inline fun <reified E : ScreenEvent> EventHandler.addEventHandler(noinline eventListener: EventListener<E>) =
    addEventHandler(E::class, eventListener)

inline fun <reified E : ScreenEvent> EventHandler.removeEventHandler(noinline eventListener: EventListener<E>) =
    removeEventHandler(E::class, eventListener)

typealias EventHandlerData<E> = Pair<KClass<E>, EventListener<E>>

typealias EventListener<E> = (E) -> Unit

typealias EventDispatchChain = List<EventHandler>