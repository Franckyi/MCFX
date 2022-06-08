package dev.franckyi.mcfx.impl

import dev.franckyi.mcfx.api.*

fun setupVanillaImpl() {
    setupControls()
    setupLayouts()
    setupEvents()
}

fun setupControls() {
    DI.factory<Label> { LabelImpl(param()) }
    DI.factory<Button> { ButtonImpl(param()) }
    DI.factory<CheckBox> { CheckBoxImpl(param(), param()) }
    DI.factory<TextField> { TextFieldImpl(param()) }
}

fun setupLayouts() {
    DI.factory<HBox> { HBoxImpl(param()) }
    DI.factory<VBox> { VBoxImpl(param()) }
}

fun setupEvents() {
    DI.factory<MouseMoveEvent> { MouseMoveEventImpl(param(), param(), param()) }
    DI.factory<MouseClickEvent> { MouseClickEventImpl(param(), param(), param(), param()) }
    DI.factory<MouseReleaseEvent> { MouseReleaseEventImpl(param(), param(), param(), param()) }
    DI.factory<MouseDragEvent> { MouseDragEventImpl(param(), param(), param(), param(), param(), param()) }
    DI.factory<MouseScrollEvent> { MouseScrollEventImpl(param(), param(), param(), param()) }
    DI.factory<KeyPressEvent> { KeyPressEventImpl(param(), param(), param(), param()) }
    DI.factory<KeyReleaseEvent> { KeyReleaseEventImpl(param(), param(), param(), param()) }
    DI.factory<CharTypeEvent> { CharTypeEventImpl(param(), param(), param()) }
    DI.factory<ActionEvent> { ActionEventImpl(param(), param()) }
}
