package nex.world.biome;

import com.google.common.collect.ImmutableList;
import lex.LibEx;
import lex.config.FileConfig;
import lex.config.IConfig;
import lex.util.ConfigHelper;
import lex.util.FileHelper;
import lex.world.biome.BiomeWrapper;
import lex.world.biome.IBiomeWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import nex.NetherEx;
import nex.handler.ConfigHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class NetherExBiomeManager
{
    private static final List<Biome> BIOMES = new ArrayList<>();
    private static final Map<Biome, IBiomeWrapper> BIOME_WRAPPERS = new HashMap<>();

    public static void preInit()
    {
        FileHelper.copyDirectoryToDirectory(new File(NetherEx.class.getResource("/assets/nex/biome_configs").getFile()), new File(LibEx.CONFIG_DIRECTORY, "NetherEx/Biomes"));
    }

    public static void setupDefaultBiomes()
    {
        NetherEx.LOGGER.info("Setting up default biomes.");
        parseBiomeConfigs(new File(LibEx.CONFIG_DIRECTORY, "NetherEx/Biomes/nex"));
    }

    public static void setupCompatibleBiomes(MinecraftServer server)
    {
        World world = server.getEntityWorld();

        if(Loader.isModLoaded("biomesoplenty") && ConfigHandler.compatibilityConfig.biomesOPlenty.enableCompat)
        {
            WorldType worldType = world.getWorldType();

            if(worldType.getName().equalsIgnoreCase("BIOMESOP") || worldType.getName().equalsIgnoreCase("lostcities_bop"))
            {
                NetherEx.LOGGER.info("Setting up Biomes O' Plenty biomes.");
                parseBiomeConfigs(new File(LibEx.CONFIG_DIRECTORY, "NetherEx/Biomes/biomesoplenty"));
            }
        }
    }

    public static void setupCustomBiomes()
    {
        NetherEx.LOGGER.info("Setting up custom biomes.");
        parseBiomeConfigs(new File(LibEx.CONFIG_DIRECTORY, "NetherEx/Biomes/custom"));
    }

    private static void parseBiomeConfigs(File directory)
    {
        if(!directory.exists())
        {
            directory.mkdirs();
        }

        try
        {
            Iterator<Path> pathIter = Files.walk(directory.toPath()).iterator();

            while(pathIter.hasNext())
            {
                Path configPath = pathIter.next();
                File configFile = configPath.toFile();

                if(FileHelper.getFileExtension(configFile).equals("json"))
                {
                    wrapBiome(new FileConfig(configFile), configFile);
                }
                else if(!configFile.isDirectory())
                {
                    NetherEx.LOGGER.warn("Skipping file located at, {}, as it is not a json file.", configPath.toString());
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void wrapBiome(IConfig config, File configFile)
    {
        IBiomeWrapper wrapper = new BiomeWrapper(config);

        if(wrapper.getBiome() != null)
        {
            BIOMES.add(wrapper.getBiome());
            BIOME_WRAPPERS.put(wrapper.getBiome(), wrapper);
            ConfigHelper.saveConfig(config, configFile);
        }
    }

    public static void clearBiomes()
    {
        BIOMES.clear();
        BIOME_WRAPPERS.clear();
    }

    public static List<Biome> getBiomes()
    {
        return ImmutableList.copyOf(BIOMES);
    }

    public static IBiomeWrapper getBiomeWrapper(Biome key)
    {
        return BIOME_WRAPPERS.get(key);
    }
}
