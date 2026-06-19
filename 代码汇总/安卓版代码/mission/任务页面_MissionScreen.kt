package com.lvzelin.kurokobasketball.mission

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 任务页面。
 * 后续可以放每日任务、主线任务、成就任务和奖励领取。
 */
@Composable
fun MissionScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "任务",
        subtitle = "这里后续放每日任务、成就和奖励领取。",
        onBack = onBack
    )
}
