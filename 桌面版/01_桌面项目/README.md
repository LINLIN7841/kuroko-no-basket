# 黑子的篮球游戏桌面版

这是 Windows 桌面版大厅原型，使用 C# WinForms 制作，可以编译成真正的 `.exe` 双击运行。

## 构建方式

双击或在命令行运行：

```bat
build_desktop.bat
```

生成位置：

```text
..\04_打包输出\KurokoBasketballDesktop.exe
```

## 游戏图标

如果要让 EXE 显示自定义图标，把图标文件放到：

```text
..\02_素材图\game_icon.ico
```

然后重新运行 `build_desktop.bat`。脚本会自动检测这个文件；如果不存在，会继续使用默认 EXE 图标。

## 主界面图标

如果要替换窗口里面的主界面图标，把 PNG 图片放到：

```text
..\02_素材图
```

然后编辑同目录下的：

```text
主界面图标映射表.csv
```

映射表格式为：

```csv
功能编号,功能名称,图片文件名
gold,金币,UI_Dynamic_ItemIcon_Currency4_02.png
coupon,点券,UI_Dynamic_ItemIcon_Currency21_02.png
```

图片原文件名不用改。桌面版会优先按映射表读取图片；如果图片名没写 `.png`，会自动再尝试补 `.png` 查找。找不到图片时，对应图标继续使用程序绘制的占位图。

支持的功能编号和安卓版一致：`avatar_blue`、`avatar_red`、`avatar_gold`、`gold`、`diamond`、`coupon`、`kuroko_ticket`、`mail`、`market`、`member_card`、`season`、`limited_shop`、`gacha`、`shop`、`activity`。

## 当前功能

- 主界面复刻安卓版布局。
- 左上角头像和玩家名，支持改名和选择头像库。
- 顶部资源栏：金币、钻石、点券、黑子券。
- 右上角：邮件、篮球交易市场。
- 头像下方：会员卡商店、赛季征程、限时商店。
- 左侧：扭蛋、商城、活动大厅。
- 底部：潜力、篮球、背包、球员、成就。

## 后续素材

当前界面图标由程序绘制占位。后续可把真实图标、角色立绘、背景图放入 `02_素材图`，再接入图片加载逻辑。
