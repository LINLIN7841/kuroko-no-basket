$ErrorActionPreference = "Stop"

$ProjectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$RootDir = (Resolve-Path (Join-Path $ProjectDir "..")).Path
$OutputDir = (Get-ChildItem -LiteralPath $RootDir -Directory | Where-Object { $_.Name -like "04_*" } | Select-Object -First 1).FullName
$AssetDir = (Get-ChildItem -LiteralPath $RootDir -Directory | Where-Object { $_.Name -like "02_*" } | Select-Object -First 1).FullName
$IconFile = Join-Path $AssetDir "game_icon.ico"
$TempExe = Join-Path $env:TEMP "KurokoBasketballDesktop.exe"
$OutputExe = Join-Path $OutputDir "KurokoBasketballDesktop.exe"
$Csc = "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe"
$SourceFile = Join-Path $ProjectDir "DesktopLobbyMainProgram.cs"

function Test-PngFile {
    param([string]$Path)
    if (-not (Test-Path -LiteralPath $Path)) {
        return $false
    }
    $bytes = [System.IO.File]::ReadAllBytes($Path)
    return $bytes.Length -ge 8 -and
        $bytes[0] -eq 0x89 -and
        $bytes[1] -eq 0x50 -and
        $bytes[2] -eq 0x4E -and
        $bytes[3] -eq 0x47 -and
        $bytes[4] -eq 0x0D -and
        $bytes[5] -eq 0x0A -and
        $bytes[6] -eq 0x1A -and
        $bytes[7] -eq 0x0A
}

function Convert-PngToCompatibleIco {
    param(
        [string]$PngPath,
        [string]$IcoPath
    )

    Add-Type -AssemblyName System.Drawing

    $source = [System.Drawing.Image]::FromFile($PngPath)
    try {
        $size = 64
        $bitmap = New-Object System.Drawing.Bitmap $size, $size, ([System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
        try {
            $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
            try {
                $graphics.Clear([System.Drawing.Color]::Transparent)
                $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
                $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
                $graphics.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
                $graphics.DrawImage($source, 0, 0, $size, $size)
            } finally {
                $graphics.Dispose()
            }

            $pixelBytes = New-Object byte[] ($size * $size * 4)
            $index = 0
            for ($y = $size - 1; $y -ge 0; $y--) {
                for ($x = 0; $x -lt $size; $x++) {
                    $pixel = $bitmap.GetPixel($x, $y)
                    $pixelBytes[$index++] = $pixel.B
                    $pixelBytes[$index++] = $pixel.G
                    $pixelBytes[$index++] = $pixel.R
                    $pixelBytes[$index++] = $pixel.A
                }
            }

            $maskStride = [int]([Math]::Ceiling($size / 32.0) * 4)
            $maskBytes = New-Object byte[] ($maskStride * $size)
            $imageBytesLength = 40 + $pixelBytes.Length + $maskBytes.Length

            $stream = [System.IO.File]::Create($IcoPath)
            $writer = New-Object System.IO.BinaryWriter $stream
            try {
                $writer.Write([UInt16]0)
                $writer.Write([UInt16]1)
                $writer.Write([UInt16]1)
                $writer.Write([Byte]$size)
                $writer.Write([Byte]$size)
                $writer.Write([Byte]0)
                $writer.Write([Byte]0)
                $writer.Write([UInt16]1)
                $writer.Write([UInt16]32)
                $writer.Write([UInt32]$imageBytesLength)
                $writer.Write([UInt32]22)
                $writer.Write([UInt32]40)
                $writer.Write([Int32]$size)
                $writer.Write([Int32]($size * 2))
                $writer.Write([UInt16]1)
                $writer.Write([UInt16]32)
                $writer.Write([UInt32]0)
                $writer.Write([UInt32]$pixelBytes.Length)
                $writer.Write([Int32]0)
                $writer.Write([Int32]0)
                $writer.Write([UInt32]0)
                $writer.Write([UInt32]0)
                $writer.Write($pixelBytes)
                $writer.Write($maskBytes)
            } finally {
                $writer.Dispose()
                $stream.Dispose()
            }
        } finally {
            $bitmap.Dispose()
        }
    } finally {
        $source.Dispose()
    }
}

$compilerArgs = @(
    "/nologo",
    "/target:winexe",
    "/platform:x64",
    "/codepage:65001",
    "/out:$TempExe",
    "/reference:System.dll",
    "/reference:System.Drawing.dll",
    "/reference:System.Windows.Forms.dll"
)

if (Test-Path -LiteralPath $IconFile) {
    $CompilerIconFile = Join-Path $env:TEMP "KurokoBasketballDesktop_icon.ico"
    if (Test-PngFile -Path $IconFile) {
        Convert-PngToCompatibleIco -PngPath $IconFile -IcoPath $CompilerIconFile
        Write-Host "game_icon.ico is PNG data. Converted it to a compatible ico for build: $CompilerIconFile"
    } else {
        Copy-Item -LiteralPath $IconFile -Destination $CompilerIconFile -Force
        Write-Host "Copied desktop icon to a compiler-friendly temp path: $CompilerIconFile"
    }
    $compilerArgs += "/win32icon:$CompilerIconFile"
    Write-Host "Using desktop icon: $CompilerIconFile"
} else {
    Write-Host "No desktop icon found. Build will use the default exe icon."
}

$compilerArgs += $SourceFile
& $Csc @compilerArgs

if ($LASTEXITCODE -ne 0) {
    throw "Build failed."
}

Copy-Item -LiteralPath $TempExe -Destination $OutputExe -Force
Write-Host "Build successful: $OutputExe"
