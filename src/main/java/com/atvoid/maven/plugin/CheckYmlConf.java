package com.atvoid.maven.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "check-yml", defaultPhase = LifecyclePhase.COMPILE)
public class CheckYmlConf extends AbstractMojo {


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {




    }
}
