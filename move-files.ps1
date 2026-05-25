# 移动文件脚本 - 将包从 com.yourcompany.dronerent 移动到 com.xxq.dronerent

$sourcePath = "D:\test02\java\DroneRent\src\main\java\com\yourcompany\dronerent"
$targetPath = "D:\test02\java\DroneRent\src\main\java\com\xxq\dronerent"

Write-Host "开始移动文件..." -ForegroundColor Green

# 如果目标目录不存在，创建它
if (-not (Test-Path $targetPath)) {
    New-Item -ItemType Directory -Force -Path $targetPath | Out-Null
    Write-Host "创建目标目录: $targetPath" -ForegroundColor Yellow
}

# 复制所有文件和子目录
Copy-Item -Path "$sourcePath\*" -Destination $targetPath -Recurse -Force
Write-Host "文件已复制到: $targetPath" -ForegroundColor Green

# 删除源目录
Remove-Item -Path "D:\test02\java\DroneRent\src\main\java\com\yourcompany" -Recurse -Force
Write-Host "已删除源目录: D:\test02\java\DroneRent\src\main\java\com\yourcompany" -ForegroundColor Green

Write-Host "`n文件移动完成！" -ForegroundColor Green
Write-Host "新的包路径: com.xxq.dronerent" -ForegroundColor Cyan
