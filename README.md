# RabbitMQ消息投递组件封装

## 0.引言

&emsp;&emsp;在如今越来越内卷的IT行业，高并发，基本会出现在每一场面试中。而面试者关于此的回答中，我们也始终能听到一个熟悉的名词：消息队列，以及他所存在的一些特性和问题。例如：**削峰填谷**，**异步化缓冲**等，借着学习课程的契机以及自身的理解，也对消息队列中的RabbitMQ进行封装，做到开箱即用。

## 1.架构图解

![架构图解](https://cdn.jsdelivr.net/gh/xuyikai1/PictureBedCDN/img/架构图解.png)

|名称|说明|
|:-:|:-:|
|Sender|生产消息-生产端|
|MQ Broker|接收生产端发出的消息，MQ Broker接收|
|Confirm Listener|接收RabbitMQ响应给生产端，表示RabbitMQ收到消息，返回确认，而Listener接收确认消息|
|分布式定时任务|xxl-job分布式调度，定时轮询未投递/消费成功的消息，重新让Sender进行二次/重复投递|

> 大体流程：消息生产端Sender在发送消息前，对消息进行初始化并且落库MSG DB（业务上也可同时记录-> BIZ DB），而后发送至MQ，MQ Broker收到消息后发回该条消息的响应，表示消息已收到，Comfirm Listener负责接收响应，并且更新MSG DB的该条消息状态为已投递。而另一端定时任务会抽取待确认的消息，到达重试时间并且重试多次（自定义），让Sender进行再次投递，保证消息投递成功。

## 2.组件关键点概述

![组件概述](https://cdn.jsdelivr.net/gh/xuyikai1/PictureBedCDN/img/组件概述.png)

根据上图，组件封装的关键点清晰可见

|部分要点|技术选型|说明|
|:-:|:-:|:--:|:-:|
|数据交互层|tk-mybatis|通用性mapper|
|序列化|jackson|保证自定义message与amqp message的转换|
|定时任务|xxl-job分布式调度|定时任务|

### 1.1 消息类型区分

消息在组件中分为三种类型：
（1）**迅速/普通消息**：普通消息，不要求MQ返回ack确认收到，力求消息投递速度
（2）**可靠消息**：要求MQ返回ack确认收到，生产端发出的每一条消息不回丢失
（3）**延迟消息**：要求MQ返回ack确认收到，生产端发出的消息会在自定义的时间后要求MQ返回ack确认收到

### 1.2 消息异步化/序列化

（1）消息通过JacksonSerializer序列化和反序列化，做到amqp Message与自定义Message之间的转换。发送的时候是自定义的message，能通过自定义序列化保证收到也是自定义的message
（2）生产消息异步化

### 1.3 链接池化/高性能

（1）RabbitMQ每个topic对应一个RabbitTemplate，缓存起来，有效节约创建销毁开支。template单例模式下，在多生产者时（不同topic不同exchange）性能会受到影响，以topic为key生成多生产者，不同种类的topic对应相应的template，做不同的定制操作，提升性能
（2）在生产消息的过程中，由线程池统一调度

### 1.4 完备的补偿机制

消息信息落库，并且对消息进行状态标记，再由xxl-job分布式定时任务扫描进行重新投递，保证消息不因为网络抖动发生丢失，以达到准确投递

## 2.可靠性投递剖析与改进点

![可靠性投递](https://cdn.jsdelivr.net/gh/xuyikai1/PictureBedCDN/img/生产端的可靠性投递.png)

### 3.1 消息不可靠要点

- RabbitMQ收到消息生产端发送的消息之后，因为负载压力过大等因素，返回给生产端的失败(confirm失败)，但实际是成功的
- broker因为网络抖动没收到消息，消息丢失
- broker收到消息，confirm因为网络抖动没收到消息，消息丢失

![解决方案](https://cdn.jsdelivr.net/gh/xuyikai1/PictureBedCDN/img/互联网大厂解决方案.png)

### 3.2 消息落库，消息打标

此项目采用的就是这一解决方案来做到可靠性投递

![消息落库](https://cdn.jsdelivr.net/gh/xuyikai1/PictureBedCDN/img/消息信息落库.png)


### 3.3 消息的延迟投递，做二次确认，回调检查
> （99.99%的可靠性） - 减少数据库操作，保证性能

![延迟投递二次确认](https://cdn.jsdelivr.net/gh/xuyikai1/PictureBedCDN/img/二次确认.png)
> **upstream service：** 上游服务
> **callback service：** 回调服务
> **重点：** 一定是数据库操作完了再去发送消息，互联网大厂不加事务（导致性能瓶颈）
> **优点：** 主流程中，相比第一种方案少了一次DB操作，而是使用callback service异步来承担

1.一次性生成两条MQ消息（第二条为延迟消息投递- 用来n分钟之后检查，两条消息投递的队列不同，查看图中线的颜色）

2.消费端处理完消息之后，再发一条处理成功的消息并且入库msg db，callback service来专门监听消费端的处理成功的消息队列，也同时监听生产端发出的延迟消息队列，callback收到延迟消息时查询db

3.如果消费端返回处理失败或者没有响应时，callback service要做消息补偿（即收到延迟消息查询之后发现没有处理成功，则主动发起rpc通信，给上游服务发送reSend命令，生产端再根据biz的业务相关id来查询biz db再次发送两条消息）

封装组件源码：**[源码](https://github.com/xuyikai1/rabbit-parent)**
