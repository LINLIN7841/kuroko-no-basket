package com.lvzelin.kurokobasketball.settings

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 设置页面。
 * 后续可以放音量、画质、账号、退出登录等选项。
 */
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "设置",
        subtitle = "这里后续放音量、画质、账号和退出选项。",
        onBack = onBack
    )
}
