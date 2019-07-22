package com.atvoid.maven.plugin.utils;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommonUtilsTest {

     @Test
     public void findFilesWithNameTest() {

          List<String> res =new ArrayList<>();

          CommonUtils.findFilesWithName("./","a.txt",res,"/","properties");
          System.out.println(res.toString());
     }


}
