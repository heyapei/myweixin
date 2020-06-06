# 刚开始起步的小程序后端程序

## 2020年5月28日23点22分 完成了最基础本地小程序的调用后端数据的实验
### 小程序使用http调用本地后端服务器返回的内容，小程序对于json好像很适应的样子


```$xslt
今天中午一直在做sql上面的处理 不知道为啥
#logging.level.dao=debug
#logging.level.org.mybatis=debug
开启这个语句就会看到每次执行数据库的操作就会创建一个session 这个多次创建session具体的原因我也是不知道的
本来我以为这个语句是为了打印每次执行数据库操作的语句，但是并没有按照预期进行打印数据
 <logger name="com.hyp.myweixin.mapper" level="DEBUG"/> 在logback配置文件中添加这句话，才终于执行了打印数据



```

```$xslt
修改了打包过程中不对test进行检查
只需要在properties中添加skip就行
 <skipTests>true</skipTests>


后端服务器 添加了允许跨域请求的代码 这样本地更好测试吧

还有就是我发现 浏览器会自动整出来一个跨域请求
跨域请求过程中，ajax会自动省略'X-Requested-With'请求头 这样后端很难做判断了
所以如果需要的 我们需要设置一下
1. 将ajax的跨域请求关掉
2. 在ajax请求中添加head，如： headers: {'X-Requested-With': 'XMLHttpRequest'}
$.ajax({
            type: "GET",
           /* url: "http://localhost:8099/page/index/carousel/image",*/
            url: "https://yapei.cool/wechat/v1/user/add",
            dataType: "json",
            crossDomain: false,
            timeout: 5000,
            success: function (data) {
                alert(data.code);
            },
            error: function (data, type, err) {  // 以下依次是返回过来的数据，错误类型，错误码
                console.log("ajax错误类型：" + type);
                console.log(err);
            }
        });
跨域请求真的很烦，毕竟暂时不知道有什么用，等有时间研究一下
```
```$xslt
按照我的理解读取自定义配置文件不应该是这样的吗？
@Getter
@Component
@PropertySource("classpath:wechat.properties")
public class PropertiesValue {

    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appSecret;
}

但是这样在别的文件中访问总是null，拿不到值
使用这样的配置就是可以的。。。好蛋疼呀
@Component
@PropertySource("classpath:secret-key.properties")
public class SecretKeyPropertiesValue {
    /**
     * md5验证的密码
     */
    private static String md5key;
    public String getMd5Key() {
        return md5key;
    }
    @Value("${md5.key}")
    private void setMd5Key(String value) {
        md5key = value;
    }
}
而且出来controller中可以使用@Autowired进行注入，其他地方不可以的。。。也是好奇怪的
难受的一匹

使用insertUseGeneratedKeys如果需要返回主键
如果你直接获取他的值，那么获取的是影响的条数，需要使用当前对象的getId方法获取主键
当然是需要 实体类标识ID，数据库字段也是ID 然后是自增的
 @Override
    public int addWechatInfo(WeixinVoteUser weixinVoteUser) {
        try {
            weixinVoteUserMapper.insertUseGeneratedKeys(weixinVoteUser);
        } catch (Exception e) {
            log.info(e.toString());
            throw new MyDefinitionException("保存微信用户数错误");
        }

        return weixinVoteUser.getId();
    }
```




```$xslt
2020年5月30日
配置了druid的账号和密码 但是不知道如何去处理多个properties的读取

2020年5月31日 00点40分
添加了swagger的配置
该部分使用了security进行权限的限制 为啥使用这个呢是因为我搞不定swagger默认的权限控制模块
访问swagger文档 一个是 http://localhost:8080/swagger-ui.html 还有一个是http://localhost:8080/doc.html
还有一点是使用swagger需要解除对swagger静态资源的限制

```
