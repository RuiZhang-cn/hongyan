# hongyan

#### 介绍

hongyan 是一个 终端便利的多功能聚合网站,免费提供各种方便的小工具,目前不断增加中.

本项目由JAVA语言编写,使用了Hutool工具包简化开发,本项目代码简单,扩展容易,理解更容易,
欢迎大家参与贡献,希望它能成为你在github/gitee上参与贡献的第一个仓库!
详情请移动至**参与贡献**内有代码编写帮助
**参与贡献并非只有提交代码,提交BUG/建议/修改错字/修改描述让人更易理解均是对项目的贡献**

#### 可以做什么
##### 文本传输

例子：
在命令终端执行（cmd/或者bash）

``` curl 
curl -X POST --location "http://localhost:80/测试KEY/测试VALUE"
```

即成功设置了一个key为测试KEY，value为测试VALUE的对象
接下来在浏览器访问：http://localhost:8080/测试KEY
或者：`curl -X GET --location "http://localhost:80/测试KEY"`
即可查询对应的value
##### 设置方法和参数作为值
可以设置如下格式的VALUE来实现调用方法
如: `method:获取请求信息?arg=参数1&arg2=参数2`

其中`method:`是固定前缀,`获取请求信息`是要调用的方法,`?arg=参数1&arg2=参数2`是要传递的参数,使用url参数格式

例:`curl -X POST --location "http://localhost:80/dtff1/method:获取请求信息?arg=%E5%8F%82%E6%95%B01"`

返回值:
```json
{
    "url": "http://localhost/获取请求信息",
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

`curl http://localhost:80/我的IP`
返回你的出网IP
`127.0.1`
##### 获取请求信息
`curl http://localhost:80/获取请求信息`
返回你这次请求的所有请求信息 可用于验证代码或者curl命令是否有问题
```json
{
  "url": "http://localhost/获取请求信息",
  "args": {
  },
  "header": {
    "host": "103.233.255.220:80",
    "connection": "Keep-Alive",
    "user-agent": "Apache-HttpClient/4.5.13 (Java/17.0.3)",
    "accept-encoding": "gzip,deflate"
  }
}
```
###### 时间转换
`curl http://localhost:80/时间转换`
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
`curl http://localhost:80/摩斯密码?text=hello`

`...././.-../.-../---/`
#### 软件架构
开发框架:SpringBoot+Druid+Mybatis-plus+Hutool工具包
数据库:MemFire Cloud 免费云数据库

#### 安装教程
下载最新的jar包执行 `nohop java -jar 包名 &`
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
https://cloud.memfiredb.com/auth/login?from=fZA8As
感谢[MemFire Cloud](https://cloud.memfiredb.com/auth/login?from=fZA8As)提供的免费数据库!

感谢项目中使用到的所有开源项目!
