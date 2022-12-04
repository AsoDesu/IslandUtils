package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.resourcepack.ResourcePackOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import static net.asodev.islandutils.resourcepack.ResourcePackOptions.assertIslandFolder;

public class NativeLibrary {

    public static String os = System.getProperty("os.name").toLowerCase();
    public static String architecture = System.getProperty("os.arch").toLowerCase();

    public static File grabDiscordNative() throws Exception {
        String name = "discord_game_sdk";
        String extention;

        if (os.contains("windows")) extention = ".dll";
        else if (os.contains("linux")) extention = ".so";
        else if (os.contains("mac os")) extention = ".dylib";
        else { throw new RuntimeException("Can't get OS type: " + os + ". Discord Presence Disabled."); }

        if (architecture.equals("amd64")) architecture = "x86_64";

        String libIndex = "native/lib/" + architecture + "/" + name+extention;

        assertIslandFolder();
        File outFile = new File(ResourcePackOptions.islandFolder + libIndex);
        if (!outFile.exists()) {
            System.out.println("Extracting Discord Natives.");
            InputStream stream = NativeLibrary.class.getResourceAsStream("/" + libIndex);
            if (stream == null) throw new IOException("Natives couldn't be found in the resources. (" + libIndex + ")");

            outFile.mkdirs();
            Files.copy(stream, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return outFile;
    }

    public static File grabDiscordJNI() throws IOException {
        String name = "discord_game_sdk_jni";
        String osName = os;

        String fileName;

        if(osName.contains("windows")) {
            osName = "windows";
            fileName = name + ".dll";
        }
        else if(osName.contains("linux")) {
            osName = "linux";
            fileName = "lib" + name + ".so";
        }
        else if(osName.contains("mac os")) {
            osName = "macos";
            fileName = "lib" + name + ".dylib";
        }
        else {
            throw new RuntimeException("cannot determine OS type: "+osName);
        }

        if(architecture.equals("x86_64"))
            architecture = "amd64";

        String libIndex = "native/"+osName+"/"+architecture+"/"+fileName;

        File outFile = new File(ResourcePackOptions.islandFolder + libIndex);
        if (!outFile.exists()) {
            InputStream stream = NativeLibrary.class.getResourceAsStream("/" + libIndex);
            if (stream == null) throw new IOException("JNI couldn't be found in the resources. (" + libIndex + ")");

            outFile.mkdirs();
            Files.copy(stream, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return outFile;
    }

}
