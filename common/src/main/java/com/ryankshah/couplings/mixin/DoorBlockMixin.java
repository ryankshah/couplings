package com.ryankshah.couplings.mixin;

import com.ryankshah.couplings.impl.DoorBlockCoupling;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DoorBlock.class)
abstract class DoorBlockMixin extends Block {
    DoorBlockMixin(final Properties properties) {
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
            require = 1,
            allow = 1)
    private void usedWithoutItem(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Player player,
            final BlockHitResult hit,
            final CallbackInfoReturnable<InteractionResult> cir) {
        DoorBlockCoupling.used(state, level, pos, player);
    }

    @Inject(
            method =
                    "setOpen("
                            + "Lnet/minecraft/world/entity/Entity;"
                            + "Lnet/minecraft/world/level/Level;"
                            + "Lnet/minecraft/world/level/block/state/BlockState;"
                            + "Lnet/minecraft/core/BlockPos;"
                            + "Z"
                            + ")V",
            at =
            @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/level/block/DoorBlock;"
                                    + "playSound("
                                    + "Lnet/minecraft/world/entity/Entity;"
                                    + "Lnet/minecraft/world/level/Level;"
                                    + "Lnet/minecraft/core/BlockPos;"
                                    + "Z"
                                    + ")V"),
            require = 1,
            allow = 1)
    private void openStateChanged(
            final Entity entity,
            final Level level,
            final BlockState state,
            final BlockPos pos,
            final boolean open,
            final CallbackInfo ci) {
        DoorBlockCoupling.openStateChanged(entity, state, level, pos, open);
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
            require = 1,
            allow = 1,
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void neighborChanged(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Block block,
            final BlockPos offset,
            final boolean moved,
            final CallbackInfo ci,
            final boolean powered) {
        DoorBlockCoupling.neighborChanged(state, level, pos, powered);
    }
}