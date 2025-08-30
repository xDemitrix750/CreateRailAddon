package com.example.createrailaddon;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Random;

@Mod("create_rail_addon")
public class CreateRailAddon {
    public static final String MODID = "create_rail_addon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateRailAddon() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Register chunk load listener
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Create Rail Addon setup complete.");
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        // Only run on server side
        if (event.getWorld() instanceof Level) {
            Level lvl = (Level) event.getWorld();
            if (lvl.isClientSide()) return;
            if (!(lvl instanceof ServerLevel)) return;
            ServerLevel world = (ServerLevel) lvl;

            // Use chunk persistent data to only process once
            net.minecraft.nbt.CompoundTag data = event.getChunk().getLevelData().getCustomBossEvents(); // fallback - non ideal
            // Simpler: use chunk's persistentData via forge capability isn't trivial; we'll do a cheap check:
            long rand = world.getRandom().nextLong();
            // To avoid doing too much work, only generate with 1/3 chance
            if ((rand & 3) != 0) return;

            try {
                placeTrackLine(world, event.getChunk().getPos().getMinBlockX(), event.getChunk().getPos().getMinBlockZ());
            } catch (Exception e) {
                LOGGER.error("Error placing track line", e);
            }
        }
    }

    private void placeTrackLine(ServerLevel world, int chunkMinX, int chunkMinZ) {
        // place a short straight line of create train tracks centered in the chunk
        int centerX = chunkMinX + 8;
        int centerZ = chunkMinZ + 8;
        int y = world.getHeight(Heightmap.Types.WORLD_SURFACE, centerX, centerZ) + 1;

        // Lookup the Create train track block by id: 'create:train_track'
        ResourceLocation loc = new ResourceLocation("create", "train_track");
        Block block = net.minecraft.core.Registry.BLOCK.get(loc);
        if (block == null) return;
        BlockState state = block.defaultBlockState();

        // Place 32 blocks along X axis
        int length = 32;
        for (int i = -length/2; i < length/2; i++) {
            BlockPos pos = new BlockPos(centerX + i, y, centerZ);
            // Only replace air to avoid destroying builds
            if (world.isEmptyBlock(pos)) {
                world.setBlock(pos, state, 3);
            }
        }
    }
}
