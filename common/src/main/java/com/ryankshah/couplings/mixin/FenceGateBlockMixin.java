package com.ryankshah.couplings.mixin;

import com.ryankshah.couplings.impl.FenceGateBlockCoupling;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FenceGateBlock.class)
abstract class FenceGateBlockMixin extends HorizontalDirectionalBlock {
    FenceGateBlockMixin(final Properties properties) {
        super(properties);
    }

    @Inject(
            method =
                    "useWithoutItem("
                            + "Lnet/minecraft/world/level/block/state/BlockState;"
                            + "Lnet/minecraft/world/level/Level;"
                            + "Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/entity/player/Player;"
                            + "Lnet/minecraft/world/phys/BlockHitResult;"
                            + ")Lnet/minecraft/world/InteractionResult;",
            require = 1,
            allow = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", //Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;)V",
                    shift = At.Shift.AFTER))
    private void used(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Player player,
            final BlockHitResult hit,
            final CallbackInfoReturnable<InteractionResult> cir) {
        BlockState updatedState = level.getBlockState(pos);
        FenceGateBlockCoupling.used(updatedState, level, pos, player);
    }

    @Inject(
            method =
                    "neighborChanged("
                            + "Lnet/minecraft/world/level/block/state/BlockState;"
                            + "Lnet/minecraft/world/level/Level;"
                            + "Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/Block;"
                            + "Lnet/minecraft/core/BlockPos;"
                            + "Z"
                            + ")V",
            require = 1,
            allow = 1,
            at =
            @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/level/Level;"
                                    + "setBlock("
                                    + "Lnet/minecraft/core/BlockPos;"
                                    + "Lnet/minecraft/world/level/block/state/BlockState;"
                                    + "I"
                                    + ")Z"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void neighborChanged(
            final BlockState state,
            final Level world,
            final BlockPos pos,
            final Block block,
            final BlockPos offset,
            final boolean moved,
            final CallbackInfo ci,
            final boolean powered) {
        FenceGateBlockCoupling.neighborChanged(state, world, pos, powered);
    }
}