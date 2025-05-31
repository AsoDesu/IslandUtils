package dev.asodesu.islandutils.cosmetics.cloak

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.PlayerRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class CloakRenderLayer(
    parent: RenderLayerParent<PlayerRenderState, PlayerModel>,
    private val context: EntityRendererProvider.Context
) : RenderLayer<PlayerRenderState, PlayerModel>(parent) {
    companion object {
        var cloakForNextRender: ItemStack? = null
        var playerEntityForNextRender: LivingEntity? = null
    }
    private val itemModelResolver = context.itemModelResolver
    private val cloakItem = ItemStackRenderState()

    override fun render(poseStack: PoseStack, multiBufferSource: MultiBufferSource, light: Int, entityRenderState: PlayerRenderState, f: Float, g: Float) {
        val cloak = cloakForNextRender ?: return
        cloakForNextRender = null

        val player = playerEntityForNextRender ?: return
        playerEntityForNextRender = null

        val entityModel = this.parentModel
        entityModel.root().translateAndRotate(poseStack)
        entityModel.body.translateAndRotate(poseStack)
        poseStack.translate(0f, -2.1f, 0f)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f))
        poseStack.scale(0.625f, -0.625f, -0.625f)
        itemModelResolver.updateForLiving(cloakItem, cloak, ItemDisplayContext.HEAD, player)
        cloakItem.render(poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY)
    }

}