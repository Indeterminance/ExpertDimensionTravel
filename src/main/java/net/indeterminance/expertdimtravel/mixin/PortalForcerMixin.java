package net.indeterminance.expertdimtravel.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
    @Unique
    private final Random expertdimtravel$RNG = new Random();

    @ModifyArg(
            method = "createPortal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"),
            index = 1
    )
    public BlockState createPortal(BlockState origBlockState) {
        if (origBlockState.is(Blocks.NETHER_PORTAL)) return Blocks.AIR.defaultBlockState();
        else if (origBlockState.is(Blocks.OBSIDIAN)) {
            int randInt = expertdimtravel$RNG.nextInt(5);
            if (randInt == 0) return Blocks.AIR.defaultBlockState();
            if (randInt < 3) return Blocks.CRYING_OBSIDIAN.defaultBlockState();
            return origBlockState;
        }
        return origBlockState;
    }
}
