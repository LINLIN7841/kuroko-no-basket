package com.lvzelin.kurokobasketball.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.lvzelin.kurokobasketball.theme.CourtInk
import com.lvzelin.kurokobasketball.theme.CourtPanel

/**
 * 所有页面共用的背景容器。
 * 负责提供深色渐变背景和统一页面边距。
 */
@Composable
fun AppScaffold(content: @Composable (PaddingValues) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CourtPanel, CourtInk)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            content(PaddingValues())
        }
    }
}
