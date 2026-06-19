package com.lvzelin.kurokobasketball.bag

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 背包页面。
 * 第一版先展示占位内容，后续可以放道具、碎片、材料等列表。
 */
@Composable
fun BagScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "背包",
        subtitle = "这里后续放道具、碎片和材料列表。",
        onBack = onBack
    )
}
