package com.atvoid.maven.plugin.compare;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.atvoid.maven.plugin.utils.CommonUtils.removeBackslash;

public class CompareYmlKey extends AbstractMojo implements CompareKey {

    Yaml yaml = new Yaml();

    @Override
    public boolean compare(String onlinePath, List<String> onlineFileNames, String offlinePath, List<String> offlineFileNames,Set<String> ignoreKeySet) {
        // 移除路径结尾的/
        onlinePath = removeBackslash(onlinePath);
        offlinePath = removeBackslash(offlinePath);
        getLog().info("online path:" + onlinePath);
        getLog().info("online files:" + onlineFileNames.toString());
        getLog().info("offline path:" + offlinePath);
        getLog().info("offline files:" + offlineFileNames.toString());

        InputStream offlineIns = null;
        InputStream onlineIns = null;


        boolean hasError = false;

        // 对输入的文件进行循环
        for (int i = 0; i < onlineFileNames.size(); ++i) {
            try {
                getLog().info("检验online文件 onlinePath:" + onlineFileNames.get(i));
                onlineIns = new FileInputStream(onlinePath + onlineFileNames.get(i));

                String offlineFileName = "";
                for (String s : offlineFileNames) {
                    if (s.equals(onlineFileNames.get(i))) {
                        offlineFileName = s;
                        getLog().info("查找到offline文件::" + offlinePath + offlineFileName);
                        offlineIns = new FileInputStream(offlinePath + offlineFileName);
                        break;
                    }
                }
                if (offlineIns == null) {
                    getLog().error("online File does not exists in offlinePath |" + offlineFileName);
                    continue;
                }

                // 加载online-resources目录下的.yml
                Map<String, Object> onlineMap = (Map<String, Object>) yaml.load(onlineIns);
                HashSet<String> onlineSet = new HashSet<>();
                convertKeyMap(onlineMap, onlineSet, "");


                // 加载offline-resources目录下的.properties
                Map<String, Object> offlineMap = (Map<String, Object>) yaml.load(offlineIns);
                HashSet<String> offlineSet = new HashSet<>();
                convertKeyMap(offlineMap, offlineSet, "");

                // 循环online-file.yml所有的key
                if (onlineSet.size() > 0) {
                    for (String key : onlineSet) {

                        // 如果对应的offline-file.yml没有key
                        if (!offlineSet.contains(key)) {

                            if (ignoreKeySet.contains(key)) {
                                getLog().warn("The online key " + key + " was not found in offlinePath" + offlineFileName);
                                getLog().info("The online key " + key + " is in ignore list");
                            } else {
                                getLog().error("The online key " + key + " was not found in offlinePath" + offlineFileName);
                                hasError = true;
                            }
                        }
                    }
                }
                getLog().info("检验结束onlinePath:" + onlineFileNames.get(i));

            } catch (FileNotFoundException e) {
                getLog().error("File does not exists|" + e.getMessage());
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

    public static void convertKeyMap(Map<String, Object> original, HashSet<String> set, String prefix) {

        for (Map.Entry<String, Object> entry : original.entrySet()) {

            if (entry.getValue() instanceof LinkedHashMap) {
                convertKeyMap((Map<String, Object>) entry.getValue(), set, prefix.equals("") ? entry.getKey() : prefix + "-" + entry.getKey());
            } else {
                set.add(prefix + ":" + entry.getValue());
            }
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

    }
}
