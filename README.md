# jvm-tools

## 说明

`jvm-tools`是使用`javaagent`和`javaasist`实现的一些小功能

## 使用

1. 找到java程序的pid
2. 使用命令`java -jar jvm-tools.jar <pid>` attach到目标java进程
3. 在终端中输入支持的指令

## 支持指令

### 方法参数打印

* 输入打印方法的参数
* 指令: `methodlog <className> <methodName>`

### 还原类

* 能够将修改的类还原
* 指令: `restoreclass <className>`

### 退出

* 关闭attach时候启动的`netty`服务端, 客户端也同时关闭
* 指令: `shutdown`

