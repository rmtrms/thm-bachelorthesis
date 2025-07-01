# Benchmark Dataset

## Categories

- **no_copyrights_holders_authors**
  - 25 Einträge
  - Copyrights aus "exact_matches_with_single_copyright" (mit RandomFileCopier ausgewählt) genommen und copyrights aus Original file und .json entfernt

- **only_authors** ("holders_authors_without_copyrights")
  - 25 Einträge
  - Benötigt viel manuelle Pflege wegen Autoren
  - Scancode erkennt viele Arten von Autoren nicht (z.B. "Contributors")
  - Problem mit langen listen von Autoren ohne bestimmte Worte dazwischen, die der TextSieve erkennen könnte TODO: Lösung finden

- **restored_format**
  - 25 Einträge
  - bereits manuell geprüft

- **restored_case**
  - 25 Einträge
  - keine manuelle Pflege nötig
  - zu restored_case werden keine der extrahierten Unterkategorien mit in den Benchmark aufgenommen da diese Fälle ausreichend bei **exact_match*** behandelt werden

- **exact_matches_multiple_copyrights_with_authors**
  - 25 Einträge
  - benötigt viel manuelle Pflege wegen Autoren

- **exact_matches_multiple_copyrights_without_authors**
  - 25 Einträge
  - teils manuelle Pflege benötigt da bei "holders" oft Punkte "." mitgenommen

- **exact_matches_single_copyrights_with_authors**
  - 25 Einträgt (mit RandomFileCopier ausgewählt)
  - Bemerkt, dass files ohne copyright vorhanden sind, diese in einen anderen Ordner verschoben (10801 files). TODO: zahlen in Abbildungen anpassen, evtl. darüberliegende Datentöpfe auch bereinigen
  - manuelle Pflege bei Autoren nötig, oft werden mehrere Autoren in einen Eintrag gesetzt anstatt sie separat zu erfassen
  
- **exact_matches_single_copyrights_without_authors**
  - 25 Einträge (mit RandomFileCopier ausgewählt)
  - keine manuelle Pflege nötig

