package com.atvoid.maven.plugin.check;

import com.atvoid.maven.plugin.CheckConf;
import com.atvoid.maven.plugin.ComparePropertiesKey;
import com.atvoid.maven.plugin.utils.CommonUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.atvoid.maven.plugin.contants.Constants.PROPERTIES_SUFFIX;

/**
 * 比较properties文件的key在resources与online-resources目录下是否相同（即在resources/x.properties中存在的key必须在online-resources/x.properties中存在）
 * 不同则置build faild.
 * 保持测试环境与生产环境配置项的一致性,方便再建立新的环境.
 */
@Mojo(name = "check-properties", defaultPhase = LifecyclePhase.COMPILE)
public class CheckPropertiesConf extends AbstractMojo {

    /**
     * Location of the output directory.
     **/
    @Parameter(property = "project.build.directory")
    private File outputDirectory;


    @Parameter(property = "offlinePath")
    private String offlinePath;


    @Parameter(property = "onlinePath")
    private String onlinePath;


    @Parameter(property = "fileNames")
    private String[] fileNames;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("========================== validate dir starts ==========================");

        if (!CommonUtils.isDirectory(offlinePath, onlinePath)) {
            throw new MojoFailureException("The [" + offlinePath + "] or [" + onlinePath
                    + "] you input was not valid  directory!..");
        } else {
            getLog().info("========================== validate dir ends ==========================");
        }


        CheckConf checkConf = new ComparePropertiesKey();
        // 文件存在与resource而不存在于online-resources，则警告输出
        checkConf.compareDir(offlinePath, onlinePath);

        getLog().info("==========================check starts==========================");

        boolean hasError = false;
        // 如果是人工指定的比较配置文件
        if (fileNames != null && fileNames.length > 0) {

            // 默认的扫描所有online-resources下的属性文件
            List<String> onlineFileNameList = new ArrayList<>();
            List<String> offlineFileNameList = new ArrayList<>();

            for(String fileName : fileNames) {
                CommonUtils.findFilesWithName(onlinePath, fileName, onlineFileNameList,"/");
                CommonUtils.findFilesWithName(offlinePath, fileName, offlineFileNameList,"/");
            }
            hasError = checkConf.compare(onlinePath,onlineFileNameList,offlinePath, offlineFileNameList);
        } else {
            // 默认的扫描所有online-resources下的属性文件
            List<String> onlineFileNameList = new ArrayList<>();
            CommonUtils.findFilesWithSuffix(PROPERTIES_SUFFIX,onlinePath,onlineFileNameList,"/");

            List<String> offlineFileNameList = new ArrayList<>();
            CommonUtils.findFilesWithSuffix(PROPERTIES_SUFFIX,offlinePath,offlineFileNameList,"/");

            hasError = checkConf.compare(onlinePath,onlineFileNameList,offlinePath, offlineFileNameList);
        }

        if (hasError) {
            throw new MojoExecutionException(
                    "The keys listed above don't exist in online-resources.Please fix it before maven build!..");
        } else {
            getLog().info("==========================check ends==========================");
        }

    }

}
