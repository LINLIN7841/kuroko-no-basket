package com.lvzelin.kurokobasketball.character

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 球员页面。
 * 后续可以扩展为球员列表、球员立绘、技能详情和培养入口。
 */
@Composable
fun CharacterScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "球员",
        subtitle = "这里后续放角色列表、角色立绘和能力详情。",
        onBack = onBack
    )
}
