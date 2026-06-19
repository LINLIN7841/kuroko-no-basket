package com.lvzelin.kurokobasketball.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * App 全局颜色方案。
 * 所有 Compose 页面默认都会使用这套暗色篮球大厅风格。
 */
private val AppColors = darkColorScheme(
    primary = EnergyBlue,
    secondary = EnergyGold,
    tertiary = EnergyRed,
    background = CourtInk,
    surface = CourtPanel,
    onPrimary = CourtInk,
    onSecondary = CourtInk,
    onTertiary = CourtInk,
    onBackground = CourtLine,
    onSurface = SurfaceMist
)

/**
 * App 全局主题入口。
 * 在 MainActivity 中包住整个应用，让颜色和字体统一生效。
 */
@Composable
fun KurokoBasketballTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
