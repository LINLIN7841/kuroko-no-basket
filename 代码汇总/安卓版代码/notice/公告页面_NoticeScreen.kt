package com.lvzelin.kurokobasketball.notice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lvzelin.kurokobasketball.components.AppScaffold
import com.lvzelin.kurokobasketball.data.HomeData
import com.lvzelin.kurokobasketball.theme.CourtPanel
import com.lvzelin.kurokobasketball.theme.EnergyGold
import com.lvzelin.kurokobasketball.theme.SurfaceMist

/**
 * 公告页面。
 * 读取 HomeData.notices 中的本地公告，并以卡片形式展示。
 */
@Composable
fun NoticeScreen(onBack: () -> Unit) {
    AppScaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onBack, shape = RoundedCornerShape(8.dp)) {
                    Text("返回")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "公告",
                    color = SurfaceMist,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            HomeData.notices.forEach { notice ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CourtPanel.copy(alpha = 0.82f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(notice.title, color = EnergyGold, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(notice.date, color = SurfaceMist, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(notice.content, color = SurfaceMist)
                    }
                }
            }
        }
    }
}
