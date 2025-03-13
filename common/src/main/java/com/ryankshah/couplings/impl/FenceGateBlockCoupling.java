package com.ryankshah.couplings.impl;

import com.ryankshah.couplings.CouplingsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public final class FenceGateBlockCoupling {
    private FenceGateBlockCoupling() {}

    public static void used(
            final BlockState state, final Level level, final BlockPos pos, final Player player) {
        if (CouplingsCommon.couplesFenceGates(level)
                && (!player.isCrouching() || CouplingsCommon.ignoresSneaking(player))) {
            tryOpenCloseEach(state, level, pos, player, state.getValue(FenceGateBlock.OPEN));
        } else {
        }
    }

    public static void neighborChanged(
            final BlockState state, final Level level, final BlockPos pos, final boolean powered) {

        if (CouplingsCommon.couplesFenceGates(level)
                && (!powered || (level.getBestNeighborSignal(pos) >= CouplingsCommon.COUPLING_SIGNAL))) {
            tryOpenCloseEach(state, level, pos, null, powered);
        } else {
        }
    }

    public static void tryOpenCloseEach(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final @Nullable Player player,
            final boolean open) {

        final var axis = state.getValue(HorizontalDirectionalBlock.FACING).getAxis();
        final var distance = CouplingsCommon.COUPLING_DISTANCE;
        var continueUp = true;
        var continueDown = true;

        for (var offset = 1; (offset <= distance) && (continueUp || continueDown); ++offset) {
            if (continueUp) {
                final var above = pos.above(offset);

                continueUp =
                        ((player == null) || level.mayInteract(player, above))
                                && tryOpenClose(state, level, above, player, axis, open);
            }

            if (continueDown) {
                final var below = pos.below(offset);

                continueDown =
                        ((player == null) || level.mayInteract(player, below))
                                && tryOpenClose(state, level, below, player, axis, open);
            }
        }
    }

    private static boolean tryOpenClose(
            final BlockState state,
            final Level level,
            final BlockPos offset,
            final @Nullable Player player,
            final Axis axis,
            final boolean open) {

        final var other = level.getBlockState(offset);

        boolean sameBlock = state.getBlock() == other.getBlock();
        boolean differentOpenState = false;
        boolean sameAxis = false;

        if (sameBlock && other.hasProperty(FenceGateBlock.OPEN)) {
            differentOpenState = open != other.getValue(FenceGateBlock.OPEN);

            if (other.hasProperty(HorizontalDirectionalBlock.FACING)) {
                sameAxis = axis == other.getValue(HorizontalDirectionalBlock.FACING).getAxis();
            }
        }

        if (sameBlock && differentOpenState && sameAxis) {
            final var newOther = other.setValue(FenceGateBlock.OPEN, open);

            if (player != null) {
                final var facing = state.getValue(HorizontalDirectionalBlock.FACING);
                level.setBlock(offset, newOther.setValue(HorizontalDirectionalBlock.FACING, facing), 2);
            } else {
                level.setBlock(offset, newOther, 2);
            }

            level.gameEvent(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);

            return true;
        }

        return false;
    }
}