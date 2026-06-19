package com.lvzelin.kurokobasketball.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lvzelin.kurokobasketball.bag.BagScreen
import com.lvzelin.kurokobasketball.character.CharacterScreen
import com.lvzelin.kurokobasketball.components.PlaceholderPage
import com.lvzelin.kurokobasketball.home.HomeScreen
import com.lvzelin.kurokobasketball.mission.MissionScreen
import com.lvzelin.kurokobasketball.notice.NoticeScreen
import com.lvzelin.kurokobasketball.settings.SettingsScreen
import com.lvzelin.kurokobasketball.shop.ShopScreen
import com.lvzelin.kurokobasketball.start.StartGameScreen
import com.lvzelin.kurokobasketball.team.TeamScreen

/**
 * App 页面导航总入口。
 * 这里用一个 currentScreen 状态记录当前显示哪个页面。
 */
@Composable
fun KurokoBasketballApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    BackHandler(enabled = currentScreen != Screen.Home) {
        currentScreen = Screen.Home
    }

    when (currentScreen) {
        Screen.Home -> HomeScreen(onOpenScreen = { currentScreen = it })
        Screen.StartGame -> StartGameScreen(onBack = { currentScreen = Screen.Home })
        Screen.Character -> CharacterScreen(onBack = { currentScreen = Screen.Home })
        Screen.Team -> TeamScreen(onBack = { currentScreen = Screen.Home })
        Screen.Bag -> BagScreen(onBack = { currentScreen = Screen.Home })
        Screen.Shop -> ShopScreen(onBack = { currentScreen = Screen.Home })
        Screen.Mission -> MissionScreen(onBack = { currentScreen = Screen.Home })
        Screen.Notice -> NoticeScreen(onBack = { currentScreen = Screen.Home })
        Screen.Settings -> SettingsScreen(onBack = { currentScreen = Screen.Home })
        Screen.Potential -> PlaceholderRouteScreen(Screen.Potential, "这里后续放球员潜力培养和突破入口。") { currentScreen = Screen.Home }
        Screen.Basketball -> PlaceholderRouteScreen(Screen.Basketball, "这里后续放篮球养成、装备和球类收藏。") { currentScreen = Screen.Home }
        Screen.Achievement -> PlaceholderRouteScreen(Screen.Achievement, "这里后续放成就进度和奖励领取。") { currentScreen = Screen.Home }
        Screen.Gacha -> PlaceholderRouteScreen(Screen.Gacha, "这里后续放扭蛋抽取、卡池和概率说明。") { currentScreen = Screen.Home }
        Screen.ActivityHall -> PlaceholderRouteScreen(Screen.ActivityHall, "这里后续放限时活动、活动副本和福利入口。") { currentScreen = Screen.Home }
        Screen.MemberCardShop -> PlaceholderRouteScreen(Screen.MemberCardShop, "这里后续放会员卡购买和权益说明。") { currentScreen = Screen.Home }
        Screen.SeasonJourney -> PlaceholderRouteScreen(Screen.SeasonJourney, "这里后续放赛季任务、赛季等级和通行证奖励。") { currentScreen = Screen.Home }
        Screen.LimitedShop -> PlaceholderRouteScreen(Screen.LimitedShop, "这里后续放限时礼包和倒计时商品。") { currentScreen = Screen.Home }
        Screen.Mail -> PlaceholderRouteScreen(Screen.Mail, "这里后续放系统邮件、奖励邮件和公告邮件。") { currentScreen = Screen.Home }
        Screen.Market -> PlaceholderRouteScreen(Screen.Market, "这里后续放篮球交易市场和球员交易入口。") { currentScreen = Screen.Home }
    }
}

/**
 * 还没有单独实现的页面统一走这里。
 * 这样新增入口时不需要马上写完整页面，也不会点击后空白。
 */
@Composable
private fun PlaceholderRouteScreen(
    screen: Screen,
    subtitle: String,
    onBack: () -> Unit
) {
    PlaceholderPage(
        title = screen.title,
        subtitle = subtitle,
        onBack = onBack
    )
}
