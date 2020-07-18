Rpc 项目
----
- 极其简单的一个rpc项目

设计思想(都是基于一个简单的实现)
----
* 一个简单的rpc简单讲由以下几个模块构成
    - 注册中心
    - 服务提供方
    - 服务调用方
* 注册中心
    - 注册中心就是一张表，这张表中包含:
        - 服务提供方可被调用的接口
        - 服务提供方的ip
    - 注册表由各个服务提供方，自行注册
* 服务提供方
    - 上报可用服务
    - 上报本机ip
    - 提供对外接口
    - 解析rpc协议，以及传输结果
* 服务调用方
    - 从注册中心找到服务提供方ip
    - 编码并解析rpc协议，请求服务提供方接口
    - 获取结果

主要模块
----
- rpc-framework
    - 服务注册，服务发现，负载均衡
    - rpc协议编码/解码，网络传输
- rpc-api-demo
    - 提供注册api,由服务提供方注册,由服务使用方发现
- rpc-client-demo
    - 服务使用方
- rpc-server-demo
    - 服务调用方

进阶
---- 
todo


分步骤编码
---
- [done] 注册(这里其实就是注册ip:port)-->ServiceRegister
   - 这里是注册到zookeeper
- [done] 发现(发现提供服务的ip:port)--->ServiceDiscover
   - 这里是从zookeeper根据serverName 找到对应的服务地址
   - 获取到了之后，增加了一个负载均衡的的机制LoadBalance
- [todo] rpc 协议-->[netty]
- [todo] 尝试集成spring