package com.atvoid.maven.plugin.check;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 比较yml文件的key在resources与online-resources目录下是否相同（即在resources/x.yml中存在的key必须在online-resources/x.yml中存在）
 * 不同则置build faild.
 * 保持测试环境与生产环境配置项的一致性,方便再建立新的环境.
 */
@Mojo(name = "check-yml", defaultPhase = LifecyclePhase.COMPILE)
public class CheckYmlConf extends AbstractMojo {


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {


    }
}
