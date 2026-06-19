package com.lvzelin.kurokobasketball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lvzelin.kurokobasketball.navigation.KurokoBasketballApp
import com.lvzelin.kurokobasketball.theme.KurokoBasketballTheme

/**
 * 安卓程序启动入口。
 * 系统点击 App 图标后会先进入这个 Activity。
 */
class MainActivity : ComponentActivity() {
    /**
     * Activity 创建时执行。
     * 这里负责加载 Compose 主题，并显示整个游戏大厅应用。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KurokoBasketballTheme {
                KurokoBasketballApp()
            }
        }
    }
}
