package net.indeterminance.expertdimtravel.event;


import com.mojang.datafixers.util.Pair;
import net.indeterminance.expertdimtravel.ExpertDimTravel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.indeterminance.expertdimtravel.utils.EventHelper;

import java.util.Optional;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = ExpertDimTravel.MOD_ID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
            ServerLevel serverLevel = EventHelper.getServerLevelFromEvent(event);
            BlockPos blockPos = event.getPos();
            // Get HolderSet for ruined portals
            StructureManager structureManager = serverLevel.structureManager();
            Optional<HolderSet.Named<Structure>> holderSet = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(StructureTags.RUINED_PORTAL);
            if (!holderSet.isPresent()) {
                event.setCanceled(true);
                return;
            }

            // Check if event occurs inside a ruined portal structure
            Pair<BlockPos, Holder<Structure>> pair = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, holderSet.get(), blockPos, 50, false);
            Structure portal = pair.getSecond().get();
            if (!structureManager.getStructureAt(blockPos, portal).isValid()) {
                event.setCanceled(true);
            }
        }
    }
}
