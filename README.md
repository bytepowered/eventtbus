# NextEvents

## 已支持特性

- 支持使用Subscriber接口回调；
- 支持使用@Subscriber注解方法回调；
- @Subscriber注解支持添加自定义事件过滤过滤器；
- 支持多种回调方式: CallerThread / Threads / MainThread
- 支持自定义回调目标的调度处理 Schedules

## 未来支持特性

- 粘滞事件
- 多事件触发
- 自定义事件匹配处理
- 过滤器排序

## Benchmark

测试类型/负载方式| QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)	| ### Delivered:3008/s		   | Emit:38ms		    | Runs:664ms		| Calls:2000
SharedThreads(1ms Payload)	| ### Delivered:3034/s		   | Emit:3ms		    | Runs:659ms		| Calls:2000
CallerThread(1ms Payload)	| ### Delivered:729/s		   | Emit:2740ms		| Runs:2740ms		| Calls:2000
GuavaEvents(1ms Payload)	| ### Delivered:720/s		   | Emit:2775ms		| Runs:2776ms		| Calls:2000
MultiThreads(Nop Payload)	| ### Delivered:1396836/s		| Emit:1431ms		| Runs:1431ms		| Calls:2000000
SharedThreads(Nop Payload)	| ### Delivered:1360313/s		| Emit:1466ms		| Runs:1470ms		| Calls:2000000
CallerThread(Nop Payload)	| ### Delivered:4635219/s		| Emit:431ms		| Runs:431ms		| Calls:2000000
GuavaEvents(Nop Payload)	| ### Delivered:2018744/s		| Emit:990ms		| Runs:990ms		| Calls:2000000

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
