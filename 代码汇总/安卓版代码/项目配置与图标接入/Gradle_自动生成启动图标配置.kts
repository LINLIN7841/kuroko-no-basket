import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.lvzelin.kurokobasketball"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lvzelin.kurokobasketball"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.core:core-ktx:1.15.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
}

val launcherIconSource = rootProject.file("../02_素材图/Logo/game_icon.png")
val launcherIconSizes = mapOf(
    "mipmap-mdpi" to 48,
    "mipmap-hdpi" to 72,
    "mipmap-xhdpi" to 96,
    "mipmap-xxhdpi" to 144,
    "mipmap-xxxhdpi" to 192
)

tasks.register("prepareLauncherIcons") {
    group = "assets"
    description = "如果存在 ../02_素材图/Logo/game_icon.png，就自动生成安卓启动图标。"

    doLast {
        if (!launcherIconSource.exists()) {
            logger.lifecycle("未找到 ${launcherIconSource.path}，继续使用默认占位启动图标。")
            return@doLast
        }

        val sourceImage = ImageIO.read(launcherIconSource)
            ?: throw GradleException("无法读取图标文件：${launcherIconSource.path}")

        launcherIconSizes.forEach { (folderName, size) ->
            val outputDir = project.file("src/main/res/$folderName")
            outputDir.mkdirs()
            val squareIcon = resizeSquare(sourceImage, size)
            ImageIO.write(squareIcon, "png", outputDir.resolve("ic_launcher.png"))
            ImageIO.write(squareIcon, "png", outputDir.resolve("ic_launcher_round.png"))
        }

        logger.lifecycle("已根据 ${launcherIconSource.path} 生成安卓启动图标。")
    }
}

tasks.matching { it.name == "preBuild" }.configureEach {
    dependsOn("prepareLauncherIcons")
}

fun resizeSquare(source: BufferedImage, size: Int): BufferedImage {
    val output = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = output.createGraphics()
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    val cropSize = minOf(source.width, source.height)
    val cropX = (source.width - cropSize) / 2
    val cropY = (source.height - cropSize) / 2
    graphics.drawImage(source, 0, 0, size, size, cropX, cropY, cropX + cropSize, cropY + cropSize, null)
    graphics.dispose()
    return output
}
