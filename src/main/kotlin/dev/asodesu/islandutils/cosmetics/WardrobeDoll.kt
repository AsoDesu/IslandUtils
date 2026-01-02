package dev.asodesu.islandutils.cosmetics

import dev.asodesu.islandutils.api.extentions.minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

class WardrobeDoll(private val wardrobe: Wardrobe) {
    var xRot = -25f
    var yRot = -5f

    fun render(guiGraphics: GuiGraphics, x0: Int, y0: Int, x1: Int, y1: Int, size: Int) {
        val player = minecraft.player!!
        renderDoll(guiGraphics, x0, y0, x1, y1, size, player)
    }

    private fun renderDoll(guiGraphics: GuiGraphics, x0: Int, y0: Int, x1: Int, y1: Int, size: Int, livingEntity: LocalPlayer) {
        // store original items
        val originalItems = wardrobe.slots.associateWith { it.getFromEntity(livingEntity) }
        wardrobe.apply(guiGraphics, livingEntity)

        // appropriated from InventoryScreen#renderEntityInInventoryFollowsMouse
        //  we have to copy and use our own due to the way our rotations work :3
        val yAngle = atan((yRot / 40.0f).toDouble()).toFloat()
        val xAngle = xRot
        val rotation = Quaternionf().rotateZ(Math.PI.toFloat())
        val xRotation = Quaternionf().rotateX(yAngle * 20.0f * (Math.PI / 180.0).toFloat())
        rotation.mul(xRotation)
        val entityRenderState = InventoryScreen.extractRenderState(livingEntity)
        if (entityRenderState is LivingEntityRenderState) {
            entityRenderState.bodyRot = 180f + xAngle
            entityRenderState.yRot = 0f
            entityRenderState.xRot = -yAngle * 20.0f
            entityRenderState.boundingBoxWidth /= entityRenderState.scale
            entityRenderState.boundingBoxHeight /= entityRenderState.scale
            entityRenderState.scale = 1.0f
        }

        val translation = Vector3f(0.0f, entityRenderState.boundingBoxHeight / 2.0f + 0.0625f, 0.0f)
        guiGraphics.submitEntityRenderState(
            entityRenderState,
            size.toFloat(),
            translation,
            rotation,
            xRotation,
            x0,
            y0,
            x1,
            y1
        )

        // restore original items
        originalItems.forEach { (type, item) -> type.setToEntity(livingEntity, item) }
    }

}