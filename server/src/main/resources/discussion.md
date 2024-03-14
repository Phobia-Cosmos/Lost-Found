# 实体类

```java
@Data
// （原来的lost&found）
public class Item {
    private Long id;
    private String name;
    private String description;
    private String img;
    private Integer status;  //表示找回了还是丢失
    private Integer tag;    //这个物品是什么类型
    private Long userId;    // 发布者ID
    private Integer isLost;     //是丢失还是招领
    private Timestamp startTime;    //什么时候找到或者丢失
    private Timestamp endTime;
    private Timestamp publishTime;  //用来识别数据库中的物品
}

@Data
public class Message {
    private Long id;
    private Long itemId;
    private Long lostUserId;    // 丢失者ID
    private Long foundUserId;
    private String content;     //消息内容
    private Timestamp createTime;   //这个对话创建时间
    private Timestamp updateTime;   //对话的修改时间
}

@Data
public class Suggestion {
    private Long id;
    private Long userId;
    private Integer tag;        //建议类型
    private Integer pollCount;      //建议的票数
    private String content;
    private Timestamp publishTime;  //发布时间
}

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Integer role;
    private Integer reputation;     //用户的信誉或者赞数（flower），admin修改
    private String school;      //备用
    private Timestamp createTime;   
    private Integer status;  //用户的状态：封禁、注销等（用来扩展admin的功能）
}
```

# 问题

1. 我想知道我们这个Admin和User的区别，如果没有区别他们的功能都是类似的话就没有必要多写controller，而且也可能不需要管理员这个身份，因此我想了几个可能的功能
   1. Admin：审核、封禁文章、还原用户账号、修改建议etc；
   2. User：建议、发布文章、查询etc；
2. 我觉得notice那个controller没有必要，我们只需要从item（原来的lost&found）中选出几个就可以了
---

# 测试

为了后续开发方便，我先将当前版本的代码上传（file上传功能为实现），你可以先将代码拉取到本地测试一下接口（接口未完全测试，因此可能存在问题），熟悉一下修改后的接口并提出测试的问题。问题按照 **文件名-方法名** 
标识出来，可以汇总成一个文档给我（我将同步进行测试），由我统一修改后端代码，修改完成后再讨论如何修改前端。

## 如何操作

1. 从github上pull代码到本地（或者使用我发给你的代码），其中的大部分逻辑未发生改变，只是代码重构了；
2. 配置Maven仓库（使用你之前的，大部分配置都在其中，我多加了一个swagger依赖以及更改了一些json依赖）；
3. 导入数据库sql脚本（其中大部分都是表结构，我刚测试了UserController，因此数据不多）；
4. compile后（也可以不compile）启动，浏览器localhost:9090/doc.html进行endpoint测试，不需要启动前端项目；


## 问题

1. 密码等字段长度校验在前端完成，所以密码的校验可以删除
2. 用户的信息已经存储ThreadLocal中了，因此我们后续处理时可以判断用户的权限

