package com.atvoid.maven.plugin.compare;


import com.atvoid.maven.plugin.utils.CommonUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.atvoid.maven.plugin.utils.CommonUtils.removeBackslash;

public class ComparePropertiesKey extends AbstractMojo implements CompareKey {

    public boolean compare(String onlinePath, List<String> onlineFileNames, String offlinePath, List<String> offlineFileNames, Set<String> ignoreKeySet) {
        // 移除路径结尾的/
        onlinePath = removeBackslash(onlinePath);
        offlinePath = removeBackslash(offlinePath);
        getLog().info("online path:" + onlinePath);
        getLog().info("online files:" + onlineFileNames.toString());
        getLog().info("offline path:" + offlinePath);
        getLog().info("offline files:" + offlineFileNames.toString());

        InputStream offlineIns = null;
        InputStream onlineIns = null;
        Properties offlineProp = null;
        Properties onlineProp = null;

        boolean hasError = false;

        // 对输入的文件进行循环
        for (int i = 0; i < onlineFileNames.size(); ++i) {
            try {
                getLog().info("检验online文件开始 onlinePath:" + onlineFileNames.get(i));
                onlineIns = new FileInputStream(onlinePath + onlineFileNames.get(i));

                String offlineFileName = "";
                for (String s : offlineFileNames) {
                    if (s.equals(onlineFileNames.get(i))) {
                        offlineFileName = s;
                        getLog().info("查找对应的offline文件:" + offlineFileName);
                        offlineIns = new FileInputStream(offlinePath + offlineFileName);
                        break;
                    }
                }
                if (offlineIns == null) {
                    getLog().error("online File does not exists in offlinePath:" + onlineFileNames.get(i) + "已忽略");
                    continue;
                }


                // 加载online-resources目录下的.properties
                onlineProp = new Properties();
                onlineProp.load(onlineIns);
                //getLog().info(onlineProp.toString());

                // 加载offline-resources目录下的.properties
                offlineProp = new Properties();
                offlineProp.load(offlineIns);
                //getLog().info(offlineProp.toString());

                Enumeration<?> onlineKeys = onlineProp.propertyNames();


                // 循环onlineResources目录下的.properties所有的key
                while (onlineKeys.hasMoreElements()) {
                    String key = (String) onlineKeys.nextElement();

                    // 如果offline-resources对应的.properties文件没有key
                    if (!offlineProp.containsKey(key)) {
                        if (ignoreKeySet.contains(key)) {
                            getLog().warn("The online key " + key + " was not found in offlinePath" + offlineFileName);
                            getLog().info("The online key " + key + " is in ignore list");
                        } else {
                            getLog().error("The online key " + key + " was not found in offlinePath" + offlineFileName);
                            hasError = true;
                        }
                    }
                }
                getLog().info("检验online文件结束 onlinePath:" + onlineFileNames.get(i));

            } catch (FileNotFoundException e) {
                getLog().error("File does not exists|" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (offlineIns != null) {
                    try {
                        offlineIns.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (onlineIns != null) {
                    try {
                        onlineIns.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return hasError;

    }


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

    }
}




