package com.atvoid.maven.plugin.compare;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public interface CompareKey {



    /**
     * 校验资源文件
     * @param onlineFileNames
     * @param offlineFileNames
     * @return
     */
     boolean compare(String onlinePath, List<String> onlineFileNames, String offlinePath, List<String> offlineFileNames, Set<String> ignoreKeySet);



}
