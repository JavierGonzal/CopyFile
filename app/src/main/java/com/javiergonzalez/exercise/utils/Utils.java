package com.javiergonzalez.exercise.utils;

import com.javiergonzalez.exercise.FoldersFilesListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by javiergonzalezcabezas on 9/7/15.
 */
public class Utils {


    static FoldersFilesListener sListener;

    public static void copyFileOrDirectory(FoldersFilesListener listener, final String srcDir, final String dstDir) {

        sListener = listener;

        new Thread(new Runnable() {
            public void run() {
                copy(srcDir, dstDir);
            }

            private void copy(String srcDir, String dstDir) {
                try {

                    File src = new File(srcDir);
                    File dst = new File(dstDir, src.getName());

                    //Showed directory in Progress
                    sListener.paintFolderFile(srcDir);

                    //Check is a directory or file
                    if (src.isDirectory()) {

                        String files[] = src.list();
                        int filesLength = files.length;
                        for (int i = 0; i < filesLength; i++) {
                            String src1 = (new File(src, files[i]).getPath());
                            String dst1 = dst.getPath();
                            copy(src1, dst1);

                        }
                    } else {
                        copyFile(src, dst);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void copyFile(File sourceFile, File destFile) throws IOException {
                if (!destFile.getParentFile().exists())
                    destFile.getParentFile().mkdirs();

                if (!destFile.exists()) {
                    destFile.createNewFile();
                }

                FileChannel source = null;
                FileChannel destination = null;

                try {
                    source = new FileInputStream(sourceFile).getChannel();
                    destination = new FileOutputStream(destFile).getChannel();
                    destination.transferFrom(source, 0, source.size());

                    // Showed File in progress
                    sListener.paintFolderFile(sourceFile.toString());
                } finally {
                    if (source != null) {
                        source.close();
                    }
                    if (destination != null) {
                        destination.close();
                    }
                }
            }
        }).start();
    }
}