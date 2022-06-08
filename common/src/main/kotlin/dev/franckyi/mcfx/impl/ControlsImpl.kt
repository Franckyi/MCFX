package dev.franckyi.mcfx.impl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.franckyi.mcfx.api.*
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import kotlin.math.max

abstract class AbstractControl : AbstractNode(), Control

abstract class AbstractLabeled(override var text: Text = EMPTY_TEXT) : AbstractControl(), Labeled

class LabelImpl(text: Text = EMPTY_TEXT) : AbstractLabeled(text), Label {
    override var textColor: Int = 0xFFFFFF
    override var textAlign: Align = Align.CENTER

    override val computedWidth: Int get() = font.getWidth(text) + padding.horizontal
    override val computedHeight: Int get() = font.fontHeight + padding.vertical

    override fun renderContent(ctx: RenderContext) {
        val textX = x + textAlign.getAlignedX(width, computedWidth)
        val textY = y + textAlign.getAlignedY(height, computedHeight)
        font.draw(ctx.matrices, text, textX.toFloat(), textY.toFloat(), textColor)
    }
}

abstract class ButtonBase(text: Text = EMPTY_TEXT) : AbstractLabeled(text) {
    override fun mouseClicked(event: MouseClickEvent) {
        super.mouseClicked(event)
        if (event.isLeftButton) {
            event.screen.dispatchActionEvent(this)
        }
    }

    override fun keyPressed(event: KeyPressEvent) {
        if (event.keyCode == GLFW.GLFW_KEY_ENTER || event.keyCode == GLFW.GLFW_KEY_SPACE || event.keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            event.screen.dispatchActionEvent(this)
        }
    }

    override fun actionPerformed(event: ActionEvent) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
    }
}

class ButtonImpl(text: Text = EMPTY_TEXT) : ButtonBase(text), Button {
    override var textColor: Int = -1

    override val computedWidth: Int get() = 20 + font.getWidth(text) + padding.horizontal
    override val computedHeight: Int get() = 20 + padding.vertical

    override fun renderContent(ctx: RenderContext) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderTexture(0, ClickableWidget.WIDGETS_TEXTURE)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        val i = if (disabled) 0 else if (hovered(ctx.mouseX, ctx.mouseY)) 2 else 1
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.enableDepthTest()
        DrawableHelper.drawTexture(ctx.matrices, x, y, 0f, 46f + i * 20, width / 2, height, 256, 256)
        DrawableHelper.drawTexture(
            ctx.matrices,
            x + width / 2,
            y,
            200f - width / 2,
            46f + i * 20,
            width / 2,
            height,
            256,
            256
        )
        val color = if (textColor != -1) textColor else if (disabled) 0xA0A0A0 else 0xFFFFFF
        DrawableHelper.drawCenteredText(ctx.matrices, font, text, x + width / 2, y + (height - 8) / 2, color)
    }
}

class CheckBoxImpl(text: Text = EMPTY_TEXT, override var selected: Boolean = false) : ButtonBase(text), CheckBox {
    override var textColor: Int = 0xE0E0E0
    override var boxSize: Int = 20
    override var textGap: Int = 8
    override var showLabel: Boolean = true

    override val computedWidth: Int get() = boxSize + textGap + font.getWidth(text) + padding.horizontal
    override val computedHeight: Int get() = max(boxSize, font.fontHeight) + padding.vertical

    override fun renderContent(ctx: RenderContext) {
        RenderSystem.setShaderTexture(0, TEXTURE)
        RenderSystem.enableDepthTest()
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
        DrawableHelper.drawTexture(
            ctx.matrices,
            x,
            y + (height - boxSize) / 2,
            boxSize,
            boxSize,
            if (hovered(ctx.mouseX, ctx.mouseY)) 20.0f else 0.0f,
            if (selected) 20.0f else 0.0f,
            20,
            20,
            64,
            64
        )
        if (showLabel) {
            font.draw(ctx.matrices, text, x + boxSize + textGap.toFloat(), y + (height - 8) / 2f, textColor)
        }
    }

    override fun actionPerformed(event: ActionEvent) {
        super<ButtonBase>.actionPerformed(event)
        selected = !selected
    }

    companion object {
        private val TEXTURE = Identifier("textures/gui/checkbox.png")
    }
}

class TextFieldImpl(override var text: String = "") : AbstractControl(), TextField {
    override var maxLength: Int = Int.MAX_VALUE
    override var textRenderer: (String) -> Text = { LiteralText(it) }
    override var textOffset: Int = 0
    override var caretPosition: Int = 0
    override var selectionStart: Int = 0

    override val computedWidth: Int get() = 200 + padding.horizontal
    override val computedHeight: Int get() = 20 + padding.vertical

    override fun renderContent(ctx: RenderContext) {
        DrawableHelper.fill(ctx.matrices, x - 1, y - 1, x + width + 1, y + height + 1, if (focused) -1 else -6250336)
        DrawableHelper.fill(ctx.matrices, x, y, x + width, y + height, -16777216)
        val trimmedText = font.trimToWidth(text.substring(textOffset), width - padding.horizontal - 2)
        font.draw(
            ctx.matrices,
            textRenderer(trimmedText),
            x + padding.left + 1f,
            y + (height - font.fontHeight) / 2f,
            0xFFFFFF
        )
    }

    override fun keyPressed(event: KeyPressEvent) {
        when (event.keyCode) {
            GLFW.GLFW_KEY_BACKSPACE -> {
                if (caretPosition > 0) {
                    text = text.substring(0, caretPosition - 1) + text.substring(caretPosition)
                    caretPosition--
                }
            }
        }
    }

    override fun charTyped(event: CharTypeEvent) {
        text = text.substring(0, caretPosition) + event.chr + text.substring(caretPosition)
        caretPosition++
    }
}