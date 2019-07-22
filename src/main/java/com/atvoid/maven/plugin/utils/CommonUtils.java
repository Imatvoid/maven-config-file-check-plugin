package com.atvoid.maven.plugin.utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;


public class CommonUtils {
    private static Log log;


    public static void setLog(Log log) {
        CommonUtils.log = log;
    }

    /**
     * 判断输入的路径是否是合法目录
     *
     * @param paths 输入的路径
     * @return
     */
    public static boolean isDirectory(String... paths) {


        if (paths == null || paths.length == 0) {
            log.info("输入路径为空");
            return false;
        }

        for (String e : paths) {
            log.info("输入路径:" + e);
            File resources = new File(e);
            if (!resources.isDirectory()) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param currentDirUsed     搜索目录
     * @param currentFileNameSet 返回的所有文件
     * @param pathPrefix         起始的路径前缀
     */
    public static void findFiles(String currentDirUsed, HashSet<String> currentFileNameSet, String pathPrefix) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * 如果目录则递归继续遍历
                 */
                findFiles(file.getAbsolutePath(), currentFileNameSet, pathPrefix + file.getName() + "/");
            } else {
                /**
                 * 如果不是目录,直接添加
                 */
                currentFileNameSet.add(pathPrefix + file.getName());

            }
        }
    }

    /**
     * 在特定目录用文件名查找
     *
     * @param currentDirUsed      搜索目录
     * @param fileName            要搜寻的文件名
     * @param currentFilenameList 返回的匹配结果
     * @param pathPrefix          起始的路径前缀
     */
    public static void findFilesWithName(String currentDirUsed, String fileName, List<String> currentFilenameList, String pathPrefix, String fileType) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * 如果目录则递归继续遍历
                 */
                findFilesWithName(file.getAbsolutePath(), fileName, currentFilenameList, pathPrefix + file.getName() + "/", fileType);
            } else {

                //如果不是目录,直接添加
                if (StringUtils.isBlank(fileType) && file.getName().equals(fileName) && file.getName().endsWith(fileType)) {
                    currentFilenameList.add(pathPrefix + file.getName());
                    return;
                }

                if (file.getName().equals(fileName)) {
                    currentFilenameList.add(pathPrefix + file.getName());
                }
            }

        }
    }


    /**
     * 寻找指定目录下，具有指定后缀名的所有文件。
     *
     * @param filenameSuffix      文件后缀名
     * @param currentDirUsed      搜索目录
     * @param currentFilenameList 返回的结果
     * @param pathPrefix          路径前缀
     */
    public static void findFilesWithSuffix(String filenameSuffix, String currentDirUsed,
                                           List<String> currentFilenameList, String pathPrefix) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * 如果目录则递归继续遍历
                 */
                findFilesWithSuffix(filenameSuffix, file.getAbsolutePath(), currentFilenameList, pathPrefix + file.getName() + "/");
            } else {
                /**
                 * 如果不是目录。
                 * 那么判断文件后缀名是否符合。
                 */
                if (file.getAbsolutePath().endsWith(filenameSuffix)) {
                    currentFilenameList.add(pathPrefix + file.getName());
                }
            }
        }
    }


    /**
     * 文件存在于resource而不存在于online-resources，则警告输出
     *
     * @param offlinePath
     * @param onlinePath
     */
    public static void compareDir(String offlinePath, String onlinePath) {
        HashSet<String> onlineResourcesSet = new HashSet<String>();
        CommonUtils.findFiles(onlinePath, onlineResourcesSet, "/");

        HashSet<String> offlineResourcesSet = new HashSet<String>();
        CommonUtils.findFiles(offlinePath, offlineResourcesSet, "/");

        for (String e : offlineResourcesSet) {
            if (!onlineResourcesSet.contains(e)) {
                log.warn("The file " + e + " does not exist in online-resources!..");
            }
        }

        for (String e : onlineResourcesSet) {
            if (!offlineResourcesSet.contains(e)) {
                log.warn("The file " + e + " does not exist in offline-resources!..");
            }
        }


    }

    public static String removeBackslash(String t) {

        if (t.endsWith("/") || t.endsWith("\\")) {
            return t.substring(0, t.length() - 1);
        }
        return t;
    }


}
