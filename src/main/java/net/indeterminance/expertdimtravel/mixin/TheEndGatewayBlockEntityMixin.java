package net.indeterminance.expertdimtravel.mixin;

import net.indeterminance.expertdimtravel.ExpertDimTravel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TheEndGatewayBlockEntity.class)
public abstract class TheEndGatewayBlockEntityMixin {

    /*
        End Gateways spawned within 700 blocks are almost certainly either on the main island or artificial,
        and those should maintain default behaviour. Gateways outside that bound are likely return gateways,
        since those generate at a minimum of 768(?) blocks away from the origin.
     */
    @Redirect(
            method = "teleportEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;teleportToWithTicket(DDD)V"
            )
    )
    private static void redirectTeleportWithTicket(Entity entity, double x, double y, double z) {
        ExpertDimTravel.LOGGER.debug("Touched gateway!");
        if (entity.position().horizontalDistance() < 700d) {
            ExpertDimTravel.LOGGER.debug("Default teleport");
            entity.teleportToWithTicket(x,y,z);
        }
        else if (entity.level().dimension() == Level.END) {
            ExpertDimTravel.LOGGER.debug("End escape!");
            ServerLevel overworld = entity.getServer().overworld();
            entity.changeDimension(overworld);
        }
    }

    // Suppress the spawn of quick-return gateways. If you want to escape the End, go exploring!
    @Redirect(
            method = "teleportEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/TheEndGatewayBlockEntity;spawnGatewayPortal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/EndGatewayConfiguration;)V"
            )
    )
    private static void spawnGatewayPortal(ServerLevel p_155822_, BlockPos p_155823_, EndGatewayConfiguration p_155824_) {
        ExpertDimTravel.LOGGER.debug("Prevented return gateway from spawning!");
    }
}
