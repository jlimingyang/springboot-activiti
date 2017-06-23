#springboot-activiti

###项目本地部署###
1. 下载附件`gradle-2.12.rar`，解压到任意目录，添加到PATH环境变量`xx\gradle-2.12\bin`
2. 进入项目根目录，双击`gradlew.bat` 安装`spring boot`包依赖
3. 安装`js`依赖: `npm install`
4. 执行`bower install` 安装`angular js`依赖
5. 执行`gulp serve` 编译前台页面
6. 打开 http://localhost:9000 (登录：用户名：admin 密码：admin 企业号：admin) 进行访问

### 项目介绍
1. 后台框架基于Spring Boot，Spring Security
2. 工作流使用Activiti
3. 前台框架基于Angular js

### 注意事项
1. 项目分为测试环境和生成环境，在`application.yml`中配置`active: dev`或`active: prod`
2. 工作流Activiti没有创建自带的用户权限表，而是由我们自己创建用户权限视图，创建脚本在`import.sql`中
3. 项目首次启动时，`activiti.cfg.xml`中`<property name="databaseSchemaUpdate" value="true" />`
value值应设为`true`,以后启动更改为`none`,否则会因activiti表已存在导致启动失败
4. 本项目是在jhipster项目基础上构建，具体可参考github：https://github.com/jhipster/generator-jhipster

###下面是项目部分截图
![screenshot 1](http://git.oschina.net/wyy396731037/springboot-activiti/raw/master/1.png "在线编辑流程图")
![screenshot 2](http://git.oschina.net/wyy396731037/springboot-activiti/raw/master/2.png "流程模板管理")
![screenshot 3](http://git.oschina.net/wyy396731037/springboot-activiti/raw/master/3.png "流程跟踪")
![screenshot 4](http://git.oschina.net/wyy396731037/springboot-activiti/raw/master/4.png "代办任务管理")
