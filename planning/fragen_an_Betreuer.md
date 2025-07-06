# Fragen Herr Klaus 03.07.2025
- [x] Wie sollen Internetquellen abgegeben/angegeben werden? (laut Vorlage soll ein PDF Download der Seite mit der 
Arbeit abgegeben werden)
  - Link reicht aus
- [x] Sollen/dürfen alle verwendeten Ressourcen mit Links angegeben werden? (Sprachmodelle auf Ollama/Github Repositories usw.)
  - so zitieren wie bereitgestellt, ansonsten Link
- [x] Darf Text in Abbildungen (siehe Sanky-Diagramm) auf Englisch sein?
  - ja
- [x] Wie sollen die Ergebnisse des Benchmarks in der Arbeit angehängt werden?
  - JSONS mit Ergebnissen müssen nicht abgegeben werden, auch nicht im Repo
- [x] Wie soll der Prozess der Datenaggregation beschrieben werden? Schritt für Schritt, sehr ausführlich?
  - Schritt für Schritt, zuerst was gemacht wurde, was war das Ergebnis, was lässt sich daraus feststellen/ableiten
- [x] Benötige ich für Metriken (F1 usw.) Quellen oder gilt dies als Allgemeinwissen? Kann hierzu ein Paper genannt werden
welches die Metriken erklärt/verwendet?
  - einfach ein Paper angeben, welches die Metrik verwendet/erklärt, kann auch als Begründung gegeben werden, warum die Metrik relevant ist
- [x] Müssen Metriken mit Formeln angegeben werden?
  - ja, [Quelle](https://en.wikipedia.org/wiki/F-score) für F1-Score
- [x] Wie viele Graphen/Tabellen darf ein Kapitel beinhalten?
  - So viele, dass noch relevante Informationen dargestellt werden, eher zu viel als zu wenig
- [x] Soll Karsten als **Stakeholder** oder als Haupt-Stakeholder genannt werden?
  - Mir überlassen, Stakeholder genügt
- [x] Etwa oder ca.?
  - "ca." ist wissenschaftlicher, "etwa" ist umgangssprachlich
- [x] ist das Approximieren der Tokens/sec so zulässig?
  - Ja solange erwähnt wird, dass es keine guten Monitoring Möglichkeiten mit Ollama gibt, da es sich um eine Metrik handelt 
die bei jedem Modell gleich bemessen wird ist die Metrik aussagekräftig
  - Die genaue Zahl, in wie viele Tokens das jeweilige Modell den Prompt unterteilt ist auch zweitrangig, da die Metrik 
einfach aussagt, wie Lange das Modell für eine bestimmte Größe an Input braucht, die interne Verarbeitung ist für den Vergleich nebensächlich
- [Quelle](https://help.openai.com/en/articles/4936856-what-are-tokens-and-how-to-count-them) für Berechnung der Tokens

# Fragen Karsten
- [x] Wie soll das Kundeninventar in der Arbeit erklärt werden? Darf erwähnt werden, dass es sich um ein Kundeninventar 
handelt?
  - nicht als Kundeninventar beschreiben, sondern einfach als Alpine Container, auf die Eigenschaften von Alpine eingehen
(Historie von Code, vielseitige Bestandteile, sowohl alte als auch neue Komponenten)
- [x] Gibt es Alternativen zu ScanCode? Eventuell Proprietär?
  - Scancode ist der Marktstandard und wird flächendeckend eingesetzt, es gibt proprietäre Lösungen (diese noch Nachschauen)
- [ ] Von Karsten Inventar zu Alpine Container zukommen lassen
- [ ] Gemeinsam mit Karsten den MetaScan Prozess durchsprechen damit dieser in der Arbeit erklärt werden kann
