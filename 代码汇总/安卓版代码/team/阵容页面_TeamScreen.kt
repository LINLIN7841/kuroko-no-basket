package com.lvzelin.kurokobasketball.team

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 阵容页面。
 * 后续可以放上阵球员、替补球员、站位和阵容加成。
 */
@Composable
fun TeamScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "阵容",
        subtitle = "这里后续放上阵球员、站位和队伍配置。",
        onBack = onBack
    )
}
