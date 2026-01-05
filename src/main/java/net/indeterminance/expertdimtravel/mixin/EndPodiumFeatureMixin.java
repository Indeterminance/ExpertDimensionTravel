package net.indeterminance.expertdimtravel.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
    We can get rid of the exit portal by modifying the end podium.
    While we're here, we may as well restructure the end podium to reflect that!
 */
@Mixin(EndPodiumFeature.class)
public abstract class EndPodiumFeatureMixin extends Feature<NoneFeatureConfiguration> {
    public EndPodiumFeatureMixin(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Inject(
            method = "place",
            at = @At("HEAD"),
            cancellable = true
    )
    public void place(FeaturePlaceContext<NoneFeatureConfiguration> feature, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockpos = feature.origin();
        WorldGenLevel worldgenlevel = feature.level();

        for(BlockPos blockpos1 : BlockPos.betweenClosed(new BlockPos(blockpos.getX() - 3, blockpos.getY() - 1, blockpos.getZ() - 3), new BlockPos(blockpos.getX() + 3, blockpos.getY() + 32, blockpos.getZ() + 3))) {
            boolean flag = blockpos1.closerThan(blockpos, 2.5D);
            if (flag || blockpos1.closerThan(blockpos, 3.5D)) {
                if (blockpos1.distManhattan(blockpos.below()) < 3) {
                    this.setBlock(worldgenlevel, blockpos1, Blocks.BEDROCK.defaultBlockState());
                } else if (blockpos1.getY() < blockpos.getY()) {
                    this.setBlock(worldgenlevel, blockpos1, Blocks.END_STONE.defaultBlockState());
                } else {
                    this.setBlock(worldgenlevel, new BlockPos(blockpos1), Blocks.AIR.defaultBlockState());
                }
            }
        }

        for(int i = 0; i < 4; ++i) {
            this.setBlock(worldgenlevel, blockpos.above(i), Blocks.BEDROCK.defaultBlockState());
        }

        BlockPos blockpos2 = blockpos.above(2);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            this.setBlock(worldgenlevel, blockpos2.relative(direction), Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, direction));
        }

        cir.setReturnValue(true);
    }
}
