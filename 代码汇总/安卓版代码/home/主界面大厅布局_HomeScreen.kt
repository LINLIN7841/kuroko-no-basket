package com.lvzelin.kurokobasketball.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lvzelin.kurokobasketball.components.AppScaffold
import com.lvzelin.kurokobasketball.data.AvatarOption
import com.lvzelin.kurokobasketball.data.HomeData
import com.lvzelin.kurokobasketball.data.HomeMenuItem
import com.lvzelin.kurokobasketball.data.ResourceBalance
import com.lvzelin.kurokobasketball.navigation.Screen
import com.lvzelin.kurokobasketball.theme.CourtPanel
import com.lvzelin.kurokobasketball.theme.EnergyBlue
import com.lvzelin.kurokobasketball.theme.EnergyGold
import com.lvzelin.kurokobasketball.theme.SurfaceMist

/**
 * 主界面大厅。
 * 这个函数把顶部、快捷入口、左侧入口、立绘区域和底部按钮组合起来。
 */
@Composable
fun HomeScreen(onOpenScreen: (Screen) -> Unit) {
    var nickname by rememberSaveable { mutableStateOf(HomeData.player.nickname) }
    var avatarRes by rememberSaveable { mutableStateOf(HomeData.avatarOptions.first().iconRes) }
    var avatarIconKey by rememberSaveable { mutableStateOf(HomeData.avatarOptions.first().iconKey) }
    var showNameDialog by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }

    AppScaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HeaderSection(
                nickname = nickname,
                avatarRes = avatarRes,
                avatarIconKey = avatarIconKey,
                onEditName = { showNameDialog = true },
                onEditAvatar = { showAvatarDialog = true },
                onOpenScreen = onOpenScreen
            )

            ShortcutEntryRow(
                items = HomeData.shortcutItems,
                onOpenScreen = onOpenScreen
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LeftSideMenu(
                    items = HomeData.sideMenuItems,
                    onOpenScreen = onOpenScreen
                )

                CharacterStandArea(modifier = Modifier.weight(1f))
            }

            BottomMainMenu(
                items = HomeData.bottomMenuItems,
                onOpenScreen = onOpenScreen
            )
        }
    }

    if (showNameDialog) {
        EditNameDialog(
            currentName = nickname,
            onDismiss = { showNameDialog = false },
            onSave = {
                nickname = it.ifBlank { HomeData.player.nickname }
                showNameDialog = false
            }
        )
    }

    if (showAvatarDialog) {
        AvatarLibraryDialog(
            avatarOptions = HomeData.avatarOptions,
            onDismiss = { showAvatarDialog = false },
            onSelect = {
                avatarRes = it.iconRes
                avatarIconKey = it.iconKey
                showAvatarDialog = false
            }
        )
    }
}

/**
 * 顶部功能区。
 * 左边显示头像和玩家名，中间显示资源栏，右边显示邮件和交易市场图标。
 */
@Composable
private fun HeaderSection(
    nickname: String,
    avatarRes: Int,
    avatarIconKey: String?,
    onEditName: () -> Unit,
    onEditAvatar: () -> Unit,
    onOpenScreen: (Screen) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayerProfileEditor(
            nickname = nickname,
            avatarRes = avatarRes,
            avatarIconKey = avatarIconKey,
            onEditName = onEditName,
            onEditAvatar = onEditAvatar
        )

        ResourceBalanceBar(
            resources = HomeData.resourceBalances,
            modifier = Modifier.weight(1f)
        )

        TopRightActionRow(
            items = HomeData.topRightItems,
            onOpenScreen = onOpenScreen
        )
    }
}

/**
 * 玩家资料编辑区。
 * 点击头像可以换头像，点击“改名”可以修改玩家名称。
 */
@Composable
private fun PlayerProfileEditor(
    nickname: String,
    avatarRes: Int,
    avatarIconKey: String?,
    onEditName: () -> Unit,
    onEditAvatar: () -> Unit
) {
    Row(
        modifier = Modifier.width(118.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        MappedIconImage(
            iconKey = avatarIconKey,
            fallbackRes = avatarRes,
            contentDescription = "修改头像",
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .clickable(onClick = onEditAvatar)
                .background(CourtPanel, CircleShape)
                .border(1.dp, EnergyBlue, CircleShape)
                .padding(6.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = nickname,
                color = SurfaceMist,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            TextButton(
                onClick = onEditName,
                modifier = Modifier.height(26.dp),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
            ) {
                Text("改名", color = EnergyGold, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/**
 * 主界面映射图标显示组件。
 * 先尝试读取构建脚本放入 assets/main_icons 的自定义 PNG，读取不到时使用 fallbackRes 对应的默认占位图标。
 */
@Composable
private fun MappedIconImage(
    iconKey: String?,
    fallbackRes: Int?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mappedIcon = remember(context, iconKey) {
        iconKey?.let { key ->
            runCatching {
                context.assets.open("main_icons/$key.png").use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }

    if (mappedIcon != null) {
        Image(
            bitmap = mappedIcon,
            contentDescription = contentDescription,
            modifier = modifier
        )
    } else if (fallbackRes != null) {
        Image(
            painter = painterResource(fallbackRes),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

/**
 * 顶部资源栏。
 * 按顺序展示金币、钻石、点券和黑子券。
 */
@Composable
private fun ResourceBalanceBar(
    resources: List<ResourceBalance>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .heightIn(min = 42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CourtPanel.copy(alpha = 0.76f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        resources.forEachIndexed { index, item ->
            ResourceBalanceItem(item)
            if (index != resources.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

/**
 * 单个资源显示项。
 * 左边是资源图标，右边是资源数量。
 */
@Composable
private fun ResourceBalanceItem(item: ResourceBalance) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        MappedIconImage(
            iconKey = item.iconKey,
            fallbackRes = item.iconRes,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = item.amount.toString(),
            color = SurfaceMist,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 右上角图标按钮区。
 * 当前放邮件和篮球交易市场。
 */
@Composable
private fun TopRightActionRow(
    items: List<HomeMenuItem>,
    onOpenScreen: (Screen) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            IconOnlyButton(item = item, onOpenScreen = onOpenScreen)
        }
    }
}

/**
 * 只显示图标的按钮。
 * 适合邮件、交易市场这种右上角小入口。
 */
@Composable
private fun IconOnlyButton(
    item: HomeMenuItem,
    onOpenScreen: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CourtPanel.copy(alpha = 0.78f))
            .border(1.dp, EnergyBlue.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
            .clickable { onOpenScreen(item.destination) },
        contentAlignment = Alignment.Center
    ) {
        MappedIconImage(
            iconKey = item.iconKey,
            fallbackRes = item.iconRes,
            contentDescription = item.title,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * 头像下方快捷入口行。
 * 包含会员卡商店、赛季征程、限时商店。
 */
@Composable
private fun ShortcutEntryRow(
    items: List<HomeMenuItem>,
    onOpenScreen: (Screen) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            IconLabelButton(
                item = item,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                onOpenScreen = onOpenScreen
            )
        }
    }
}

/**
 * 左侧竖排入口列表。
 * 包含扭蛋、商城、活动大厅。
 */
@Composable
private fun LeftSideMenu(
    items: List<HomeMenuItem>,
    onOpenScreen: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .width(76.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { item ->
            SideIconEntry(item = item, onOpenScreen = onOpenScreen)
        }
    }
}

/**
 * 左侧单个图标入口。
 * 图标在上方，入口名称在下方。
 */
@Composable
private fun SideIconEntry(
    item: HomeMenuItem,
    onOpenScreen: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenScreen(item.destination) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CourtPanel.copy(alpha = 0.82f))
                .border(1.dp, EnergyGold.copy(alpha = 0.58f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            MappedIconImage(
                iconKey = item.iconKey,
                fallbackRes = item.iconRes,
                contentDescription = item.title,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.title,
            color = SurfaceMist,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * 中间角色立绘区域。
 * 当前只显示“立绘待补充”，后续接入立绘图时不要加区域边框。
 */
@Composable
private fun CharacterStandArea(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.52f)
                    .aspectRatio(0.68f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "立绘待补充",
                    color = SurfaceMist.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "黑子的篮球游戏",
                color = EnergyGold,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 底部五个主按钮。
 * 从左到右固定为：潜力、篮球、背包、球员、成就。
 */
@Composable
private fun BottomMainMenu(
    items: List<HomeMenuItem>,
    onOpenScreen: (Screen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CourtPanel.copy(alpha = 0.88f))
                    .border(1.dp, EnergyBlue.copy(alpha = 0.72f), RoundedCornerShape(8.dp))
                    .clickable { onOpenScreen(item.destination) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.title,
                    color = SurfaceMist,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 图标加文字的横向按钮。
 * 头像下方的三个快捷入口会复用这个函数。
 */
@Composable
private fun IconLabelButton(
    item: HomeMenuItem,
    modifier: Modifier = Modifier,
    onOpenScreen: (Screen) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CourtPanel.copy(alpha = 0.8f))
            .border(1.dp, EnergyBlue.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .clickable { onOpenScreen(item.destination) }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (item.iconRes != null) {
            MappedIconImage(
                iconKey = item.iconKey,
                fallbackRes = item.iconRes,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = item.title,
            color = SurfaceMist,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

/**
 * 修改玩家名称弹窗。
 * 输入框最多保留 8 个字，保存后更新主界面昵称。
 */
@Composable
private fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var draftName by rememberSaveable { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("修改玩家名称") },
        text = {
            OutlinedTextField(
                value = draftName,
                onValueChange = { draftName = it.take(8) },
                singleLine = true,
                label = { Text("玩家名称") }
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(draftName.trim()) }) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 头像库弹窗。
 * 点击任意头像后，会把主界面头像切换成对应图标。
 */
@Composable
private fun AvatarLibraryDialog(
    avatarOptions: List<AvatarOption>,
    onDismiss: () -> Unit,
    onSelect: (AvatarOption) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择头像") },
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                avatarOptions.forEach { option ->
                    MappedIconImage(
                        iconKey = option.iconKey,
                        fallbackRes = option.iconRes,
                        contentDescription = option.name,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent, CircleShape)
                            .border(1.dp, EnergyBlue, CircleShape)
                            .clickable { onSelect(option) }
                            .padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}
