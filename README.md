# ArgFlow

> 文档编写中，若有疏漏可以提 issue

基于状态机模式的轻量级内嵌式 Spring 可编排流程引擎组件。

## 设计思想：

[博客-设计一个流程编排引擎](https://blog.mydawn.space/archives/gzvOR59J)

经验上来说，这是我们在一个标准化系统中设计一个流程的思路
![DDD](img/DDD.png)

业务建模依赖标准化流程建模设计自己的定制化流程编排，然后细分解耦流程节点进行开发。

## 功能支持（API）：

![接口.png](img/接口.png)

- 基于 BeanId 的直观简洁的流程编排
- 多分支流程编排，直观的规则树设计
- 支持任务中断挂起和唤醒
- 支持流程任务持久化记录接口
- 自定义任务ID生成器接口

流程任务的生命周期：
![life_circle](img/生命周期.png)

## 快速开始

具体代码在 `spring-example` module 中

下面有一个简单的下单流程的示例，实际代码在 DemoStrategy 中。

![示例](img/示例.png)

