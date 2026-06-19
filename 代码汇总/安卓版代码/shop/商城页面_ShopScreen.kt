package com.lvzelin.kurokobasketball.shop

import androidx.compose.runtime.Composable
import com.lvzelin.kurokobasketball.components.PlaceholderPage

/**
 * 商城页面。
 * 后续可以放礼包、道具、限时商品和充值入口。
 */
@Composable
fun ShopScreen(onBack: () -> Unit) {
    PlaceholderPage(
        title = "商城",
        subtitle = "这里后续放礼包、道具和充值入口占位。",
        onBack = onBack
    )
}
