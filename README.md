# Reto técnico – Analista de Automatización de Pruebas

[![E2E - Serenity BDD OpenCart](https://github.com/manuelaycart/reto-tecnico-qa-sofka/actions/workflows/e2e-serenity.yml/badge.svg)](https://github.com/manuelaycart/reto-tecnico-qa-sofka/actions/workflows/e2e-serenity.yml)
[![API - Karate PetStore](https://github.com/manuelaycart/reto-tecnico-qa-sofka/actions/workflows/api-karate.yml/badge.svg)](https://github.com/manuelaycart/reto-tecnico-qa-sofka/actions/workflows/api-karate.yml)
[![Performance - k6 login FakeStoreAPI](https://github.com/manuelaycart/reto-tecnico-qa-sofka/actions/workflows/performance-k6.yml/badge.svg)](https://github.com/manuelaycart/reto-tecnico-qa-sofka/actions/workflows/performance-k6.yml)

Solución de la **Prueba Técnica V9** (automatización funcional) y del **Ejercicio de Performance**.
Autor: **Manuel Aycart**.

## Resumen

| Parte | Ejercicio | Herramienta | Carpeta | Resultado |
|---|---|---|---|---|
| Prueba técnica | 1. E2E: compra como invitado en OpenCart | Serenity BDD 5.3.8 + Screenplay + Cucumber | [`automatizacion/e2e-serenity-opencart`](automatizacion/e2e-serenity-opencart) | Compra completada hasta "Your order has been placed!" |
| Prueba técnica | 2. API REST: usuarios de PetStore | Karate 1.5.2 | [`automatizacion/api-karate-petstore`](automatizacion/api-karate-petstore) | 7 de 7 escenarios aprobados |
| Performance | 1. Prueba de carga del login | k6 2.3.0 | [`performance/carga-k6-login`](performance/carga-k6-login) | 27 TPS, p95 514 ms, 0 % de errores |
| Performance | 2. Análisis de resultados | Informe | [`performance/analisis-resultados`](performance/analisis-resultados) | [`InformeResultados.doc`](performance/analisis-resultados/InformeResultados.doc) |

Cada ejercicio tiene su propio `readme.txt` (pasos de ejecución y versiones) y `conclusiones.txt`
(hallazgos y conclusiones), además de una carpeta de evidencias con los reportes de una ejecución real.

## Requisitos

| Herramienta | Versión | Se usa en |
|---|---|---|
| Java (JDK) | 17 o superior | E2E y API |
| Apache Maven | 3.9 o superior | E2E y API |
| Google Chrome | Última estable | E2E |
| k6 | 0.49 o superior (probado con 2.3.0) | Carga |

## Ejecución rápida

```bash
git clone https://github.com/manuelaycart/reto-tecnico-qa-sofka.git
cd reto-tecnico-qa-sofka

# Prueba técnica - Ejercicio 1: E2E (reporte en target/site/serenity/index.html)
cd automatizacion/e2e-serenity-opencart && mvn clean verify && cd ../..

# Prueba técnica - Ejercicio 2: API (reporte en target/karate-reports/karate-summary.html)
cd automatizacion/api-karate-petstore && mvn clean test && cd ../..

# Performance - Ejercicio 1: carga (reportes en reports/)
cd performance/carga-k6-login && ./run-load-test.sh && cd ../..
```

En Windows, la prueba de carga se ejecuta con `powershell -ExecutionPolicy Bypass -File .\run-load-test.ps1`.

## Estructura

```text
.
├── .github/workflows/            CI: un workflow por ejercicio, publica los reportes como artefactos
├── automatizacion/
│   ├── e2e-serenity-opencart/    Serenity BDD + Screenplay (Maven)
│   └── api-karate-petstore/      Karate (Maven)
└── performance/
    ├── carga-k6-login/           Script k6, CSV de usuarios y scripts de ejecución
    └── analisis-resultados/      InformeResultados.doc e insumos del análisis
```

## Integración continua

Cada ejercicio automatizado tiene un workflow de GitHub Actions que se ejecuta con cada cambio en su
carpeta y que también se puede lanzar manualmente desde la pestaña **Actions**. Al terminar, el
reporte (Serenity, Karate o k6) queda disponible como artefacto de la ejecución.

## Hallazgos destacados

- **OpenCart:** el título del carrito muestra "Use Gift Certificate" en lugar de "Shopping Cart", y
  varios productos destacados (MacBook, MacBook Air y MacBook Pro) están sin stock.
- **PetStore:** la API devuelve la contraseña del usuario en texto plano y permite crear, modificar y
  eliminar usuarios sin autenticación.
- **FakeStoreAPI:** estable a 30 TPS, sin errores y con todas las respuestas por debajo de 1 s.
- **Análisis de resultados:** la prueba cumple sus thresholds, pero tuvo una degradación de ~7 minutos
  (throughput -83 %) que concentra los errores; el detalle está en el informe.
