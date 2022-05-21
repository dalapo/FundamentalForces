package com.sammy.fufo.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.sammy.fufo.FufoMod;
import com.sammy.fufo.client.ui.UIMath;
import com.sammy.fufo.common.blockentity.ArrayBlockEntity;
import com.sammy.fufo.common.blockentity.UITestBlockEntity;
import com.sammy.ortus.helpers.util.AnimationTickHolder;
import com.sammy.ortus.setup.OrtusRenderTypeRegistry;
import com.sammy.ortus.systems.rendering.VFXBuilders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

import static com.sammy.ortus.handlers.RenderHandler.DELAYED_RENDER;

public class ArrayRenderer implements BlockEntityRenderer<ArrayBlockEntity> {

    public ArrayRenderer(BlockEntityRendererProvider.Context context) {

    }

    private static final ResourceLocation TEST_BEAM = FufoMod.fufoPath("textures/vfx/uv_test.png");
    private static final RenderType TEST_BEAM_TYPE = OrtusRenderTypeRegistry.ADDITIVE_TEXTURE.apply(TEST_BEAM);
    @Override
    public void render(ArrayBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        VertexConsumer consumer = DELAYED_RENDER.getBuffer(TEST_BEAM_TYPE);
        Vec3 player = Minecraft.getInstance().player.getEyePosition();
        Vec3 center = new Vec3(pBlockEntity.getBlockPos().getX() + 0.5, pBlockEntity.getBlockPos().getY() + 1.5, pBlockEntity.getBlockPos().getZ() + 0.5);

        Vec3 startYaw = new Vec3(0.0, 0.0, 1.0);
        Vec3 endYaw = new Vec3(player.x, 0.0, player.z).subtract(new Vec3(center.x, 0.0, center.z)).normalize();
        Vec3 d = player.subtract(center);

        // Find angle between start & end in yaw
        float yaw = (float) Math.toDegrees(Math.atan2(endYaw.x - startYaw.x, endYaw.z - startYaw.z));

        // Find angle between start & end in pitch
        float pitch = (float) Math.toDegrees(Math.atan2(Math.sqrt(d.z * d.z + d.x * d.x), d.y) + Math.PI);

        Quaternion Q = Quaternion.ONE.copy();

        // doubling to account for how quats work
        Q.mul(new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), yaw * 2, true));
        Q.mul(new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), -pitch + 90, true));

//        pPoseStack.translate(-pBlockEntity.pos);
        pPoseStack.translate(0.5, 1.5, 0.5);
        pPoseStack.mulPose(Q);
        pPoseStack.scale(0.5f, 0.5f, 0.5f);
        VFXBuilders.createWorld().setColor(Color.ORANGE).setAlpha(1).renderQuad(consumer, pPoseStack, 1.0f, 1.0f);
        pPoseStack.popPose();
    }
}
