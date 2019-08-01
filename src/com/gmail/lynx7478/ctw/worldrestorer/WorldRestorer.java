package com.gmail.lynx7478.ctw.worldrestorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldRestorer extends JavaPlugin 
{
    public void onEnable() {
        final File worldDirectoy = new File(new File(this.getDataFolder().getParent(), "CaptureTheWool"), "Worlds");
        final File backupDirectory = new File(new File(this.getDataFolder().getParent(), "CaptureTheWool"), "WorldBackups");
        if (!backupDirectory.exists()) {
            backupDirectory.mkdir();
        }
        final File[] worldFiles = worldDirectoy.listFiles();
        for (int x = 0; x < worldFiles.length; ++x) {
            final File file = worldFiles[x];
            if (file.isDirectory()) {
                final File bFile = this.searchFolder(backupDirectory, file.getName());
                if (bFile != null) {
                    file.delete();
                    try {
                        this.copy(bFile, file);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bukkit.getLogger().info("[CaptureTheWool] Copying over world \"" + bFile.getName() + "\" from the backup directory.");
                }
                else {
                    try {
                        this.copy(file, new File(backupDirectory, file.getName()));
                        Bukkit.getLogger().info("[CaptureTheWool] Backing up world \"" + file.getName() + "\" in the backups folder.");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private File searchFolder(final File folder, final String nameToFind) {
        File[] listFiles;
        for (int length = (listFiles = folder.listFiles()).length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (file.getName().equals(nameToFind)) {
                return file;
            }
        }
        return null;
    }
    
    void copy(final File source, final File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            final String[] files = source.list();
            String[] array;
            for (int length2 = (array = files).length, i = 0; i < length2; ++i) {
                final String file = array[i];
                final File srcFile = new File(source, file);
                final File destFile = new File(destination, file);
                this.copy(srcFile, destFile);
            }
        }
        else {
            final InputStream in = new FileInputStream(source);
            final OutputStream out = new FileOutputStream(destination);
            final byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

}
