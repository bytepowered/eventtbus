# NextEvents

## 已支持特性

- 支持使用EventHandler接口回调；
- 支持使用@Subscribe注解方法回调；
- 支持多种回调方式: CallerThread / Threads / MainThread(For Android extends)
- 支持自定义回调目标的调度处理 Schedule
- 支持提交事件元组

## 未来支持特性

- 粘滞事件
- 多事件触发
- 自定义事件匹配处理
- 过滤器排序

## Benchmark

`v1.4` 在 `OS X 10.11.3' / 2.6 GHz Intel Core i5 / 8 GB / Java(TM) SE Runtime Environment (build 1.8.0_60-b27)`环境下的性能对比情况如下表：

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

// 提交事件，以上两个方法都被回调
nextEvents.emit("test-event", new String("this is an event payload"))

```

## 事件组

**注意：使用事件组的回调方法的参数列表，它们的类型强制建议互不相同！相同类型的参数将按发送顺序填充！**

在提交事件时，将多个事件以对象组（Object[]实现）的方式提交，回调方法中定义对应类型的参数即可。参数顺序可以随意，但类型最好是互不相同。
如果相同类型的参数，回调方法中数值填充的顺序将与emit(...)的参数顺序一致。


```java
@Subscribe(on="users")
void handleEvent(String name, int age, float weight) {
    ...
}

nextEvents.emit("users", "yoojia", 18, 1024.0f)

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
