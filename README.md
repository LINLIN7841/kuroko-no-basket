# 黑子的篮球游戏大厅 App

这是第一版安卓主界面工程，只包含游戏大厅、页面跳转和占位页面，不包含登录、联网、支付或比赛玩法。

## 运行方式

1. 使用 Android Studio 打开当前文件夹，等待 Gradle 同步完成。
2. 选择模拟器或真机运行 `app`。
3. 也可以在当前目录执行：

```bat
gradlew.bat assembleDebug
```

当前电脑已经安装 Android Command Line Tools、Android SDK Platform 35、Build Tools 34/35、Platform Tools 和 Gradle 8.10.2。Gradle Wrapper 已补齐，当前 wrapper 使用本机已下载的 Gradle zip：

```text
C:\Users\LVZELIN\AndroidBuildTools\downloads\gradle-8.10.2-bin.zip
```

生成的 debug APK 位置：

```text
app\build\outputs\apk\debug\app-debug.apk
```

## 当前内容

- 主界面：中间角色立绘占位区，顶部玩家信息，功能入口按钮。
- 功能页面：开始游戏、角色、阵容、背包、商城、任务、公告、设置。
- 素材：外部 `02_素材图` 文件夹保持为空，后期补充。
