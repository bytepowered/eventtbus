# NextEvents

## 已支持特性

- 支持使用EventHandler接口回调；
- 支持使用@Subscribe注解方法回调；
- @ Subscribe注解支持添加自定义事件过滤过滤器；
- 支持多种回调方式: CallerThread / Threads / MainThread(Not impl yet)
- 支持自定义回调目标的调度处理 Schedule

## 未来支持特性

- 粘滞事件
- 多事件触发
- 自定义事件匹配处理
- 过滤器排序

## Benchmark

`v1.4` 在 `OSX 10.11.3' / 2.6 GHz Intel Core i5 / 8 GB / Java(TM) SE Runtime Environment (build 1.8.0_60-b27)`环境下的性能对比情况如下表：

测试类型/负载方式| TPS/QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)	 | 2950		| 65ms		| 677ms		| 2000
SharedThreads(1ms Payload)	 | 2892		| 9ms		| 691ms		| 2000
CallerThread(1ms Payload)	 | 728		| 2743ms		| 2743ms		| 2000
GuavaEvents(1ms Payload)	 | 714		| 2797ms		| 2797ms		| 2000
MultiThreads(Nop Payload)	 | 2203562		| 903ms		| 907ms		| 2000000
SharedThreads(Nop Payload)	 | 2030149		| 985ms		| 985ms		| 2000000
CallerThread(Nop Payload)	 | 4788136		| 417ms		| 417ms		| 2000000
GuavaEvents(Nop Payload)	 | 2309855		| 865ms		| 865ms		| 2000000

`v1.4` 在 `Ubuntu 14.04 LTS / 3.6 GHz AMD A8-5600K / 8 GB / Java(TM) SE Runtime Environment (build 1.8.0_65-b17) ` 环境下的性能对比情况如下：

测试类型/负载方式| TPS/QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)	 | 3170		| 53ms		| 630ms		| 2000
SharedThreads(1ms Payload)	 | 3334		| 25ms		| 599ms		| 2000
CallerThread(1ms Payload)	 | 914		| 2187ms	| 2187ms	| 2000
GuavaEvents(1ms Payload)	 | 884		| 2260ms	| 2261ms	| 2000
MultiThreads(Nop Payload)	 | 1160939		| 1629ms		| 1722ms		| 2000000
SharedThreads(Nop Payload)	 | 1337668		| 1410ms		| 1495ms		| 2000000
CallerThread(Nop Payload)	 | 5011338		| 398ms		    | 399ms		    | 2000000
GuavaEvents(Nop Payload)	 | 1714623		| 1166ms		| 1166ms		| 2000000

## 事件处理目标触发条件

在注解模式下，即@Subscribe注解的方法中，如：

1. `事件名相同，参数类型相同`，即事件处理方法关注事件的具体；
1. `事件名相同，无参数`，即事件处理方法只关注事件是否发生，不关注事件内容；

```java
// 回调方法带参数
@Subscribe(on="test-event")
void handleWithArgEvents(String evt) {
    ...
}

// 回调方法不带参数
@Subscribe(on="text-event")
void handleNoArgEvent( ) {
    ...
}

// 发送事件，以上两个方法都被回调
nextEvents.emit("test-event", new String("this is an event payload"))

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
