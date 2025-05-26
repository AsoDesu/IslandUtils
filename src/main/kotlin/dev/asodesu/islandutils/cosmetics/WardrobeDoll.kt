package dev.asodesu.islandutils.cosmetics

import dev.asodesu.islandutils.api.extentions.minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.player.LocalPlayer
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

class WardrobeDoll(private val wardrobe: Wardrobe) {
    var xRot = 155f
    var yRot = -5f

    fun render(guiGraphics: GuiGraphics, x: Int, y: Int, size: Int) {
        val player = minecraft.player!!
        renderDoll(guiGraphics, x.toFloat(), y.toFloat(), size, player)
    }

    private fun renderDoll(guiGraphics: GuiGraphics, x: Float, y: Float, size: Int, livingEntity: LocalPlayer) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(0f, 0f, 150f)

        val yRotation = atan(yRot / 40)

        val quaternionf = Quaternionf().rotateZ(Math.PI.toFloat())
        val quaternionf2 = Quaternionf().rotateX(yRotation * 20.0f * (Math.PI / 180.0).toFloat())
        quaternionf.mul(quaternionf2)

        val preBodyRot = livingEntity.yBodyRot
        val preYRot = livingEntity.yRot
        val preXRot = livingEntity.xRot
        val preYHeadRot0 = livingEntity.yHeadRotO
        val preYHeadRot = livingEntity.yHeadRot
        val preCrouching = livingEntity.crouching

        livingEntity.yBodyRot = xRot
        livingEntity.yRot = xRot
        livingEntity.xRot = yRotation * -20f
        livingEntity.yHeadRot = livingEntity.yRot
        livingEntity.yHeadRotO = livingEntity.yRot
        livingEntity.crouching = false

        // store original items
        val originalItems = wardrobe.slots.associateWith { it.getFromEntity(livingEntity) }

        val vector3f = Vector3f(0.0f, livingEntity.bbHeight / 2.0f + 0.0625f, 0.0f)
        val sizef = size.toFloat()
        wardrobe.apply(guiGraphics, livingEntity)
        InventoryScreen.renderEntityInInventory(guiGraphics, x, y, sizef, vector3f, quaternionf, quaternionf2, livingEntity)

        // restore original items
        originalItems.forEach { (type, item) -> type.setToEntity(livingEntity, item) }

        livingEntity.yBodyRot = preBodyRot
        livingEntity.yRot = preYRot
        livingEntity.xRot = preXRot
        livingEntity.yHeadRotO = preYHeadRot0
        livingEntity.yHeadRot = preYHeadRot
        livingEntity.crouching = preCrouching

        guiGraphics.pose().popPose()
    }

}