"""
Análisis de la gráfica de monitoreo (VUs vs http_reqs) del Ejercicio 2 de performance.

1. Digitaliza la imagen original: recorre cada columna de píxeles, ubica la línea
   azul (http_reqs) y la línea verde (VUs) y convierte su posición a valores usando
   las marcas de los ejes.
2. Calcula los datos que se usan en el informe: throughput en régimen estable,
   ventana de degradación, tiempos implícitos por la Ley de Little y peticiones
   estimadas dentro de la ventana.
3. Genera las figuras 1 y 2 del informe.

Requisitos: Python 3.10+, pillow, matplotlib y numpy
    pip install pillow matplotlib numpy
Uso (desde esta carpeta):
    python analisis_grafica.py
"""
import csv
import statistics
from pathlib import Path

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.ticker import FuncFormatter, MultipleLocator
from PIL import Image

BASE = Path(__file__).resolve().parent
IMAGEN = BASE / "grafica-monitoreo-vus.png"
FIGURAS = BASE / "figuras"

# --- Calibración de ejes (píxeles de la imagen original de 725 x 433) ---------
Y_CERO = 307.0            # línea base (0 VUs y 0 req/s)
PX_POR_VU = 1.5           # 50 VUs cada 75 px en el eje izquierdo
PX_POR_REQ = 2.18         # 25 req/s cada ~54,5 px en el eje derecho
X_0140 = 151.5            # posición de la marca 01:40:00
PX_POR_MIN = (567.5 - 151.5) / 50.0   # 01:40 -> 02:30 = 50 minutos
COLUMNAS_TOOLTIP = range(343, 470)    # el recuadro del tooltip tapa la línea
COLUMNAS_MARCADOR = range(328, 345)   # punto verde del tooltip sobre la línea de VUs
THINK_TIME = 1.86 - 0.86168           # iteration_duration - http_req_duration
VUS = 140


def es_azul(p):
    r, g, b = p
    return b > 150 and r < 120 and g < 140 and (b - r) > 60


def es_verde(p):
    r, g, b = p
    return g > 90 and r < 80 and b < 120 and g > r + 40 and g > b


def minuto_del_dia(x):
    return 100 + (x - X_0140) / PX_POR_MIN   # 100 min = 01:40


def hhmmss(minuto):
    s = int(round(minuto * 60))
    return f"{s // 3600:02d}:{s % 3600 // 60:02d}:{s % 60:02d}"


def digitalizar():
    img = Image.open(IMAGEN).convert("RGB")
    reqs, vus = [], []
    for x in range(100, 645):
        azules = [y for y in range(60, 312) if es_azul(img.getpixel((x, y)))]
        verdes = [y for y in range(60, 312) if es_verde(img.getpixel((x, y)))]
        t = minuto_del_dia(x)
        if azules and x not in COLUMNAS_TOOLTIP:
            reqs.append((t, (Y_CERO - statistics.median(azules)) / PX_POR_REQ))
        if verdes and x not in COLUMNAS_MARCADOR:
            v = (Y_CERO - min(verdes)) / PX_POR_VU
            vus.append((t, float(VUS) if v >= 137 else v))
    vus = [(reqs[0][0], 0.0)] + vus + [(reqs[-1][0], 0.0)]
    return reqs, vus


def analizar(reqs, vus):
    inicio_meseta = next(t for t, v in vus if v >= VUS)
    fin_meseta = next(t for t, v in reversed(vus) if v >= VUS)
    meseta = [(t, r) for t, r in reqs if inicio_meseta <= t <= fin_meseta]
    bajos = [(t, r) for t, r in meseta if r < 60]
    v_ini, v_fin = bajos[0][0], bajos[-1][0]
    normal = [r for t, r in meseta if not (v_ini - 0.5 <= t <= v_fin + 0.5)]
    ventana = [r for t, r in meseta if v_ini <= t <= v_fin]
    profundo = [(t, r) for t, r in meseta if r < 30]
    media_profunda = statistics.mean(r for _, r in profundo)

    peticiones_ventana = 0.0
    for (t1, r1), (t2, r2) in zip(reqs, reqs[1:]):
        if v_ini <= t1 <= v_fin:
            peticiones_ventana += (r1 + r2) / 2 * (t2 - t1) * 60

    print(f"Meseta de {VUS} VUs: {hhmmss(inicio_meseta)} - {hhmmss(fin_meseta)}")
    print(f"Throughput estable: media {statistics.mean(normal):.1f} req/s "
          f"(min {min(normal):.1f}, max {max(normal):.1f})")
    print(f"Ventana de degradación: {hhmmss(v_ini)} - {hhmmss(v_fin)} "
          f"({v_fin - v_ini:.1f} min), media {statistics.mean(ventana):.1f} req/s, "
          f"mínimo {min(ventana):.1f} req/s")
    print(f"Tramo más profundo (< 30 req/s): {hhmmss(profundo[0][0])} - {hhmmss(profundo[-1][0])}, "
          f"media {media_profunda:.1f} req/s")
    print(f"Peticiones estimadas dentro de la ventana: {peticiones_ventana:,.0f}")
    print(f"Pausa por iteración (Z): {THINK_TIME:.2f} s")
    for etiqueta, x in [("estable", statistics.mean(normal)), ("02:02:00", 82.6),
                        ("tramo profundo", media_profunda), ("mínimo", min(ventana))]:
        print(f"  R ({etiqueta}) = {VUS} / {x:.1f} - {THINK_TIME:.2f} = {VUS / x - THINK_TIME:.2f} s")
    return statistics.mean(normal)


def figura_vus_throughput(reqs, vus, estable):
    texto, texto2, grilla, fondo = "#0b0b0b", "#52514e", "#e6e5e1", "#fcfcfb"
    azul, naranja, critico = "#2a78d6", "#eb6834", "#d03b3b"
    fig, (a1, a2) = plt.subplots(2, 1, figsize=(9.2, 5.6), sharex=True,
                                 gridspec_kw={"height_ratios": [1, 1.6], "hspace": 0.18})
    fig.patch.set_facecolor(fondo)
    for ax in (a1, a2):
        ax.set_facecolor(fondo)
        ax.axvspan(111.23, 118.2, color=critico, alpha=0.10, lw=0)
        ax.grid(axis="y", color=grilla, lw=0.8)
        ax.set_axisbelow(True)
        for lado in ("top", "right"):
            ax.spines[lado].set_visible(False)

    a1.plot([t for t, _ in vus], [v for _, v in vus], color=azul, lw=2)
    a1.set_ylim(0, 160)
    a1.yaxis.set_major_locator(MultipleLocator(50))
    a1.set_title("Usuarios virtuales (VUs)", loc="left", fontsize=10.5, color=texto, fontweight="bold")
    a1.text(126.5, 146, "140 VUs constantes durante ~60 min (01:36:48 – 02:36:40)",
            ha="center", color=texto2, fontsize=9)

    corte_a, corte_b = minuto_del_dia(343), minuto_del_dia(470)
    tramo1 = [(t, r) for t, r in reqs if t < corte_a]
    tramo2 = [(t, r) for t, r in reqs if t > corte_b]
    a2.plot([t for t, _ in tramo1], [r for _, r in tramo1], color=naranja, lw=1.6)
    a2.plot([t for t, _ in tramo2], [r for _, r in tramo2], color=naranja, lw=1.6)
    a2.plot([corte_a, corte_b], [tramo1[-1][1], tramo2[0][1]], color=naranja, lw=1.2,
            ls=(0, (2, 2)), alpha=0.7)
    a2.set_ylim(0, 105)
    a2.yaxis.set_major_locator(MultipleLocator(25))
    a2.yaxis.set_major_formatter(FuncFormatter(lambda v, _: f"{int(v)}/s"))
    a2.set_title("Throughput (http_reqs por segundo)", loc="left", fontsize=10.5, color=texto,
                 fontweight="bold")
    a2.axhline(estable, color=texto2, lw=0.9, ls=(0, (4, 3)))
    a2.text(101.0, 92.5, "Régimen estable ≈ 80 req/s", color=texto2, fontsize=9)
    a2.text(124.8, 66, "Tramo oculto por el tooltip\nde la imagen original", color=texto2,
            fontsize=8, linespacing=1.1)
    a2.annotate("Ventana de degradación\n01:51 – 01:58 (~7 min)\nmedia ≈ 22 req/s\nmínimo ≈ 8,5 req/s",
                xy=(112.6, 11), xytext=(97.0, 22), fontsize=8.8, color=texto,
                arrowprops=dict(arrowstyle="-", color=texto2, lw=0.8))
    a2.annotate("02:02:00 · 140 VUs\n82,6 req/s", xy=(122.0, 82.6), xytext=(124.3, 33),
                fontsize=9, color=texto, arrowprops=dict(arrowstyle="-", color=texto2, lw=0.8))
    a2.plot([122.0], [82.6], "o", ms=6, color=naranja, mec=fondo, mew=1.5)
    a2.xaxis.set_major_locator(MultipleLocator(10))
    a2.xaxis.set_major_formatter(FuncFormatter(lambda m, _: f"{int(round(m)) // 60:02d}:{int(round(m)) % 60:02d}"))
    a2.set_xlim(94, 159)
    a2.set_xlabel("Hora de la prueba (24/04/2025)", color=texto2)
    fig.savefig(FIGURAS / "figura1-vus-throughput.png", dpi=200, bbox_inches="tight", facecolor=fondo)
    plt.close(fig)


def figura_tiempos_respuesta():
    texto, texto2, grilla, fondo = "#0b0b0b", "#52514e", "#e6e5e1", "#fcfcfb"
    estadisticos = ["Promedio", "Mediana", "p(90)", "p(95)"]
    todas = [0.86168, 0.61342, 1.28, 1.57]          # textSummary: http_req_duration
    exitosas = [0.73584, 0.6007, 1.22, 1.42]        # textSummary: expected_response:true
    fig, ax = plt.subplots(figsize=(8.4, 3.9))
    fig.patch.set_facecolor(fondo)
    ax.set_facecolor(fondo)
    x = np.arange(len(estadisticos))
    ancho = 0.36
    b1 = ax.bar(x - ancho / 2 - 0.01, todas, ancho, color="#2a78d6", label="Todas las peticiones", zorder=3)
    b2 = ax.bar(x + ancho / 2 + 0.01, exitosas, ancho, color="#eb6834", label="Solo respuestas exitosas", zorder=3)
    for barras in (b1, b2):
        for b in barras:
            ax.text(b.get_x() + b.get_width() / 2, b.get_height() + 0.03,
                    f"{b.get_height():.2f} s".replace(".", ","), ha="center", va="bottom",
                    fontsize=8.8, color=texto, zorder=4,
                    bbox=dict(boxstyle="square,pad=0.12", fc=fondo, ec="none"))
    ax.axhline(1.5, color=texto2, lw=1.1, ls=(0, (5, 3)), zorder=2)
    ax.text(-0.45, 1.535, "Referencia 1,5 s", color=texto2, fontsize=9)
    ax.set_xticks(x, estadisticos)
    ax.set_ylim(0, 1.9)
    ax.yaxis.set_major_locator(MultipleLocator(0.5))
    ax.yaxis.set_major_formatter(FuncFormatter(lambda v, _: f"{v:.1f} s".replace(".", ",")))
    ax.grid(axis="y", color=grilla, lw=0.8)
    ax.set_axisbelow(True)
    for lado in ("top", "right"):
        ax.spines[lado].set_visible(False)
    ax.legend(frameon=False, loc="upper left", bbox_to_anchor=(0.0, 1.13), ncol=2, fontsize=9)
    fig.savefig(FIGURAS / "figura2-tiempos-respuesta.png", dpi=200, bbox_inches="tight", facecolor=fondo)
    plt.close(fig)


if __name__ == "__main__":
    plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 9.5, "axes.edgecolor": "#52514e",
                         "axes.labelcolor": "#52514e", "xtick.color": "#52514e", "ytick.color": "#52514e"})
    FIGURAS.mkdir(exist_ok=True)
    serie_reqs, serie_vus = digitalizar()
    with open(BASE / "serie-throughput-digitalizada.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["hora", "http_reqs_por_segundo"])
        for t, r in serie_reqs:
            w.writerow([hhmmss(t), f"{r:.1f}"])
    throughput_estable = analizar(serie_reqs, serie_vus)
    figura_vus_throughput(serie_reqs, serie_vus, throughput_estable)
    figura_tiempos_respuesta()
    print("Figuras generadas en", FIGURAS)
