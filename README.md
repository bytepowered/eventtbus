# NextEvents

事件总线库

[![BuildStatus](https://travis-ci.org/yoojia/NextEvents.svg)](https://travis-ci.org/yoojia/NextEvents)

## 已支持特性

- 支持使用 <EventHandler> 接口回调；
- 支持使用 @Subscribe 注解方法回调；
- 支持多种回调方式: `CALLER_THREAD` / `IO_THREAD` / `MAIN_THREAD` (For Android only)
- 支持自定义回调目标的调度处理 <Scheduler>
- 支持事件组

## 未来支持特性

- 多事件
- 自定义事件匹配处理
- 事件转换

## 触发条件

在 `<EventHandler>` 接口回调模式下，所有 `<EventFilter>` 返回 True 即触发回调。

在 @Subscribe 注解模式下，触发回调方法需要满足两个条件：

1. 定义的事件名与发送事件名相同；
2. 定义的参数与发送事件负载，数量相同且类型相同；

对于`条件2`，以下两个特殊情况也满足：

- 定义的参数数量为0；
- 定义唯一参数且类型为 Any；

```java
// 当发生"str-event"事件，并且事件类型为String时，将被回调；
@Subscribe(events = "str-event")
void handleWithArgEvents(String event) {
    ...
}

// 定义参数数量为0： 当发生"str-event"事件时，无论事件负载是什么类型，都被回调。
@Subscribe(events = "str-event")
void handleNoArgEvent( ) {
    ...
}

// 定义唯一参数且类型为Any： 当发生"str-event"事件时，事件负载为任意数量、任意类型，都被回调。
@Subscribe(events = "str-event")
void handleAnyTypeEvent(Any events) {
    Object[] payloadValues = events.values;
    Class[] payloadTypes = events.types;
    ...
}

// 发射 str-event 事件，上面定义的全部方法都触发回调
nextEvents.emit("str-event", new String("this is an event payload"))

```

## 多负载事件

有些事件可能有多个负载，NextEvents可以直接提交多个负载，无须为这些负载创建包装类。

```java

// 当发生事件时，会将参数按参数类型顺序依次填充
@Subscribe(events = "profile-event")
void handleEvent(String name, int age, float weight) {
    ...
}

// 发射事件，回调方法的参数将会按参数类型顺序依次填充这些数值
nextEvents.emit("profile-event", "yoojia", 18, 1024.0f)

```

#### 多负载参数顺序

NextEvents对多负载事件支持乱序参数。在**符合触发条件的前提下**，上面代码为例，以下的方法都会触发回调：

```java

@Subscribe(events = "profile-event")
void handleEvent1(int age, float weight, String name) {
    ...
}

@Subscribe(events = "profile-event")
void handleEvent2(float weight, int age, String name) {
    ...
}
```

乱序符合以下规则：

- 各个参数类型**互不相同**时，可以是任意顺序。
- 存在相同参数类型的，填充顺序按 emit() 中的参数顺序。如：

```java
@Subscribe(events = "same-type-event")
void handleEvent1(int height, int age, String name) {
    // height == 1024 --> TRUE
    // age == 18 --> TRUE
    ...
}

@Subscribe(events = "same-type-event")
void handleEvent2(int age, int height, String name) {
    // age == 1024 --> TRUE
    // height == 18 --> TRUE
    ...
}

nextEvents.emit("same-type-event", 1024, 18, "yoojia")

```

## Dead event

如果发送事件没有回调目标方法或者Handler时，原事件将被包装成 DeadEvent 重新发送。可以通过订阅 `EventPayload.DEAD_EVENT` 事件来处理 DeadEvent。

**DeadEvent事件与普通事件一样，唯一区别是它的事件名固定为 `EventPayload.DEAD_EVENT` 的值。**

```java
@Subscribe(events = EventPayload.DEAD_EVENT)
void onDeadEvent(String evt) {
    ...
}
```

## Benchmark

`v2.0` 在 `OS X 10.11.3' / 2.6 GHz Intel Core i5 / 8 GB / Java(TM) SE Runtime Environment (build 1.8.0_60-b27)`环境下的性能对比情况如下表：

测试类型/负载方式| TPS/QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)	 | 2749		| 47ms		| 727ms		| 2000
SharedThreads(1ms Payload)	 | 3012		| 8ms		| 663ms		| 2000
CallerThread(1ms Payload)	 | 739		| 2705ms		| 2705ms		| 2000
GuavaEvents(1ms Payload)	 | 717		| 2785ms		| 2785ms		| 2000
MultiThreads(Nop Payload)	 | 1947582		| 1026ms	| 1026ms		| 2000000
SharedThreads(Nop Payload)	 | 891257		| 2243ms	| 2244ms		| 2000000
CallerThread(Nop Payload)	 | 3933523		| 508ms		| 508ms		| 2000000
GuavaEvents(Nop Payload)	 | 2083789		| 959ms		| 959ms		| 2000000

`v2.0` 在 `Ubuntu 14.04 LTS / 3.6 GHz AMD A8-5600K / 8 GB / Java(TM) SE Runtime Environment (build 1.8.0_65-b17) ` 环境下的性能对比情况如下：

测试类型/负载方式| TPS/QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)	 | 3242		| 22ms		| 616ms		| 2000
SharedThreads(1ms Payload)	 | 3351		| 3ms		| 596ms		| 2000
CallerThread(1ms Payload)	 | 914		| 2187ms		| 2187ms		| 2000
GuavaEvents(1ms Payload)	 | 890		| 2245ms		| 2245ms		| 2000
MultiThreads(Nop Payload)	 | 1078525		| 1846ms	| 1854ms		| 2000000
SharedThreads(Nop Payload)	 | 1373540		| 1427ms	| 1456ms		| 2000000
CallerThread(Nop Payload)	 | 3834583		| 521ms		| 521ms	    	| 2000000
GuavaEvents(Nop Payload)	 | 1578518		| 1266ms	| 1267ms		| 2000000

`v2.0` 在 `Windows 10 64x / 3.2 GHz Intel i5-4460 / 8 GB / Java(TM) SE Runtime Environment (build 1.8.0_74-b02) ` 环境下的性能对比情况如下：

测试类型/负载方式| TPS/QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)	 | 3447		| 10ms		| 580ms		| 2000
SharedThreads(1ms Payload)	 | 3376		| 1ms		| 592ms		| 2000
CallerThread(1ms Payload)	 | 902		| 2215ms		| 2215ms		| 2000
GuavaEvents(1ms Payload)	 | 884		| 2260ms		| 2260ms		| 2000
MultiThreads(Nop Payload)	 | 1935508		| 1033ms	| 1033ms		| 2000000
SharedThreads(Nop Payload)	 | 2261975		| 882ms		| 884ms		| 2000000
CallerThread(Nop Payload)	 | 4602303		| 434ms		| 434ms		| 2000000
GuavaEvents(Nop Payload)	 | 3980131		| 502ms		| 502ms		| 2000000

# 分享交流

如果你有想法和建议与我交流,欢迎通过下面方式联系:

- 邮件: yoojia.chen@gmail.com
- QQ: 228441083
- 微信: chenyoca

![WeChat](https://avatars2.githubusercontent.com/u/1492222)

# License

    Copyright 2015 Yoojia Chen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
