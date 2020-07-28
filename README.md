# 刚开始起步的小程序后端程序

## 2020年5月28日23点22分 完成了最基础本地小程序的调用后端数据的实验
### 小程序使用http调用本地后端服务器返回的内容，小程序对于json好像很适应的样子

```$xslt
2020年7月28日
文件上传压缩
1. 获取项目路径
String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath();
            //log.info("系统路径：{}", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new MyDefinitionException("获取项目路径失败");
        }

2.得到源文件的未知 并且获取到新文件的位置
2.1 新文件的位置
String thumbnailPath1Temp = path + thumbnailPath1;
2.2 源文件的位置
        String fileUrlTemp = path + fileUrl;
2.3 为新文件的位置创建一个目录 文件存放的位置
        String thumbnailPath1FileUrl = thumbnailPath1Temp.substring(0, thumbnailPath1Temp.lastIndexOf("/"));
        //log.info("临时文件地址：{}", thumbnailPath1FileUrl);
        File thumbnailPath1File = new File(thumbnailPath1FileUrl);
        if (!thumbnailPath1File.exists()) {
            //创建目录
            thumbnailPath1File.mkdirs();
        }
        //log.info("yuantu:" + fileUrlTemp);
        //log.info("thumbnailPath1Temp:" + thumbnailPath1Temp);

2.4 压缩文件
        /*只压缩大小不裁剪*/
        try {
            Thumbnails.of(fileUrlTemp).scale(1f).outputQuality(0.5f).toFile(thumbnailPath1Temp);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("将原图转换图片压缩图失败");
        }
3 源文件压缩完成后直接删除源文件 因为源文件比较大 太占内存了
        /*文件压缩完成后删除源文件 实在是有点大了
         * 按照分配20G的硬盘容量 每张图片3M那么也只能容纳6862张图片
         * 当前仅仅是测试就已经有了300多张图片了
         * 完全不够用
         * */
        try {
            File fileDelete = new File(fileUrlTemp);
            if (fileDelete.delete()) {
                weixinResource.setPath("原文件已删除");
            } else {
                log.error("原文件删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
```


```$xslt
2020年7月26日

这个第三包中包含了文件上传需要的httpmime 如果把这个注释了 就需要自己引用一下httpmime包
<!-- https://mvnrepository.com/artifact/com.arronlong/httpclientutil
         https://github.com/Arronlong/httpclientutil
         暂时不适用 其实还是有点用的
         -->
       <!-- <dependency>
            <groupId>com.arronlong</groupId>
            <artifactId>httpclientutil</artifactId>
            <version>1.0.4</version>
        </dependency>-->

自己引用httpmime包
        <!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpmime -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpmime</artifactId>
            <version>4.5.12</version>
        </dependency>
```


```$xslt
2020年7月25日
今天要解决的事情还有两个
1.excel数据导出 2020年7月25日19点47分
完成
这里使用的是easyExcel
public class ActiveVoteWorkExcelExportVO extends BaseRowModel implements Serializable 
具体的还是要参考网络文档这里只是特别简单的使用了
2.文字过滤系统
微信的接口文档是真的垃圾
也不说是不是要json数据格式 也没有什么规范 就硬写 微信是真的牛逼
垃圾微信 我要是以后能很满意的用上微信的东西才怪 阿里系和百度系加油 争取第三方接口就用你们

```



```text
2020年7月23日
添加了配置超级管理员的资源配置项
administrators.super.user.ids=25;27
这样可以在代码中这样读取
@PropertySource("classpath:administrators.properties")
@Value("#{'${administrators.super.user.ids}'.split(';')}")
private Integer[] superUserIds;
这样就可以读取了

```


```$xslt
2020年7月13日
     主要是修改投票限制内容
     // 0指的是总共有一张票 每日有几张票 0 是能不能重复投票
      // 1 可以重复投票  
      // 如果选择每天 3票 指的是： 每天可以投三次 如果选择总共 3票：总共有3票 每天重置票数

      //  如果选择不重复给一个选手进行投票  每天可以投三次 或者 总共可投3次 
      // 如果选择重复给一个选手进行投票  每天可以投三次 或者 总共可投3次  但是，总共，就是一共就3次，都可以投给这个选手  如果选择每天，每一天投给这个选手


要记住一个教训：
创建数据库的结果的时候：
如果一张表要关联其他的数据 就一定要往上找，一直找完所有的基础表 有多少张基础表就要关联多少基础表的ID
```


```$xslt
2020年7月8日 
新增了活动创建第三页的接口
新增了一个MyErrorList工具类 这个工具类主要是为了能够判断步骤的错误 另外也可以作为返回值的基础
```

```$xslt
2020年7月7日
添加了第二页的信息 但是现在出现了一个问题 第一页有多个图片，但是还没有上传完成就跳转了
导致数据没有上传回来

记录一下错误
如果使用了 selectOneByExample方法去查询，如果数据只有一个是符合查询条件的 就返回一个是正确的
那么如果符合条件example的数据有多个就会出现这种情况了 会有错误的 
所以我觉得还是使用selectByExample这样会返回一个list集合 但是这样可以很好的处理这个错误
如果很需要这种逻辑的 就是必须只能有一个倒是可以尝试使用这个
org.mybatis.spring.MyBatisSystemException: 
nested exception is org.apache.ibatis.exceptions.TooManyResultsException: 
Expected one result (or null) to be returned by selectOne(), but found: 3
```


```$xslt
2020年7月6日
修复了投票限制部分的内容 以及创建了 部分创建活动的逻辑

2020年7月6日
创建活动的图片上传及其展示 取消;分隔符
```


```$xslt
2020年7月1日
基础的活动保存数据 以及 投票限制

```


```$xslt
2020年6月28日 
今天把比较难的资源文件上传给简单的做了一下 因为比较简单而且不涉及到大文件上传 
所以暂时也没有发现什么问题
主要是：
1. 资源分类大目录分类以及避免小文件堆积在一个文件夹中的处理
2. md5校验，这个也不能叫校验，就是检查数据库存储的文件的md5是否存在如果存在就不存了

```


```$xslt
2020年6月20日
添加了用户投票后查看与其他作品差距的接口
```

```$xslt
2020年6月20日
测试成功对具体作品的投票 
如果实体类中已经给了默认值，那么 @NotNull(message = "提示语") 这个操作就没有效果了

新增了作品投票人信息列表
修改了作品详情展示图片类型变为数组类型 可以展示轮播图
记住了：以后dao层的方法一定要一个功能是一个功能尽可以的拆散要不然就会出现方法无法重复使用的情况
```
```$xslt
Unexpected error occurred invoking async method: public java.lang.Integer com.hyp.myweixin.service.impl.UserNoOpenIdIdLogImpl.addUserOperationLog(com.hyp.myweixin.pojo.modal.WeixinUserOptionLog,javax.servlet.http.HttpServletRequest)
这个错误不知道是怎么回事。
```
```$xslt
2020年6月10日 异步处理过程中 在service中抛出异常是不会被抓住的就算被抓住了请求也已经结束了
新增：
1. 日志记录：记录浏览记录
2. 按照查询投票活动ID，查询活动的信息 自定义返回数据
```
```$xslt

2020年6月9日
分页如果需要返回的是再处理的数据，那么分页信息一定要先放进去，不然分页信息就是空值 非常不好
添加了实体类转换工具类

```

```$xslt
2020年6月7日
今天上午遇到一个错误
Null return value from advice does not match primitive return type for:
网上说是aop的错误，是返回值类型不是aop需要的，然后我就给返回值都改成了包装类 就Ok了

今天下午又遇到一个pageHelp方法总是没有分页结果
然后我就升级了一下这个
 <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.5</version>
 </dependency>
pagehelper一定要在查询前面
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteBase用于条件查询
        List<WeixinVoteBase> weixinVoteBases = weixinVoteBaseMapper.selectByExample(example);
        log.info("这里呢？" + weixinVoteBases.toString());

        //log.info("这里没有查询出来数据：" + weixinVoteBases.toString());
        pageInfo = new PageInfo(weixinVoteBases);
具体的实现可以看一下WeixinVoteBaseServiceImpl.java中getVoteWorkByPage这个实现方法

```

```$xslt
2020年6月6日 
今天下午为项目添加了日期工具类
为所有的请求都加上了请求密钥限制 采用很简单的md5（含有key）加密方式
HttpAspect.java 无法拦截post请求中x-www-form-urlencoded类型的数据 所以加了一点代码用这个去获取
不知道会不会导致输出流出现问题
if (reqBody == null || reqBody.length() <= 0) {
                reqBody += "{";
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    reqBody += "\""+entry.getKey() + "\":";
                    for (String s : entry.getValue()) {
                        reqBody += s + ";";
                    }
                }
                reqBody += "}";
            }
```


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
