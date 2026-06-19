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
    $compilerArgs += "/win32icon:$IconFile"
    Write-Host "Using desktop icon: $IconFile"
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
