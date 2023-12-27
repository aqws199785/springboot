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



# 缓存

## 	缓存雪崩

​	**什么是缓存雪崩？**

​	当某一个时刻出现大规模的缓存失效的情况，那么就会导致大量的请求直接打在数据库上面，导致数据库压力巨大，如果在高并发的情况下，可能瞬间就会导致数据库宕机。这时候如果运维马上又重启数据库，马上又会有新的流量把数据库打死。这就是缓存雪崩。

​	**分析：**

​	造成缓存雪崩的关键在于在同一时间大规模的key失效。为什么会出现这个问题呢，有几种可能，第一种可能是Redis宕机，第二种可能是采用了相同的过期时间。搞清楚原因之后，那么有什么解决方案呢？

​	**解决方案：**

​	1、在原有的失效时间上加上一个随机值，比如1-5分钟随机。这样就避免了因为采用相同的过期时间导致的缓存雪崩。如果真的发生了缓存雪崩，有没有什么兜底的措施？

​	2、使用熔断机制。当流量到达一定的阈值时，就直接返回“系统拥挤”之类的提示，防止过多的请求打在数据库上。至少能保证一部分用户是可以正常使用，其他用户多刷新几次也能得到结果。

​	3、提高数据库的容灾能力，可以使用分库分表，读写分离的策略。

​	4、为了防止Redis宕机导致缓存雪崩的问题，可以搭建Redis集群，提高Redis的容灾性。

## 	**缓存击穿**

​	**什么是缓存击穿？**

​	其实跟缓存雪崩有点类似，缓存雪崩是大规模的key失效，而缓存击穿是一个热点的Key，有大并发集中对其进行访问，突然间这个Key失效了，导致大并发全部打在数据库上，导致数据库压力剧增。这种现象就叫做缓存击穿。

​	**分析：**

​	关键在于某个热点的key失效了，导致大并发集中打在数据库上。所以要从两个方面解决，第一是否可以考虑热点key不设置过期时间，第二是否可以考虑降低打在数据库上的请求数量。

​	**解决方案：**

​	1、上面说过了，如果业务允许的话，对于热点的key可以设置永不过期的key。

​	2、使用互斥锁。如果缓存失效的情况，只有拿到锁才可以查询数据库，降低了在同一时刻打在数据库上的请求，防止数据库打死。当然这样会导致系统的性能变差。

## 	**缓存穿透**

​	**什么是缓存穿透？**

​	我们使用Redis大部分情况都是通过Key查询对应的值，假如发送的请求传进来的key是不存在Redis中的，那么就查不到缓存，查不到缓存就会去数据库查询。假如有大量这样的请求，这些请求像“穿透”了缓存一样直接打在数据库上，这种现象就叫做缓存穿透。

​	**分析：**

​	关键在于在Redis查不到key值，这和缓存击穿有根本的区别，区别在于**缓存穿透的情况是传进来的key在Redis中是不存在的**。假如有黑客传进大量的不存在的key，那么大量的请求打在数据库上是很致命的问题，所以在日常开发中要对参数做好校验，一些非法的参数，不可能存在的key就直接返回错误提示，要对调用方保持这种“不信任”的心态。

​	**解决方案：**

​	1、**把无效的Key存进Redis中**。如果Redis查不到数据，数据库也查不到，我们把这个Key值保存进Redis，设置value="null"，当下次再通过这个Key查询时就不需要再查询数据库。这种处理方式肯定是有问题的，假如传进来的这个不存在的Key值每次都是随机的，那存进Redis也没有意义。

​	2、**使用布隆过滤器**。布隆过滤器的作用是某个 key 不存在，那么就一定不存在，它说某个 key 存在，那么很大可能是存在(存在一定的误判率)。于是我们可以在缓存之前再加一层布隆过滤器，在查询的时候先去布隆过滤器查询 key 是否存在，如果不存在就直接返回。



## Redis工具类

基于StringRedisTemplate封装一个缓存工具类，满足下列需求:

1. 将任意Java对象序列化为json并存储在string类型的key中，并且可以设置TTL过期时间
2. 将任意Java对象序列化为json并存储在string类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿问题
3. 根据指定的key查询缓存，并反序列化为指定类型，利用缓存空值的方式解决缓存穿透问题
4. 根据指定的key查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题

## 全局唯一ID生成器



# 超卖问题

超卖问题是典型的多线程安全问题，针对这一问题的常见解决方案就是加锁:

## **悲观锁**

认为线程安全问题一定会发生，因此在操作数据之前先获取锁，确保线程串行执行。

1. 例如Synchronized、Lock都属于悲观锁

## 乐观锁

认为线程安全问题不一定会发生，因此不加锁，只是在更新数据时去判断有没有其它线程对数据做了修改。

### 解决思路

1. 如果没有修改则认为是安全的，自己才更新数据。
2. 如果已经被其它线程修改说明发生了安全问题，此时可以重试或异常。

### 解决方法

#### 	**版本号法**

​	查看版本是否发生变化

1. 表字段添加一个version字段、每次数据更新时 version+1

2. 按照 id=1 查询数据库数据 stock=10,version=1

3. 更新库存数据 update table table_name set stock=stock-1,version=version+1 where id=1 and version =1，如果有其它线程更新了数据，version会不匹配，更新失败

   

   #### CAS法

   查看数据本身是否发生变化

1. 按照 id =1 查询数据库数据 stock=10
2. 更新库存数据 update table table_name set stock=stock-1 where id=1 and  stock=10，

弊端





# 代码问题

## 前端Authorization:返回为[object Object]

```java
// 检查返回给前端的数据是否正确 
    ResultDto.ok(token);
```

