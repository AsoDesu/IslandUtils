package net.asodev.islandutils.resourcepack;

import net.asodev.islandutils.resourcepack.schema.ResourcePack;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResourcePackOptions {

    public static final String islandFolder = FabricLoader.getInstance().getConfigDir() + "/islandutils_resources/";
    public static final String packDataFile = islandFolder + "pack.json";
    public static final String packZip = islandFolder + "island_utils.zip";

    public static ResourcePack data;

    public static void save() throws IOException {
        File folder = new File(islandFolder);
        if (!folder.exists()) folder.mkdir();

        File packData = new File(packDataFile);
        if (!packData.exists()) packData.createNewFile();

        FileOutputStream out = new FileOutputStream(packData);
        out.write(data.toJson().getBytes());
        out.close();
    }

    public static ResourcePack get() throws Exception {
        File packData = new File(packDataFile);
        if (!packData.exists()) return null;

        FileInputStream in = new FileInputStream(packData);
        String json = new String(in.readAllBytes());
        in.close();

        data = ResourcePack.fromJson(json);
        return data;
    }

}
