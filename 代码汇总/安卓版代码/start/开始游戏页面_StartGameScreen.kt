package com.lvzelin.kurokobasketball.start

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 开始游戏页面。
 * 后续可以放区服选择、模式选择和进入比赛按钮。
 */
@Composable
fun StartGameScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "开始游戏",
        subtitle = "这里后续放模式选择、区服入口和进入比赛按钮。",
        onBack = onBack
    )
}
