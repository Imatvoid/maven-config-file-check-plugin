package com.atvoid.maven.plugin.utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public class CommonUtils {

    /**
     * 判断输入的路径是否是合法目录
     *
     * @param paths 输入的路径
     * @return
     */
    public static boolean isDirectory(String... paths) {
        if (paths == null || paths.length == 0) {
            return false;
        }

        for (String e : paths) {
            File resources = new File(e);
            if (!resources.isDirectory()) {
                return false;
            }
        }

        return true;
    }


    public static void findFiles(String currentDirUsed, HashSet<String> currentFileNameSet, String relativePath) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * 如果目录则递归继续遍历
                 */
                findFiles(file.getAbsolutePath(), currentFileNameSet, relativePath + file.getName() + "/");
            } else {
                /**
                 * 如果不是目录,直接添加
                 */
                currentFileNameSet.add(relativePath + file.getName());

            }
        }
    }

    public static void findFilesWithName(String currentDirUsed, String fileName, List<String> currentFilenameList, String relativePath) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * 如果目录则递归继续遍历
                 */
                findFilesWithName(file.getAbsolutePath(), fileName, currentFilenameList, relativePath + file.getName() + "/");
            } else {
                /**
                 * 如果不是目录,直接添加
                 */
                if (file.getName().equals(fileName)) {
                    currentFilenameList.add(relativePath + file.getName());
                }

            }
        }
    }

    /**
     * 寻找指定目录下，具有指定后缀名的所有文件。
     *
     * @param filenameSuffix      : 文件后缀名
     * @param currentDirUsed      : 当前使用的文件目录
     * @param currentFilenameList ：当前文件名称的列表
     */
    public static void findFilesWithSuffix(String filenameSuffix, String currentDirUsed,
                                           List<String> currentFilenameList, String relativePath) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * 如果目录则递归继续遍历
                 */
                findFilesWithSuffix(filenameSuffix, file.getAbsolutePath(), currentFilenameList,relativePath + file.getName() + "/");
            } else {
                /**
                 * 如果不是目录。
                 * 那么判断文件后缀名是否符合。
                 */
                if (file.getAbsolutePath().endsWith(filenameSuffix)) {
                    currentFilenameList.add(relativePath + file.getName());
                }
            }
        }
    }


}
