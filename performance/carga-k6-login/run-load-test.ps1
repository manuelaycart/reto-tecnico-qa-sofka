# -----------------------------------------------------------------------------
# Ejecuta la prueba de carga del login (Windows / PowerShell) y deja los
# reportes en .\reports  (summary.txt, summary.json y dashboard.html)
#
# Uso:
#   .\run-load-test.ps1
#   $env:TARGET_TPS = "40"; $env:STEADY_DURATION = "5m"; .\run-load-test.ps1
# -----------------------------------------------------------------------------
$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot
New-Item -ItemType Directory -Force -Path "reports" | Out-Null

$env:K6_WEB_DASHBOARD = "true"
$env:K6_WEB_DASHBOARD_PORT = "-1"
$env:K6_WEB_DASHBOARD_PERIOD = "5s"
$env:K6_WEB_DASHBOARD_EXPORT = "reports/dashboard.html"

Write-Host "Ejecutando la prueba de carga (duracion aproximada: 2 min 30 s con los valores por defecto)..."
k6 run --quiet --summary-export=reports/summary.json scripts/login-load-test.js | Tee-Object -FilePath "reports/summary.txt"
exit $LASTEXITCODE
