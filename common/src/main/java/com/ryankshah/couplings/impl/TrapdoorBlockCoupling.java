package com.ryankshah.couplings.impl;

import com.ryankshah.couplings.CouplingsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public final class TrapdoorBlockCoupling {
    private TrapdoorBlockCoupling() {}

    public static void used(
            final BlockState state, final Level level, final BlockPos pos, final Player player) {
        if (CouplingsCommon.couplesTrapdoors(level)
                && (!player.isCrouching() || CouplingsCommon.ignoresSneaking(player))) {
            tryOpenCloseEach(state, level, pos, player, state.getValue(TrapDoorBlock.OPEN));
        }
    }

    public static void neighborChanged(
            final BlockState state, final Level level, final BlockPos pos, final boolean powered) {
        if (CouplingsCommon.couplesTrapdoors(level)
                && (!powered || (level.getBestNeighborSignal(pos) >= CouplingsCommon.COUPLING_SIGNAL))) {
            tryOpenCloseEach(state, level, pos, null, powered);
        }
    }

    private static void tryOpenCloseEach(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final @Nullable Player player,
            final boolean open) {
        final var half = state.getValue(TrapDoorBlock.HALF);
        final var facing = state.getValue(HorizontalDirectionalBlock.FACING);
        final var traverseZ = facing.getAxis() == Axis.X;
        final var offset = (facing.getAxisDirection() == AxisDirection.POSITIVE) ? 1 : -1;
        final var distance = CouplingsCommon.COUPLING_DISTANCE;
        var continuePos = true;
        var continueNeg = true;

        for (var step = 0; (step <= distance) && (continuePos || continueNeg); ++step) {
            if (continuePos) {
                var relative = pos;

                if (step != 0) {
                    relative = pos.offset(traverseZ ? 0 : step, 0, traverseZ ? step : 0);

                    continuePos =
                            ((player == null) || level.mayInteract(player, relative))
                                    && tryOpenClose(state, relative, level, player, facing, half, open);
                }

                if (continuePos) {
                    tryOpenCloseAt(state, level, player, open, half, facing, traverseZ, offset, relative);
                }
            }

            if (continueNeg && (step != 0)) {
                final var relative = pos.offset(traverseZ ? 0 : -step, 0, traverseZ ? -step : 0);

                continueNeg =
                        ((player == null) || level.mayInteract(player, relative))
                                && tryOpenClose(state, relative, level, player, facing, half, open);

                if (continueNeg) {
                    tryOpenCloseAt(state, level, player, open, half, facing, traverseZ, offset, relative);
                }
            }
        }
    }

    private static void tryOpenCloseAt(
            final BlockState state,
            final Level level,
            final @Nullable Player player,
            final boolean open,
            final Half half,
            final Direction facing,
            final boolean traverseZ,
            final int offset,
            final BlockPos pos) {
        final var relative = pos.offset(traverseZ ? offset : 0, 0, traverseZ ? 0 : offset);

        if ((player == null) || level.mayInteract(player, relative)) {
            tryOpenClose(state, relative, level, player, facing.getOpposite(), half, open);
        }
    }

    private static boolean tryOpenClose(
            final BlockState state,
            final BlockPos pos,
            final Level level,
            final @Nullable Player player,
            final Direction facing,
            final Half half,
            final boolean open) {
        final var other = level.getBlockState(pos);

        if ((state.getBlock() == other.getBlock())
                && (facing == other.getValue(HorizontalDirectionalBlock.FACING))) {
            if ((half == other.getValue(TrapDoorBlock.HALF))
                    && (open != other.getValue(TrapDoorBlock.OPEN))) {
                level.setBlock(pos, other.setValue(TrapDoorBlock.OPEN, open), 2);

                if (other.getValue(TrapDoorBlock.WATERLOGGED)) {
                    level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                }

                level.gameEvent(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

                return true;
            }
        }

        return false;
    }
}