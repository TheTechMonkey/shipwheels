package com.techmonkey.shipwheels.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import com.techmonkey.shipwheels.ShipWheelBlock;
import com.techmonkey.shipwheels.ShipWheelBlockEntity;

/** Draws the wheel's block model, turned around its axle by the wheel's current angle. */
public class ShipWheelRenderer implements BlockEntityRenderer<ShipWheelBlockEntity, ShipWheelRenderState> {
    private static final int[] NO_TINTS = new int[0];
    private final RandomSource random = RandomSource.create();

    public ShipWheelRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public ShipWheelRenderState createRenderState() {
        return new ShipWheelRenderState();
    }

    @Override
    public void extractRenderState(ShipWheelBlockEntity wheel, ShipWheelRenderState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(wheel, state, partialTicks, cameraPosition, breakProgress);
        state.wheelState = wheel.getBlockState();
        state.facing = state.wheelState.hasProperty(ShipWheelBlock.FACING) ? state.wheelState.getValue(ShipWheelBlock.FACING) : Direction.NORTH;
        state.angle = wheel.getAngle(partialTicks);
    }

    @Override
    public void submit(ShipWheelRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.wheelState == null) {
            return;
        }
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state.wheelState);
        List<BlockStateModelPart> parts = new ArrayList<>();
        random.setSeed(42L);
        model.collectParts(random, parts);

        poseStack.pushPose();
        // Turn around the axle, which runs through the middle of the block, in and out of the wall.
        poseStack.translate(0.5F, 0.5F, 0.5F);
        Direction facing = state.facing;
        float turn = state.angle * facing.getAxisDirection().getStep();
        poseStack.mulPose(new Matrix4f().rotation(facing.getAxis() == Direction.Axis.X ? Axis.XP.rotationDegrees(turn) : Axis.ZP.rotationDegrees(turn)));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        collector.submitBlockModel(poseStack, RenderTypes.solidMovingBlock(), parts, NO_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 96;
    }
}
