# check-maven-plugin
maven配置文件校验插件

## 场景


## 已实现功能

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