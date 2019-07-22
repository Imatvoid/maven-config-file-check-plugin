def file = new File(basedir, 'build.log')

def endCorrect = false

def defaultProperties = false

file.eachLine {

    if (it =~ /.默认为properties*/) {
        defaultProperties = true
    } else if (it =~ /.检验通过*/) {
        endCorrect = true
    }

}

if (!defaultProperties) {
    throw new RuntimeException('默认检验properties未生效')
}

if (!endCorrect) {
    throw new RuntimeException('正确的项目配置检验未通过')
}