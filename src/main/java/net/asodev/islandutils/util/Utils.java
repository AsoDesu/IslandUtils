package net.asodev.islandutils.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
    public static final ExecutorService savingQueue = Executors.newFixedThreadPool(2);


    public static String readFile(File file) throws Exception {
        if (!file.exists()) return null;

        FileInputStream in = new FileInputStream(file);
        String json = new String(in.readAllBytes());
        in.close();
        return json;
    }

    public static void writeFile(File file, String data) throws IOException {
        if (!file.exists()) file.createNewFile();

        FileOutputStream out = new FileOutputStream(file);
        out.write(data.getBytes());
        out.close();
    }

}
