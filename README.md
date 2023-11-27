# springboot
这是一个springboot集成redis的项目

## 常用命令

```
ctrl + shift + u 大小写切换
```



## 集群的session共享问题集群的会话共享问题

session共享问题:多台Tomcat并不共享session存储空间，当请求切换到不同tomcat服务时导致数据丢失的问题。session的替代方案应该满足:会话共享问题:多台Tomcat并不共享会话存储空间，当请求切换到不同tomcat服务时导致数据丢失的问题。会话的替代方案应该满足：

1. 数据共享..数据共享

2. 内存存储..内存存储

3. key value结构

   因此考虑使用redis存储原先存储在Tomcat中Session中的数据

   

## 拦截器

**第一拦截器**

作用：拦截一切路径,刷新存活时间

1. 获取token
2. 查询Redis的用户
3. 保存到ThreadLocal
4. 刷新token有效期
5. 放行

**第二拦截器**

作用：拦截需要登录的路径

查询ThreadLocal的用户

1. 不存在,则拦截
2. 存在,则继续





## 代码问题

### 前端Authorization:返回为[object Object]

```java
// 检查返回给前端的数据是否正确 
ResultDto.ok(token)
```

