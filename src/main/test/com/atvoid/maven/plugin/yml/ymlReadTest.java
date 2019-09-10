package com.atvoid.maven.plugin.yml;

import com.atvoid.maven.plugin.utils.CommonUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static com.atvoid.maven.plugin.compare.CompareYmlKey.convertKeyMap;

public class ymlReadTest {

    @Test
    public void readYmlToMap() {

        Yaml yaml = new Yaml();
        try {
            List<String> res = new ArrayList<>();
            CommonUtils.findFilesWithName("./", "sample.yml", res, "/","yml");

            File f = new File("." + res.get(1));

            InputStream input = new FileInputStream(f);
            Map<String, Object> map = (Map<String, Object>) yaml.load(input);

            HashSet<String> set = new HashSet<>();
            convertKeyMap(map, set, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
