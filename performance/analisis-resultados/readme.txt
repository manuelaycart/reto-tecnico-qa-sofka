===============================================================================
 RETO TÉCNICO QA - EJERCICIO DE PERFORMANCE / EJERCICIO 2
 Análisis de resultados de una prueba de carga
 Autor: Manuel Aycart
===============================================================================

ENTREGABLE
----------
  InformeResultados.doc   Informe con hallazgos, conclusiones y recomendaciones,
                          incluida la relación entre usuarios virtuales y peticiones
                          por segundo de la gráfica de monitoreo.
  InformeResultados.pdf   Copia del mismo informe en PDF, para verlo sin Word.

CONTENIDO DEL INFORME
---------------------
  1. Resumen ejecutivo y resultado
  2. Alcance y metodología (perfil de carga reconstruido)
  3. Hallazgos: VUs vs. req/s, tiempos de respuesta, errores, tráfico y thresholds
  4. Conclusiones
  5. Recomendaciones priorizadas
  Anexo A. Cálculos (fórmulas de cada métrica derivada)
  Anexo B. Fuentes originales

INSUMOS DEL ANÁLISIS (carpeta insumos/)
---------------------------------------
  textSummary.txt / .png        resumen de fin de prueba entregado en el enunciado
  grafica-monitoreo-vus.png     gráfica de monitoreo original (VUs vs. http_reqs)
  analisis_grafica.py           digitaliza la gráfica, calcula los valores por tramo
                                (régimen estable, ventana de degradación, Ley de
                                Little) y genera las figuras del informe
  serie-throughput-digitalizada.csv   serie de req/s obtenida de la gráfica
  figuras/                      figuras 1 y 2 del informe

  Para reproducir el análisis (Python 3.10+):
    cd performance/analisis-resultados/insumos
    pip install pillow matplotlib numpy
    python analisis_grafica.py

  Los valores obtenidos de la gráfica son aproximados (lectura de píxeles de una
  imagen de 725 x 433). Las cifras del resumen de k6 se usan tal como aparecen.
