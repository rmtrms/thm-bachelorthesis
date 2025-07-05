# Vergleich der zwei besten Modelle aus dem Benchmark

- mistral:7b und qwen2.5-coder:7b haben die besten Ergebnisse im Benchmark erzielt, ihre Performance war dabei fast identisch

## Qwen2.5-coder Varianten und ihre Performance

- Da qwen2.5-coder in mehreren Parametergrößen verfügbar ist, wurden alle größen miteinander verglichen
  - 1,5b: konnte den Benchmark nicht beenden
  - 3b: hat schnelle aber sehr unpräzise Ergebnisse erzielt
  - 7b: hat die besten Ergebnisse erzielt mit guter Performance, vergleichbar mit anderen 7b Modellen
  - 14b: langsamer als 7b und leicht schlechtere Ergebnisse
  - 32b: am langsamsten und leicht schlechter als 14b und 7b (bei exact matches sogar deutlich schlechter)

## Mistral Modelle für Benchmark

- mistral:7b
- devstrahl:24b
- mistral-nemo:12b
- mistral-small:22b/24b
- mixtral:8x7b/8x22b
- mistral-small3.1:24b
- mistral-small3.2:24b
- magistral:24b
- mathstral:7b

## Argumente für Mistral

- Europäisches Produkt (Qwen ist eine Entwicklung von Alibaba)
- Multilingual (laut Klaus performed Mistral sehr gut)
- höchster Prozentsatz exact matches (80%)

**Aber**
- nur eine Größe verfügbar
- relativ viele false-positives bei Fällen ohne copyrights

## Argumente für Qwen

- Viele Parametergrößen verfügbar
- Höchster F1 Score im Benchmark
- Robust bei Fällen ohne copyrights

**Aber**
- geringere exact matches als mistral
- Chinesisches Produkt (löst Unmut bei manchen aus)
- verschiedene Parametergrößen liefern kaum Verbesserung, 7b Variante ist bereits die beste für die Aufgabe