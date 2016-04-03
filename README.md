# NextEvents

[![BuildStatus](https://travis-ci.org/yoojia/NextEvents.svg)](https://travis-ci.org/yoojia/NextEvents)

## 已支持特性

- 支持使用EventHandler接口回调；
- 支持使用@Subscribe注解方法回调；
- 支持多种回调方式: CallerThread / Threads / MainThread(For Android extends)
- 支持自定义回调目标的调度处理 Schedule
- 支持提交事件组

## 未来支持特性

- 多事件触发
- 自定义事件匹配处理
- 事件转换

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

## 触发条件

在注解模式下，即@Subscribe注解的回调方法的匹配规则如下：

1. 定义的事件名与发送事件名相同；
2. 无参数时，符合条件1即触发回调；
3. 原始数据类型与包装类型是相同的；
4. 有参数时，发送的事件负载数量与定义参数数量和类型相同 (定义顺序可随意, 但类型存在相同时按发送顺序依次填充)；
5. 参数中有且仅有一个参数时，可使用 Any 来接收任意负载数, 任意类型的事件 (Object.class只匹配Object类型)；

```java
// 回调方法带参数, 接收指定类型的事件：String
@Subscribe(on="test-event")
void handleWithArgEvents(String evt) {
    ...
}

// 回调方法不带参数，接收任意类型事件，并且不需要获取事件对象。
@Subscribe(on="text-event")
void handleNoArgEvent( ) {
    ...
}

// 回调方法参数类型为 Any， 接收任意类型事件，并且获取事件对象。
@Subscribe(on = "text-event")
void handleAnyTypeEvent(Any evt) {
    ...
}

// 发送事件，以上方法都被触发回调
nextEvents.emit("test-event", new String("this is an event payload"))

```

## 事件组

在发送事件时，将多个事件以对象组的方式提交，回调方法中定义对应类型的参数即可。参数顺序可以随意，但类型最好是互不相同。
如果相同类型的参数，回调方法中数值填充的顺序将与emit(...)的参数顺序一致。

**注意:事件组数量大于1时,不能包含null值**

```java

@Subscribe(on="users")
void handleEvent(String name, int age, float weight) {
    ...
}

nextEvents.emit("users", "yoojia", 18, 1024.0f)

```

## Dead event

如果发送事件没有回调目标方法或者Handler时，原事件将被包装成DeadEvent重新发送。可以通过订阅`EventPayload.DEAD_EVENT`事件来处理DeadEvent。

**DeadEvent事件与普通事件一样，唯一区别是它的事件名固定为`EventPayload.DEAD_EVENT`的值。**

```java
@Subscribe(on = EventPayload.DEAD_EVENT)
void onMissedTyped(String evt) {
    ...
}
```

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
