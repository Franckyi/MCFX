package dev.franckyi.mcfx.api

import net.minecraft.client.util.math.MatrixStack

interface Renderable {
    fun preRender(ctx: RenderContext) {}
    fun render(ctx: RenderContext)
    fun postRender(ctx: RenderContext) {}
}

data class RenderContext(val matrices: MatrixStack, val mouseX: Int, val mouseY: Int, val delta: Float)

typealias NodeRenderer<N> = N.(RenderContext) -> Unit