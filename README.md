# hongyan

#### 介绍

hongyan 是一个终端便利的工具集合体,你可以用他来储存/分享文本,上传图片,制作短链接,时间转换时间戳,摩斯加密,获取出网IP,获取本次请求的请求信息....各种功能应有尽有,只要你有基础的java-web知识,还可以自己新增功能哦!

本项目由JAVA语言编写,使用了Hutool工具包简化开发,本项目代码简单,扩展容易,理解更容易,
欢迎大家参与贡献,希望它能成为你在github/gitee上参与贡献的第一个仓库!
详情请移动至**参与贡献**内有代码编写帮助
**参与贡献并非只有提交代码,提交BUG/建议/修改错字/修改描述让人更易理解均是对项目的贡献**

#### 可以做什么
##### 文本传输

例子：
在命令终端执行（cmd/或者bash）

``` curl 
curl -X POST "https://hongyan.pro/测试KEY/测试VALUE"
```

即成功设置了一个key为测试KEY，value为测试VALUE的对象
接下来在浏览器访问：https://hongyan.pro/测试KEY
或者：`curl -X GET "https://hongyan.pro/测试KEY"`
即可查询对应的value
###### 删除或者编辑
目前hongyan仅支持对带密码的KEY进行删除或者修改操作,在设置KEY的时候指定密码请参考

新增
```curl
curl -X POST -L "http://hongyan.pro/带密码的KEY/带密码的VALUE/密码"
```
编辑
```curl
curl -X POST -L "http://hongyan.pro/带密码的KEY/带密码的新VALUE/密码"
```
删除
```curl
curl -X POST -L "http://hongyan.pro/带密码的KEY/密码"
```
##### 设置方法和参数作为值
可以设置如下格式的VALUE来实现调用方法
如: `method:获取请求信息?arg=参数1&arg2=参数2`

其中`method:`是固定前缀,`获取请求信息`是要调用的方法,`?arg=参数1&arg2=参数2`是要传递的参数,使用url参数格式

例:`curl -X POST --location "https://hongyan.pro/dtff1/method:获取请求信息?arg=%E5%8F%82%E6%95%B01"`

返回值:
```json
{
    "url": "https://hongyan.pro/获取请求信息",
    "args": {
        "arg": [
            "参数1"
        ]
    },
    "header": {
        "host": "localhost:80",
        "connection": "Keep-Alive",
        "user-agent": "Apache-HttpClient/4.5.13 (Java/17.0.3)",
        "accept-encoding": "gzip,deflate"
    }
}
```
##### 检查更新

可以储存一个key为xx软件版本号,value为版本号(0.11)
代码中获取该版本号,通过和内置版本号比较大小判断是否有新版本

##### 获取本机IP

`curl https://hongyan.pro/我的IP`
返回你的出网IP
`127.0.1`
##### 重定向
以跳转到一言为例,url=你要跳转的链接可以跟普通的请求一样传递参数?c=f&encode=text

`curl https://hongyan.pro/redirect?url=https://v1.hitokoto.cn/?c=f&encode=text`
##### 获取请求信息
`curl https://hongyan.pro/获取请求信息`
返回你这次请求的所有请求信息 可用于验证代码或者curl命令是否有问题
```json
{
  "url": "https://hongyan.pro/获取请求信息",
  "args": {
  },
  "header": {
    "host": "https://hongyan.pro",
    "connection": "Keep-Alive",
    "user-agent": "Apache-HttpClient/4.5.13 (Java/17.0.3)",
    "accept-encoding": "gzip,deflate"
  }
}
```
###### 时间转换
`curl https://hongyan.pro/时间转换`
默认返回当前时间和时间戳
```json
{
    "当前时间戳(毫秒)": 1660181822793,
    "当前时间戳(秒)": 1660181822,
    "当前时间": "2022-08-11 09:37:02",
    "介绍": "时间转换功能需要转递date参数自动识别要转换成时间或者时间戳,不传递默认返回当前时间"
}
```
###### 摩斯密码
摩斯密码功能需传递参数text如 /摩斯密码?text=a,自动识别加密或者解密
`curl https://hongyan.pro/摩斯密码?text=hello`

`...././.-../.-../---/`
#### 软件架构
开发框架:SpringBoot+Druid+Mybatis-plus+Hutool工具包
数据库: sqlite

#### 安装教程
克隆本项目(本项目依赖JDK1.8,以及maven环境),进入到pom.xml同级,执行命令`mvn install package`

等待命令执行完成后去/target目录下,找到hongyan-0.0.1-SNAPSHOT.jar,执行 `nohop java -jar 包名 &`

或者直接去[https://gitee.com/rui2450/hongyan/releases](https://gitee.com/rui2450/hongyan/releases)下载最新的jar包 

记得创建配置文件哦,在jar包同级创建config目录,里面创建application.yml,内容可以参考

``` yml
spring:
  datasource:
    # sqlite
    driver-class-name: org.sqlite.JDBC
    # 全路径
    url: jdbc:sqlite:/data/hongyan.db
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      initialSize: 5
      minIdle: 5
      maxActive: 20
      maxWait: 60000
      timeBetweenEvictionRunsMillis: 60000
      minEvictableIdleTimeMillis: 300000
      validationQuery: SELECT 1
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      # 打开PSCache
      poolPreparedStatements: true
      # SQL合并配置
      filters: mergeStat,wall
      #指定每个连接上PSCache的大小
      maxPoolPreparedStatementPerConnectionSize: 20
      #合并多个DruidDataSource的监控数据
      useGlobalDataSourceStat: true
      #慢SQL记录
      filter:
        stat:
          log-slow-sql: true
      # 监控界面配置
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        login-username: 这里记得改
        login-password: 这里记得改
        reset-enable: true
server:
  port: 80
  tomcat:
    uri-encoding: UTF-8
  servlet:
    encoding:
      charset: UTF-8
logging:
  level:
    root: info
  charset:
    console: UTF-8
    file: UTF-8
  file:
    name: logs/hongyan.log
  logback:
    rollingpolicy:
      max-file-size: 10MB
      max-history: 20
      file-name-pattern: logs/%d{yyyy-MM}/hongyan.%d{yyyy-MM-dd}.%i.log
      total-size-cap: 1024MB
      clean-history-on-start: false

```
建表sql:
```sql
create table hongyan_map_table
(
    id         INTEGER  primary key AUTOINCREMENT,
    key        text     default ''                not null,
    value      text                                  not null,
    password   text,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```
#### 使用说明

1. 访问当前域名可直接获取到目前所有可执行的方法名
2. 传递参数可使用 /方法名?参数名=参数值

#### 参与贡献
1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request
##### 代码编写帮助
HongYanBaseFunction是一个函数式接口,本项目所有对外提供的方法都继承/实现了此接口

如果您想新增一个方法可以选择继承此接口例:RandomStringFunction

或者直接使用简单的lambda表达式例:HongYanFunctionConfig.样例方法

最后在HongYanFunctionConfig中新增一个方法public HongYanBaseFunction 方法名()

将你的方法实体return出来

java类参考HongYanFunctionConfig.随机字符串

lambda表达式参考HongYanFunctionConfig.样例方法

最后在方法上加入@Bean(name = {"样例方法"})注解,name属性是个数组,用户输入数组中的任何值都可以访问到该方法

*请勿和其他方法/已被存储数据的KEY 重复,请勿命名太多个*

*在所有方法接口中只展示第一个名字*

获取用户传递的参数可以使用`request.getParameter("参数名")`

建议在用户未传递参数时返回方法的简单介绍,如HongYanFunctionConfig.生成二维码

#### 致谢
感谢[JetBrains](https://www.jetbrains.com/zh-cn/)提供的开发工具!

感谢[MemFire Cloud](https://cloud.memfiredb.com/auth/login?from=fZA8As)提供的免费数据库!

感谢项目中使用到的所有开源项目!
