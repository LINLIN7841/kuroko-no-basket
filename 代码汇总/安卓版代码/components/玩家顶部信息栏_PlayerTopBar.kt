package com.lvzelin.kurokobasketball.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lvzelin.kurokobasketball.data.PlayerProfile
import com.lvzelin.kurokobasketball.theme.CourtPanel
import com.lvzelin.kurokobasketball.theme.EnergyBlue
import com.lvzelin.kurokobasketball.theme.EnergyGold
import com.lvzelin.kurokobasketball.theme.SurfaceMist

/**
 * 玩家顶部信息栏。
 * 这是旧版主界面使用过的通用组件，保留它方便后续页面复用。
 */
@Composable
fun PlayerTopBar(player: PlayerProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CourtPanel.copy(alpha = 0.88f))
            .border(BorderStroke(1.dp, EnergyBlue.copy(alpha = 0.7f)), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(EnergyBlue.copy(alpha = 0.25f))
                .border(BorderStroke(1.dp, EnergyBlue), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("头像", color = SurfaceMist, style = MaterialTheme.typography.labelMedium)
        }

        Column {
            Text(player.nickname, color = SurfaceMist, fontWeight = FontWeight.Bold)
            Text("Lv.${player.level}", color = EnergyGold, style = MaterialTheme.typography.labelMedium)
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.End) {
            Text("金币 ${player.gold}", color = SurfaceMist, style = MaterialTheme.typography.labelMedium)
            Text("钻石 ${player.diamond}", color = EnergyGold, style = MaterialTheme.typography.labelMedium)
        }
    }
}
