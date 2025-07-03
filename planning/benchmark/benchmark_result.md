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