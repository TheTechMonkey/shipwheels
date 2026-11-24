package com.techmonkey.shipwheels;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Remembers how far the wheel has turned and how fast it is spinning (client side only). */
public class ShipWheelBlockEntity extends BlockEntity {
    public static final int EVENT_SPIN = 1;
    private static final float PUSH = 14.0F;
    private static final float MAX_SPEED = 45.0F;
    private static final float FRICTION = 0.94F;

    private float angle;
    private float previousAngle;
    private float speed;

    public ShipWheelBlockEntity(BlockPos pos, BlockState state) {
        super(ShipWheels.WHEEL_BLOCK_ENTITY, pos, state);
    }

    /** A spin sent from the server to every player nearby: 1 = clockwise push, 0 = the other way. */
    @Override
    public boolean triggerEvent(int id, int param) {
        if (id != EVENT_SPIN) {
            return super.triggerEvent(id, param);
        }
        speed = Math.clamp(speed + (param == 1 ? PUSH : -PUSH), -MAX_SPEED, MAX_SPEED);
        return true;
    }

    public void clientTick() {
        previousAngle = angle;
        angle += speed;
        speed *= FRICTION;
        if (Math.abs(speed) < 0.05F) {
            speed = 0.0F;
        }
        if (angle > 360.0F || angle < -360.0F) {
            angle %= 360.0F;
            previousAngle = angle - speed;
        }
    }

    public float getAngle(float partialTicks) {
        return previousAngle + (angle - previousAngle) * partialTicks;
    }
}
