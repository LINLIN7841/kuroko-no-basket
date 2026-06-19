package com.lvzelin.kurokobasketball.data

import com.lvzelin.kurokobasketball.R
import com.lvzelin.kurokobasketball.navigation.Screen

/**
 * 玩家基础资料。
 * 目前是本地假数据，后续接账号系统时可以从服务器读取。
 */
data class PlayerProfile(
    val nickname: String,
    val level: Int,
    val gold: Int,
    val diamond: Int
)

/**
 * 主界面功能入口配置。
 * title 是按钮名称，destination 是点击后要进入的页面，iconRes 是默认占位图标，iconKey 是映射表里的功能编号。
 */
data class HomeMenuItem(
    val title: String,
    val destination: Screen,
    val iconRes: Int? = null,
    val iconKey: String? = null
)

/**
 * 顶部资源栏的一项资源。
 * iconRes 表示默认占位图标，amount 表示数量，iconKey 是映射表里的功能编号。
 */
data class ResourceBalance(
    val iconRes: Int,
    val amount: Int,
    val iconKey: String? = null
)

/**
 * 头像库中的一个头像选项。
 * 后续如果换成真实头像图片，可以通过 iconKey 和映射表接入，不需要改图片原文件名。
 */
data class AvatarOption(
    val name: String,
    val iconRes: Int,
    val iconKey: String? = null
)

/**
 * 公告页面中的一条公告。
 * 后续可以改成从后端接口拉取公告列表。
 */
data class NoticeItem(
    val title: String,
    val date: String,
    val content: String
)

/**
 * 主界面所有本地配置数据。
 * 这里集中管理按钮、资源、头像和公告，方便你后面直接改数值或改入口。
 */
object HomeData {
    /** 默认玩家资料。 */
    val player = PlayerProfile(
        nickname = "新晋队长",
        level = 1,
        gold = 12000,
        diamond = 300
    )

    /** 头像库列表。 */
    val avatarOptions = listOf(
        AvatarOption("蓝色头像", R.drawable.ic_avatar_blue, "avatar_blue"),
        AvatarOption("红色头像", R.drawable.ic_avatar_red, "avatar_red"),
        AvatarOption("金色头像", R.drawable.ic_avatar_gold, "avatar_gold")
    )

    /** 顶部中间资源栏：金币、钻石、点券、黑子券。 */
    val resourceBalances = listOf(
        ResourceBalance(R.drawable.ic_gold, 12000, "gold"),
        ResourceBalance(R.drawable.ic_diamond, 300, "diamond"),
        ResourceBalance(R.drawable.ic_coupon, 88, "coupon"),
        ResourceBalance(R.drawable.ic_kuroko_ticket, 12, "kuroko_ticket")
    )

    /** 右上角图标入口：邮件、篮球交易市场。 */
    val topRightItems = listOf(
        HomeMenuItem("邮件", Screen.Mail, R.drawable.ic_mail, "mail"),
        HomeMenuItem("篮球交易市场", Screen.Market, R.drawable.ic_market, "market")
    )

    /** 头像下方快捷入口。 */
    val shortcutItems = listOf(
        HomeMenuItem("会员卡商店", Screen.MemberCardShop, R.drawable.ic_member_card, "member_card"),
        HomeMenuItem("赛季征程", Screen.SeasonJourney, R.drawable.ic_season, "season"),
        HomeMenuItem("限时商店", Screen.LimitedShop, R.drawable.ic_limited_shop, "limited_shop")
    )

    /** 主界面左侧竖向入口。 */
    val sideMenuItems = listOf(
        HomeMenuItem("扭蛋", Screen.Gacha, R.drawable.ic_gacha, "gacha"),
        HomeMenuItem("商城", Screen.Shop, R.drawable.ic_shop, "shop"),
        HomeMenuItem("活动大厅", Screen.ActivityHall, R.drawable.ic_activity, "activity")
    )

    /** 主界面底部五个主按钮。 */
    val bottomMenuItems = listOf(
        HomeMenuItem("潜力", Screen.Potential),
        HomeMenuItem("篮球", Screen.Basketball),
        HomeMenuItem("背包", Screen.Bag),
        HomeMenuItem("球员", Screen.Character),
        HomeMenuItem("成就", Screen.Achievement)
    )

    /** 保留旧字段名，避免后续旧代码引用 menuItems 时出错。 */
    val menuItems = bottomMenuItems

    /** 本地公告列表。 */
    val notices = listOf(
        NoticeItem(
            title = "大厅原型完成",
            date = "2026-06-19",
            content = "当前版本只包含主界面与功能入口，后续会继续补充角色、阵容和玩法。"
        ),
        NoticeItem(
            title = "素材待补充",
            date = "2026-06-19",
            content = "角色立绘、背景、按钮图标和 Logo 会在素材文件夹中后续补充。"
        )
    )
}
