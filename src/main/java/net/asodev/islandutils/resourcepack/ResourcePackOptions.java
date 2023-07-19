package net.asodev.islandutils.resourcepack;

import net.asodev.islandutils.resourcepack.schema.ResourcePack;
import net.asodev.islandutils.util.Utils;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public class ResourcePackOptions {

    public static final String islandFolder = FabricLoader.getInstance().getConfigDir() + "/islandutils_resources/";
    public static final String packDataFile = islandFolder + "pack.json";
    public static final String packZip = islandFolder + "island_utils.zip";

    public static ResourcePack data;

    public static void save() throws IOException {
        Utils.assertIslandFolder();
        Utils.writeFile(new File(packDataFile), data.toJson());
    }

    public static ResourcePack get() throws Exception {
        File packData = new File(packDataFile);
        String json = Utils.readFile(packData);

        data = ResourcePack.fromJson(json);
        return data;
    }

}
