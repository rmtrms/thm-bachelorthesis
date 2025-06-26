# Benchmark Sprachmodelle

## Tasks

- [x] Umgebung einrichten
- [ ] Modelle auswählen
- [ ] Prompt Engineering
- [ ] Daten generieren 
- [ ] Metriken bestimmen
- [ ] Daten auswerten

## Modelle für Benchmark

- Llama4
- Qwen3
- DeepSeekR1
- Gemma 3
- phi4

## Probleme

- Die Größe des Basis-Prompt zusammen mit dem Inhalt der Input-Datei sprengt den Kontext. Die Modelle generieren ab diesem Punkt nur noch schlechte Outputs. Um dies zu verhindern wird versucht, mit Hilfe eines TextSieve die Eingangsdatei auf Copyright relevante Inhalte zu kürzen und Codezeilen weitestgehend zu entfernen.
- Kleinere Modelle (phi2) scheinen die Aufgabe falsch zu verstehen. Statt dem erwarteten Output wird Code generiert, der die Aufgabe lösen soll, scheinbar verursachen die vorhandenen Code-Segmente diese falsche Einschätzung durch das Sprachmodell
