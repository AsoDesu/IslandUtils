package net.asodev.islandutils.modules.cosmetics;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CosmeticUI {

    public static void renderPlayerInInventory(GuiGraphics guiGraphics, int x0, int y0, int x1, int y1, int size, LivingEntity livingEntity) {
        float yRot = CosmeticState.yRot;
        float xRot = (float) Math.atan(CosmeticState.xRot / 40.0f);

        // save rotations
        float preYBodyRot = livingEntity.yBodyRot;
        float preYRot = livingEntity.getYRot();
        float preXrot = livingEntity.getXRot();
        float preYHeadRot0 = livingEntity.yHeadRotO;
        float preYHeadRot = livingEntity.yHeadRot;

        // set rotations
        livingEntity.yBodyRot = yRot;
        livingEntity.setYRot(yRot);
        livingEntity.yHeadRot = livingEntity.yBodyRot;
        livingEntity.yHeadRotO = livingEntity.yBodyRot;
        livingEntity.setXRot(xRot * -20f); // magic value - InventoryScreen#renderEntityInInventoryFollowsMouse [setXRot]

        float f = 0.0625F; // magic value - InventoryScreen#renderBg [renderEntityInInventoryFollowsMouse]
        float livingEntityScale = livingEntity.getScale();

        // here we make some quaternions and do some math with them
        // idfk this is just what the inventory code does :sob:
        // magic values - InventoryScreen#renderEntityInInventoryFollowsMouse
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(xRot * 20.0F * ((float) Math.PI / 180.0F));
        quaternionf.mul(quaternionf2);
        Vector3f vector3f = new Vector3f(0.0F, livingEntity.getBbHeight() / 2.0F + f * livingEntityScale, 0.0F);
//        InventoryScreen.renderEntityInInventoryFollowsMouse(
//                guiGraphics,
//                x0, y0, x1, y1,
//                size / livingEntityScale,
//                vector3f,
//                quaternionf,
//                quaternionf2,
//                livingEntity
//        );
        EntityRenderState renderState = InventoryScreen.extractRenderState(livingEntity);
        guiGraphics.submitEntityRenderState(renderState, (float) size, vector3f, quaternionf, quaternionf2, x0, y0, x1, y1);

        // restore rotations
        livingEntity.yBodyRot = preYBodyRot;
        livingEntity.setYRot(preYRot);
        livingEntity.setXRot(preXrot);
        livingEntity.yHeadRotO = preYHeadRot0;
        livingEntity.yHeadRot = preYHeadRot;
    }

}
