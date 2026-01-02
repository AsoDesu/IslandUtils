package dev.asodesu.islandutils.cosmetics.cloak

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.model.player.PlayerModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.AvatarRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class CloakRenderLayer(
    parent: RenderLayerParent<AvatarRenderState, PlayerModel>,
    context: EntityRendererProvider.Context
) : RenderLayer<AvatarRenderState, PlayerModel>(parent) {
    companion object {
        var cloakForNextRender: ItemStack? = null
        var playerEntityForNextRender: LivingEntity? = null
    }
    private val itemModelResolver = context.itemModelResolver
    private val cloakItem = ItemStackRenderState()

    override fun submit(poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector, i: Int, entityRenderState: AvatarRenderState, f: Float, g: Float) {
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
        cloakItem.submit(poseStack, submitNodeCollector, i, OverlayTexture.NO_OVERLAY, entityRenderState.outlineColor)
    }

}