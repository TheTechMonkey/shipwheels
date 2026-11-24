package com.techmonkey.shipwheels.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ShipWheelRenderState extends BlockEntityRenderState {
    public BlockState wheelState;
    public Direction facing = Direction.NORTH;
    public float angle;
}
