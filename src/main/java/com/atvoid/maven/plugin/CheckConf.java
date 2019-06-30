package com.atvoid.maven.plugin;

import java.util.List;


public interface CheckConf {



    /**
     * 校验资源文件
     * @param onlineFileNames
     * @param offlineFileNames
     * @return
     */
     boolean compare(String onlinePath,List<String> onlineFileNames,String offlinePath,List<String> offlineFileNames);



     /**
     * 文件存在于resource而不存在于online-resources，则警告输出
     * @param offlinePath
     * @param onlinePath
     */
     void compareDir(String offlinePath, String onlinePath);

}
