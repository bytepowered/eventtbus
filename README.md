# NextEvents

## 已支持特性

- 支持使用EventHandler接口回调；
- 支持使用@Subscribe注解方法回调；
- 支持多种回调方式: CallerThread / Threads / MainThread(For Android extends)
- 支持自定义回调目标的调度处理 Schedule
- 支持提交事件组

## 未来支持特性

- 多事件触发
- 自定义事件匹配处理

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

1. 回调方法定义的事件名必须与发送事件名相同；
2. 回调方法无参数时，符合条件1即触发回调；
3. 回调方法有参数时，参数数量与发送的事件（组）数量必须相同，顺序可以随意；
4. 回调方法的参数中，在有且仅有一个参数是，可使用Object来接收任意类型事件；

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

// 回调方法参数类型为Object， 接收任意类型事件，并且获取事件对象。
@Subscribe(on = "text-event")
void handleAnyTypeEvent(Object evt) {
    ...
}

// 发送事件，以上方法都被触发回调
nextEvents.emit("test-event", new String("this is an event payload"))

```

## 事件组

**注意1：使用事件组的回调方法的参数列表，它们的类型强制建议互不相同！相同类型的参数将按发送顺序填充！**
**注意2：在回调方法参数列表中，如果参数超过1个，则列表中的参数不允许存在Object类型**

在发送事件时，将多个事件以对象组的方式提交，回调方法中定义对应类型的参数即可。参数顺序可以随意，但类型最好是互不相同。
如果相同类型的参数，回调方法中数值填充的顺序将与emit(...)的参数顺序一致。

```java

@Subscribe(on="users")
void handleEvent(String name, int age, float weight) {
    ...
}

nextEvents.emit("users", "yoojia", 18, 1024.0f)

```

## Dead event

如果发送事件没有回调目标方法或者Handler时，原事件将被包装成DeadEvent重新发送。可以通过订阅`PayloadEvent.DEAD_EVENT`事件名来处理DeadEvent。

**DeadEvent事件与普通事件一样，唯一区别是它的事件名固定为`PayloadEvent.DEAD_EVENT`的值。**

```java
@Subscribe(on = PayloadEvent.DEAD_EVENT)
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
