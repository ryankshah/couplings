package com.ryankshah.couplings.impl;

import com.ryankshah.couplings.CouplingsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.gameevent.GameEvent;

public final class DoorBlockCoupling {
    private DoorBlockCoupling() {}

    public static void used(
            final BlockState state, final Level level, final BlockPos pos, final Player player) {
        if (CouplingsCommon.couplesDoors(level)
                && (!player.isCrouching() || CouplingsCommon.ignoresSneaking(player))) {
            final var offset = getCoupledDoorPos(state, pos);

            if (level.mayInteract(player, offset)) {
                final var other = level.getBlockState(offset);

                if (state.getBlock() == other.getBlock()) {
                    final var open = state.getValue(DoorBlock.OPEN);

                    if (areCoupled(state, other, open)) {
                        level.setBlock(offset, other.setValue(DoorBlock.OPEN, open), 2);
                        level.gameEvent(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);
                    }
                }
            }
        }
    }

    public static void openStateChanged(
            final Entity entity,
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final boolean open) {
        if (CouplingsCommon.couplesDoors(level)) {
            final var offset = getCoupledDoorPos(state, pos);
            final var other = level.getBlockState(offset);

            if ((state.getBlock() == other.getBlock()) && areCoupled(state, other, open)) {
                level.setBlock(offset, other.setValue(DoorBlock.OPEN, open), 10);
                level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);
            }
        }
    }

    public static void neighborChanged(
            final BlockState state, final Level level, final BlockPos pos, final boolean powered) {
        if (CouplingsCommon.couplesDoors(level)
                && (!powered || (level.getBestNeighborSignal(pos) >= CouplingsCommon.COUPLING_SIGNAL))) {
            final var offset = getCoupledDoorPos(state, pos);
            final var other = level.getBlockState(offset);

            if ((state.getBlock() == other.getBlock()) && areCoupled(state, other, powered)) {
                level.setBlock(offset, other.setValue(DoorBlock.OPEN, powered), 2);
                level.gameEvent(null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);
            }
        }
    }

    private static boolean areCoupled(
            final BlockState self, final BlockState other, final boolean open) {
        return (open != other.getValue(DoorBlock.OPEN))
                && (self.getValue(DoorBlock.FACING) == other.getValue(DoorBlock.FACING))
                && (self.getValue(DoorBlock.HALF) == other.getValue(DoorBlock.HALF))
                && (self.getValue(DoorBlock.HINGE) != other.getValue(DoorBlock.HINGE));
    }

    private static BlockPos getCoupledDoorPos(final BlockState state, final BlockPos pos) {
        final var facing = state.getValue(DoorBlock.FACING);
        final var leftHinge = state.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT;

        return pos.relative(leftHinge ? facing.getClockWise() : facing.getCounterClockWise());
    }
}