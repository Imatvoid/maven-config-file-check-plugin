# check-maven-plugin
maven配置文件校验插件

## 场景

项目分环境建立配置文件，一般是properties/yml。

- test.properties
- online.properties
- gray.properties

随着时间的推移，配置项的增多，人员的紧急操作，可能在不同环境的配置文件间出现配置不同的情况，比如`com.example=xxxx`这个属性，只在线上环境配置了，测试环境中没有，这是十分危险的。

但是人为比对多环境的配置文件，当配置项比较多的时候，费时费力。

应该考虑用插件解决，通过插件验证保证各环境配置项的一致。



## 效果

检验通过
```
[INFO] --- check-maven-conf:1.0-SNAPSHOT:check (check) @ app-all-correct ---
[INFO] 输入路径检验开始:
[INFO] 输入路径:/home/atvoid/文档/我的坚果云/project/maven-config-file-check-plugin/src/its/appAllCorrect/src/test/resources
[INFO] 输入路径:/home/atvoid/文档/我的坚果云/project/maven-config-file-check-plugin/src/its/appAllCorrect/src/main/resources
[INFO] 输入路径检验通过
[INFO] 检验路径下所有类型文件差异开始
[INFO] 检验路径下所有文件差异结束
[INFO] 未配置要忽略配置项
[INFO] 检验配置文件类型：properties文件
[INFO] ==========================check starts 路径下全部文件检验模式==========================
[info] online path:/home/atvoid/文档/我的坚果云/project/maven-config-file-check-plugin/src/its/appAllCorrect/src/main/resources
[info] online files:[/application.properties]
[info] offline path:/home/atvoid/文档/我的坚果云/project/maven-config-file-check-plugin/src/its/appAllCorrect/src/test/resources
[info] offline files:[/application.properties]
[info] 检验online文件开始 onlinePath:/application.properties
[info] 查找对应的offline文件:/application.properties
[info] 检验online文件结束 onlinePath:/application.properties
[INFO] ==========================check ends 检验通过==========================
```
检验失败。无法编译通过

```
[INFO] ==========================check starts 路径下全部文件检验模式==========================
[info] online path:/home/atvoid/文档/我的坚果云/project/maven-config-file-check-plugin/src/its/appNoConfigureFileType/src/main/resources
[info] online files:[/application.properties]
[info] offline path:/home/atvoid/文档/我的坚果云/project/maven-config-file-check-plugin/src/its/appNoConfigureFileType/src/test/resources
[info] offline files:[/application.properties]
[info] 检验online文件开始 onlinePath:/application.properties
[info] 查找对应的offline文件:/application.properties
[error] The online key d was not found in offlinePath/application.properties
[info] 检验online文件结束 onlinePath:/application.properties
```



## 实现功能list

### 拆分目录检验模式
- [x] 配置项白名单

    在白名单中的配置项不进行校验,这是为了满足一些特殊需要,
    一些配置项目可能是只有线上才有的.
    
- [x] 目录下所有文件校验和指定文件list校验

    允许输入onlinePath和offlinePath两个路径和指定文件类型后,
    可以对路径下所有配置文件进行扫描
    
    允许输入onlinePath和offlinePath两个路径和指定文件类型后,配置要校验的文件名list.
    只对这些文件进行校验.
    
    **不指定校验文件类型,默认为properties属性文件.**   
    
- [x] 默认根据得到的配置文件名递归搜索

     ```
      onlinePath/a.properties 
      
      offlinePath/xxxxx/a.properties 
     ```
     只要文件名一致,其中key配置一致,可以通过检验

- [ ] 严格模式

     路径严格匹配选项.
     
      ```
      onlinePath/a.properties      
      offlinePath/xxxxx/a.properties 
      ```
     将不能通过校验.
  
- [ ] 支持检验yml，补充测试用例

### 同目录不同后缀检验模式
- [ ] 支持检验同一文件夹下，以不同后缀区分的配置文件。


## 结构

```
.
├── check-maven-conf.iml
├── pom.xml
├── README.md
└── src
    ├── its   maven测试项目，使用groovy脚本验证maven输出
    │   ├── appAllCorrect
    │   └── appNoConfigureFileType
    └── main
        ├── java
        │   └── com
        │       └── atvoid
        │           └── maven
        │               └── plugin
        │                   ├── CheckConf.java
        │                   ├── compare
        │                   │   ├── CompareKey.java
        │                   │   ├── ComparePropertiesKey.java    检验properties文件实现
        │                   │   └── CompareYmlKey.java                  检验yml文件实现
        │                   ├── contants
        │                   │   └── Constants.java
        │                   ├── touch
        │                   │   └── demo     简单demo
        │                   │       └── MyMojo.java
        │                   └── utils
        │                       └── CommonUtils.java
        └── test  类测试文件
       
```

## 参考

https://blog.gmem.cc/maven-plugin-development     