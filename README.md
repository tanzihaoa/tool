# android_toy_brick

#### 介绍
Android项目工具公共组件

#### 软件架构
在分支master上


#### 安装教程

1.  主项目目录 build.gradle 文件中添加工具库下载脚本代码----》readme.gradle文件中有，直接复制,刷新项目
2.  主项目目录 settings.gradle 文件中添加工具库
    include ':util_lib'
    project(':util_lib').projectDir = new File('../android-toy-brick/tools')
3.  主项目app目录中 build.gradle 文件中添加
    implementation project(":util_lib")
4.  在readme.gradle 文件中，有一个注释的kotlin类LibAdUtils 用来初始化工具库

[新的不用]: # (5.  记得在工具库中添加对应的马甲)

#### 使用说明
1.  xxxx
2.  xxxx
3.  xxxx