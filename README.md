# sx-framework

[![](https://jitpack.io/v/shixinzhang/sxframework.svg)](https://jitpack.io/#shixinzhang/sxframework)

Some base function classes for android development. 

从零开始创建自己的 Android 框架。

## 目标包括：

1. network

 - 第三方网络库使用、分析
 - 自己设计、封装 OkHttp

2. image
 - 第三方图片加载库使用、分析
 - 自己设计、封装图片加载库

3. cache
 - 三级缓存的设计与分析

4. hybrid
 - WebView 常见功能封装
 - Weex 使用与分析
 - 参考 Weex 创建 hybrid 组件

5. utils
 - 常见工具类

6. views
 - 常见布局

## 目前阶段：

刚开始，**network** 开发中，（╯－＿－）╯╧╧

持续更新中，敬请 ``watch`` 。


## 如何使用

1.在根项目的 ``build.gradle`` 的 ``allprojects`` 中添加

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2.在 module 项目的 ``build.gradle`` 中添加地址

```
	dependencies {
	    compile 'com.github.shixinzhang:sxframework:1.0.5'
	}
```