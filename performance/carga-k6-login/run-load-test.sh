#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Ejecuta la prueba de carga del login y deja los reportes en ./reports
#   reports/summary.txt     resumen de fin de prueba (consola de k6)
#   reports/summary.json    metricas agregadas en formato JSON
#   reports/dashboard.html  reporte HTML con graficas de TPS, latencia y VUs
#
# Uso:
#   ./run-load-test.sh
#   TARGET_TPS=40 STEADY_DURATION=5m ./run-load-test.sh
# -----------------------------------------------------------------------------
set -euo pipefail

cd "$(dirname "$0")"
mkdir -p reports

export K6_WEB_DASHBOARD=true
export K6_WEB_DASHBOARD_PORT=-1        # no levanta el servidor web, solo exporta el HTML
export K6_WEB_DASHBOARD_PERIOD=5s
export K6_WEB_DASHBOARD_EXPORT=reports/dashboard.html

echo "Ejecutando la prueba de carga (duracion aproximada: 2 min 30 s con los valores por defecto)..."
k6 run --quiet --summary-export=reports/summary.json scripts/login-load-test.js | tee reports/summary.txt
