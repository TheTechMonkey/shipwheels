package com.techmonkey.shipwheels.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import net.fabricmc.api.ClientModInitializer;

import com.techmonkey.shipwheels.ShipWheels;

public class ShipWheelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ShipWheels.WHEEL_BLOCK_ENTITY, ShipWheelRenderer::new);
    }
}
