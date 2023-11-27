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

   

