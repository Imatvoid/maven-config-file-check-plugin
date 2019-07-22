package com.atvoid.maven.plugin;

import com.atvoid.maven.plugin.compare.CompareKey;
import com.atvoid.maven.plugin.compare.ComparePropertiesKey;
import com.atvoid.maven.plugin.compare.CompareYmlKey;
import com.atvoid.maven.plugin.utils.CommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


import java.io.File;
import java.util.*;

import static com.atvoid.maven.plugin.contants.Constants.PROPERTIES_SUFFIX;
import static com.atvoid.maven.plugin.contants.Constants.YML_SUFFIX;

/**
 * 比较properties文件的key在resources与online-resources目录下是否相同（即在resources/x.properties中存在的key必须在online-resources/x.properties中存在）
 * 不同则置build faild.
 * 保持测试环境与生产环境配置项的一致性,方便再建立新的环境.
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.COMPILE)
public class CheckConf extends AbstractMojo {

    @Parameter(property = "fileType")
    private String fileType;

    @Parameter(property = "offlinePath")
    private String offlinePath;


    @Parameter(property = "onlinePath")
    private String onlinePath;


    @Parameter(property = "fileNames")
    private String[] fileNames;

    // 因为特殊原因 忽略这些key 不进行校验 慎重
    @Parameter(property = "ignoreKeys")
    private String[] ignoreKeys;

    private Set<String> ignoreKeySet = new HashSet<>();


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        CommonUtils.setLog(getLog());


        getLog().info("输入路径检验开始:");

        if (!CommonUtils.isDirectory(offlinePath, onlinePath)) {
            throw new MojoFailureException("The [" + offlinePath + "] or [" + onlinePath
                    + "] you input was not valid  directory!..");
        } else {
            getLog().info("输入路径检验通过");
        }


        // 文件存在与resource而不存在于online-resources，则警告输出
        getLog().info("检验路径下所有类型文件差异开始");
        CommonUtils.compareDir(offlinePath, onlinePath);
        getLog().info("检验路径下所有文件差异结束");

        convertIgnoreKeys();

        if (StringUtils.isBlank(fileType)) {
            getLog().info("未配置检验文件类型,默认为properties");
            fileType = PROPERTIES_SUFFIX;
        }
        CompareKey compareKey = getCompareKeyImpl(fileType);



        boolean hasError = false;
        // 如果是人工指定的比较配置文件
        if (fileNames != null && fileNames.length > 0) {
            getLog().info("==========================check starts 路径下特定文件检验模式==========================");
            // 默认的扫描所有online-resources下的属性文件
            List<String> onlineFileNameList = new ArrayList<>();
            List<String> offlineFileNameList = new ArrayList<>();

            for (String fileName : fileNames) {
                CommonUtils.findFilesWithName(onlinePath, fileName, onlineFileNameList, "/", fileType);
                CommonUtils.findFilesWithName(offlinePath, fileName, offlineFileNameList, "/", fileType);
            }
            hasError = compareKey.compare(onlinePath, onlineFileNameList, offlinePath, offlineFileNameList,ignoreKeySet);
        } else {
            getLog().info("==========================check starts 路径下全部文件检验模式==========================");
            // 默认的扫描所有online-resources下的属性文件
            List<String> onlineFileNameList = new ArrayList<>();
            CommonUtils.findFilesWithSuffix(fileType, onlinePath, onlineFileNameList, "/");

            List<String> offlineFileNameList = new ArrayList<>();
            CommonUtils.findFilesWithSuffix(fileType, offlinePath, offlineFileNameList, "/");


            hasError = compareKey.compare(onlinePath, onlineFileNameList, offlinePath, offlineFileNameList,ignoreKeySet);
        }

        if (hasError) {
            throw new MojoExecutionException(
                    "The keys listed above don't exist in online-resources.Please fix it before maven build!..");
        } else {
            getLog().info("==========================check ends 检验通过==========================");
        }

    }

    private void convertIgnoreKeys() {
        if (ArrayUtils.isEmpty(ignoreKeys)) {
            getLog().info("未配置要忽略配置项");
            return;
        }

        ignoreKeySet.addAll(Arrays.asList(ignoreKeys));
        if (ignoreKeySet.size() > 0) {
            getLog().info("设置忽略的配置项为:" + ignoreKeySet.toString());
        }
    }

    private CompareKey getCompareKeyImpl(String fileType) throws MojoExecutionException {
        if (fileType.equals(PROPERTIES_SUFFIX)) {
            getLog().info("检验配置文件类型：properties文件");
            return new ComparePropertiesKey();
        }
        if (fileType.equals(YML_SUFFIX)) {
            getLog().info("检验配置文件类型：yml文件");
            return new CompareYmlKey();
        }

        throw new MojoExecutionException("The plugin configuration check file type neither is properties nor is yml, please fix ");

    }


}
