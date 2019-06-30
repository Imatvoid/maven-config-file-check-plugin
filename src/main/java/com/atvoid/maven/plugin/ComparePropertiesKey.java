package com.atvoid.maven.plugin;

import com.atvoid.maven.plugin.utils.CommonUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class ComparePropertiesKey extends AbstractMojo implements CheckConf {

    public boolean compare(String onlinePath,List<String> onlineFileNames,String offlinePath,List<String> offlineFileNames) {
        InputStream offlineIns = null;
        InputStream onlineIns = null;
        Properties offlineProp = null;
        Properties onlineProp = null;

        boolean hasError = false;

        // 对输入的文件进行循环
        for (int i = 0; i < onlineFileNames.size(); ++i) {
            try {
                onlineIns = new FileInputStream(onlinePath+onlineFileNames.get(i));
                for(String s : offlineFileNames) {
                    if(s.equals(onlineFileNames.get(i))) {
                        offlineIns = new FileInputStream(offlinePath + offlineFileNames.get(i));
                        break;
                    }
                }
                if(offlineIns == null){
                    getLog().error("File does not exists|" + onlineFileNames.get(i));
                    continue;
                }

                // 加载resources目录下的.properties
                offlineProp = new Properties();
                offlineProp.load(offlineIns);

                // 加载online-resources目录下的.properties
                onlineProp = new Properties();
                onlineProp.load(onlineIns);

                Enumeration<?> offlineKeys = offlineProp.propertyNames();


                // 循环offlineResources目录下的.properties所有的key
                while (offlineKeys.hasMoreElements()) {
                    String key = (String) offlineKeys.nextElement();

                    // 如果online-resources对应的.properties文件没有key
                    if (!onlineProp.containsKey(key)) {
                        getLog().error("The key " + key + " was not found in " + onlineFileNames.get(i));
                        hasError = true;
                    }
                }

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

    public void execute() throws MojoExecutionException, MojoFailureException {

    }

    public void compareDir(String offlinePath, String onlinePath) {
        HashSet<String> onlineResourcesSet = new HashSet<String>();
        CommonUtils.findFiles(onlinePath, onlineResourcesSet,"/");

        HashSet<String> offlineResourcesSet = new HashSet<String>();
        CommonUtils.findFiles(offlinePath, offlineResourcesSet,"/");

        for (String e : offlineResourcesSet) {
            if (!onlineResourcesSet.contains(e)) {
                getLog().warn("The file " + e + " does not exist in online-resources!..");
            }
        }

        for (String e : onlineResourcesSet) {
            if (!offlineResourcesSet.contains(e)) {
                getLog().warn("The file " + e + " does not exist in offline-resources!..");
            }
        }



    }


}
