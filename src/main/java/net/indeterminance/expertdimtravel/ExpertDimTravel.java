package net.indeterminance.expertdimtravel;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ExpertDimTravel.MOD_ID)
public class ExpertDimTravel
{
    public static final String MOD_ID = "expertdimtravel";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExpertDimTravel(FMLJavaModLoadingContext context)
    {
        //IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
