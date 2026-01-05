package net.indeterminance.expertdimtravel.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.level.BlockEvent;

public class EventHelper {
    /*
        This will only be called via onPortalSpawn, which only handles
        Overworld/Nether events. As such, we shall only consider these cases.
     */
    public static ServerLevel getServerLevelFromEvent(BlockEvent event) {
        DimensionType dimensionType = event.getLevel().dimensionType();
        RegistryAccess registry = event.getLevel().registryAccess();
        ResourceLocation resource = registry.registryOrThrow(Registries.DIMENSION_TYPE).getKey(dimensionType);
        if (resource == null) return null;

        String dim_path = resource.getPath();
        MinecraftServer server = event.getLevel().getServer();
        if (server == null) return null;

        if (dim_path == "overworld") {
            return server.overworld();
        }
        else if (dim_path == "the_nether") {
            return server.getLevel(Level.NETHER);
        }
        else return null;
    }
}
