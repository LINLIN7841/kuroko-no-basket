package com.lvzelin.kurokobasketball.navigation

/**
 * App 内所有页面的统一定义。
 * 每个 data object 代表一个可以跳转的页面，title 是页面显示名称。
 */
sealed class Screen(val title: String) {
    data object Home : Screen("主界面")
    data object StartGame : Screen("开始游戏")
    data object Character : Screen("球员")
    data object Team : Screen("阵容")
    data object Bag : Screen("背包")
    data object Shop : Screen("商城")
    data object Mission : Screen("任务")
    data object Notice : Screen("公告")
    data object Settings : Screen("设置")
    data object Potential : Screen("潜力")
    data object Basketball : Screen("篮球")
    data object Achievement : Screen("成就")
    data object Gacha : Screen("扭蛋")
    data object ActivityHall : Screen("活动大厅")
    data object MemberCardShop : Screen("会员卡商店")
    data object SeasonJourney : Screen("赛季征程")
    data object LimitedShop : Screen("限时商店")
    data object Mail : Screen("邮件")
    data object Market : Screen("篮球交易市场")
}
