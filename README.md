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

在 `MacBook Pro 13' / 2.6 GHz Intel Core i5 / 8 GB 1600 MHz DDR3 / Java(TM) SE Runtime Environment (build 1.8.0_60-b27)`环境下的性能对比情况如下表：

测试类型/负载方式| QPS | 总投递时间 | 总运行时间 | 投递事件量
----|----|----|----|----
MultiThreads(1ms Payload)     | 3008/s           | 38ms              | 664ms        | 2000
SharedThreads(1ms Payload)    | 3034/s           | 3ms               | 659ms        | 2000
CallerThread(1ms Payload)     | 729/s            | 2740ms        | 2740ms           | 2000
GuavaEvents(1ms Payload)      | 720/s            | 2775ms        | 2776ms           | 2000
MultiThreads(Nop Payload)     | 1396836/s           | 1431ms           | 1431ms           | 2000000
SharedThreads(Nop Payload)    | 1360313/s           | 1466ms           | 1470ms           | 2000000
CallerThread(Nop Payload)     | 4635219/s           | 431ms            | 431ms            | 2000000
GuavaEvents(Nop Payload)      | 2018744/s           | 990ms            | 990ms            | 2000000

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
