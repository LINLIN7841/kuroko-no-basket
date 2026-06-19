package com.lvzelin.kurokobasketball.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lvzelin.kurokobasketball.theme.CourtPanel
import com.lvzelin.kurokobasketball.theme.EnergyBlue
import com.lvzelin.kurokobasketball.theme.EnergyGold
import com.lvzelin.kurokobasketball.theme.SurfaceMist

/**
 * 通用占位功能页。
 * 目前没有真实功能的页面都先复用它，后续再替换成具体页面。
 */
@Composable
fun PlaceholderPage(
    title: String,
    subtitle: String,
    onBack: () -> Unit
) {
    AppScaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onBack, shape = RoundedCornerShape(8.dp)) {
                    Text("返回")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = SurfaceMist,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CourtPanel.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, EnergyBlue.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(title, color = EnergyGold, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Text(subtitle, color = SurfaceMist, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
