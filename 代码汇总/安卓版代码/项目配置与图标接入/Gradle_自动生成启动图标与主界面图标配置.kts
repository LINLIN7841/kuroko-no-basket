import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val mainMenuIconAssetDir = layout.buildDirectory.dir("generated/mainMenuIcons/assets")

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

    sourceSets {
        getByName("main") {
            assets.srcDir(mainMenuIconAssetDir)
        }
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

val launcherIconSourceCandidates = listOf(
    rootProject.file("../02_素材图/Logo/game_icon.png"),
    rootProject.file("../02_素材图/Logo/game_icon.ico")
)
val launcherIconSourceDir = rootProject.file("../02_素材图/Logo")
val launcherIconSizes = mapOf(
    "mipmap-mdpi" to 48,
    "mipmap-hdpi" to 72,
    "mipmap-xhdpi" to 96,
    "mipmap-xxhdpi" to 144,
    "mipmap-xxxhdpi" to 192
)
val mainMenuIconSourceDir = rootProject.file("../02_素材图/按钮图标")
val mainMenuIconMappingFile = mainMenuIconSourceDir.resolve("主界面图标映射表.csv")
val mainMenuIconTargets = mapOf(
    "avatar_blue" to "蓝色头像",
    "avatar_red" to "红色头像",
    "avatar_gold" to "金色头像",
    "gold" to "金币",
    "diamond" to "钻石",
    "coupon" to "点券",
    "kuroko_ticket" to "黑子券",
    "mail" to "邮件",
    "market" to "篮球交易市场",
    "member_card" to "会员卡商店",
    "season" to "赛季征程",
    "limited_shop" to "限时商店",
    "gacha" to "扭蛋",
    "shop" to "商城",
    "activity" to "活动大厅"
)

tasks.register("prepareLauncherIcons") {
    group = "assets"
    description = "如果存在 ../02_素材图/Logo/game_icon.png 或 game_icon.ico，就自动生成安卓启动图标。"
    inputs.dir(launcherIconSourceDir).withPropertyName("launcherIconSourceDir").optional()

    doLast {
        val launcherIconSource = launcherIconSourceCandidates.firstOrNull { it.exists() }
        if (launcherIconSource == null) {
            logger.lifecycle("未找到 ${launcherIconSourceCandidates.joinToString { it.path }}，继续使用默认占位启动图标。")
            return@doLast
        }

        val sourceImage = ImageIO.read(launcherIconSource)
        if (sourceImage == null) {
            logger.lifecycle("无法读取启动图标文件：${launcherIconSource.path}。如果它是把 PNG 改后缀成 ico，请确认文件内容仍是普通 PNG。继续使用默认占位启动图标。")
            return@doLast
        }

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

tasks.register("prepareMainMenuIcons") {
    group = "assets"
    description = "读取 ../02_素材图/按钮图标/主界面图标映射表.csv，把自定义主界面图标复制到 App assets。"
    inputs.file(mainMenuIconMappingFile).withPropertyName("mainMenuIconMappingFile").optional()
    inputs.dir(mainMenuIconSourceDir).withPropertyName("mainMenuIconSourceDir").optional()
    outputs.dir(mainMenuIconAssetDir)

    doLast {
        val outputRoot = mainMenuIconAssetDir.get().asFile
        val outputIconDir = outputRoot.resolve("main_icons")

        if (outputRoot.exists()) {
            outputRoot.deleteRecursively()
        }
        outputIconDir.mkdirs()

        if (!mainMenuIconMappingFile.exists()) {
            logger.lifecycle("未找到 ${mainMenuIconMappingFile.path}，主界面继续使用默认占位图标。")
            return@doLast
        }

        val sourceRootPath = mainMenuIconSourceDir.canonicalPath
        val rows = mainMenuIconMappingFile.readLines(Charsets.UTF_8).drop(1)

        rows.forEachIndexed { rowIndex, rawLine ->
            if (rawLine.isBlank()) {
                return@forEachIndexed
            }

            val columns = parseCsvLine(rawLine)
            val iconId = columns.getOrNull(0)?.removePrefix("\uFEFF")?.trim().orEmpty()
            val iconName = columns.getOrNull(1)?.trim().orEmpty()
            val imageFileName = columns.getOrNull(2)?.trim().orEmpty()

            if (iconId.isBlank() || imageFileName.isBlank()) {
                return@forEachIndexed
            }

            if (!mainMenuIconTargets.containsKey(iconId)) {
                logger.lifecycle("主界面图标映射表第 ${rowIndex + 2} 行功能编号 ${iconId} 不支持，已跳过。")
                return@forEachIndexed
            }

            val sourceFile = resolveImageFile(mainMenuIconSourceDir, imageFileName)
            if (!sourceFile.exists()) {
                logger.lifecycle("主界面图标 ${iconName.ifBlank { iconId }} 未找到图片：${mainMenuIconSourceDir.resolve(imageFileName).path}，继续使用占位图标。")
                return@forEachIndexed
            }

            if (!sourceFile.canonicalPath.startsWith(sourceRootPath)) {
                logger.lifecycle("主界面图标 ${iconName.ifBlank { iconId }} 的图片不在按钮图标目录内，已跳过：${sourceFile.path}")
                return@forEachIndexed
            }

            if (ImageIO.read(sourceFile) == null) {
                logger.lifecycle("主界面图标 ${iconName.ifBlank { iconId }} 不是可读取图片：${sourceFile.path}，已跳过。")
                return@forEachIndexed
            }

            sourceFile.copyTo(outputIconDir.resolve("$iconId.png"), overwrite = true)
            logger.lifecycle("已接入主界面图标 ${iconName.ifBlank { mainMenuIconTargets.getValue(iconId) }}：${sourceFile.name}")
        }
    }
}

tasks.matching { it.name == "preBuild" }.configureEach {
    dependsOn("prepareLauncherIcons")
    dependsOn("prepareMainMenuIcons")
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

fun resolveImageFile(sourceDir: File, imageFileName: String): File {
    val directFile = sourceDir.resolve(imageFileName)
    if (directFile.exists()) {
        return directFile
    }

    if (directFile.extension.isBlank()) {
        val pngFile = sourceDir.resolve("$imageFileName.png")
        if (pngFile.exists()) {
            return pngFile
        }
    }

    return directFile
}

fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var inQuotes = false
    var index = 0

    while (index < line.length) {
        val char = line[index]
        when {
            char == '"' && inQuotes && index + 1 < line.length && line[index + 1] == '"' -> {
                current.append('"')
                index++
            }
            char == '"' -> inQuotes = !inQuotes
            char == ',' && !inQuotes -> {
                result.add(current.toString().trim())
                current.clear()
            }
            else -> current.append(char)
        }
        index++
    }

    result.add(current.toString().trim())
    return result
}
