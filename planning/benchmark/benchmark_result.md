# Ergebnisse der Modelle

---

## qwen3:4b

- abgebrochen, anfragen brauchen zum Teil 30+ Sekunden
  - Tokens/sec max ~ 20,7
  - Tokens/sec min ~ 3,8

---

## gemma3n:e4b

- abgebrochen
  - Tokens/sec ~ 15
  - gemma erzeugt bei jeder Antwort einen Typo für das Feld "author" --> "authoors"

---

## gemma3:4b

- abgebrochen
  - fällt in endlosschleife bei 01a0afbe1a4f41cf58f9ce47d65b31e3df7c8680.h

## Visualisierung der Ergebnisse

- x-Achse logarithmisch machen
- LLMs im Graphen einfärben
  - Selbe Farbe für verschiedene Größen des selben Modells
  - Unterschiedliche Farbtöne pro Modell von einem Provider
  - Unterschiedliche Farben pro provider

## Vergleich Mac Studio M4 Max 128GB vs. Mac Mini M4 Pro 64GB
| Modell            | Mac Mini M4 Pro                     | Mac Studio M4 Max                   |
|-------------------|-------------------------------------|-------------------------------------|
| Mistral:7b        | 15.982954225698766 avg. tokens/sec  | 33.067868658619275 avg. tokens/sec  |
| Mistral-small:24b | 6.0405880963133685 avg. tokens/sec  | 11.001220949632641 avg. tokens/sec  |

