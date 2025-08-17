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
  - Ja solange erwähnt wird, dass es keine guten Monitoring-Möglichkeiten mit Ollama gibt, da es sich um eine Metrik handelt 
die bei jedem Modell gleich bemessen wird ist die Metrik aussagekräftig
  - Die genaue Zahl, in wie viele Tokens das jeweilige Modell den Prompt unterteilt ist auch zweitrangig, da die Metrik 
einfach aussagt, wie Lange das Modell für eine bestimmte Größe an Input braucht, die interne Verarbeitung ist für den Vergleich nebensächlich
- [Quelle](https://help.openai.com/en/articles/4936856-what-are-tokens-and-how-to-count-them) für Berechnung der Tokens

# Fragen Herr Klaus 07.08.2025

# Feedback

- Non-Commercial Creative Commons Lizenz

## Orga

- [x] Material für Kolloquium zusenden lassen
  - Mail senden und anfragen mit cc an Karsten
- [x] Exmatrikulation zum 31.09.2025 ansprechen (Vorgabe der Universität Mannheim)
- [x] Ampel und Meilensteine ansprechen
  - Frage beim Kolloqium: Warum hat es nicht geklappt? Herr Klein war informiert, Zeitmanagement hat nicht gepasst
- [x] Tipps für Time management für die restlichen drei Wochen, eventuell Gliederung durchsprechen und Kürzungen?
  - Pedantisches Zeitmanagement
  - Auszeiten gönnen

## Inhalt

- [x] Wo kann ich Analyse der False-Negatives des ScanCode-Toolkits mithilfe von LLM unterbringen? (welches Kapitel)
  - eventuell Prompt-Engineering
- [x] Wie gehe ich mit Anforderungen um?
  - meistens nicht-funktionale Anforderungen
  - müssen von Karsten angefragt werden
- [x] Wie viel von Policy darf ich einbringen?
  - kann gekürzt werden, die volle Länge ist aber auch kein Problem
- [x] Ist ausführlicher, rechtlicher Rahmen okay oder kürzen?
  - passt so
- [x] Wie ausführlich/kurz darf Fine-Tuning angesprochen werden?
  - Nutzen abschätzen
  - Chancen beschreiben
  - Ab wann ist etwas ein Erfolg? Wann ein Misserfolg? Was sind Mindestanforderungen?
- [x] Brauche ich eine Quelle für das Urheberrechtsgesetz?
  - einmal bei der ersten Erwähnung --> done
- [x] Wie mit unterschiedlichen Policy Versionen umgehen?
  - so wie bisher --> beschreiben welche Teile der policy bei welchem Stand verwendet werden
- [x] Wie soll das mehrstufige Vorgehen beim Benchmark beschrieben werden?
- [x] Sollen Probleme & die technische Lösung dieser in Implementierung beschrieben werden oder einem Abschnitt "Probleme"
  - Egal

- RAG in Ausblick mit rein bringen

# Fragen Karsten
- [x] Wie soll das Kundeninventar in der Arbeit erklärt werden? Darf erwähnt werden, dass es sich um ein Kundeninventar 
handelt?
  - nicht als Kundeninventar beschreiben, sondern einfach als Alpine Container, auf die Eigenschaften von Alpine eingehen
(Historie von Code, vielseitige Bestandteile, sowohl alte als auch neue Komponenten)
- [x] Gibt es Alternativen zu ScanCode? Eventuell Proprietär?
  - Scancode ist der Marktstandard und wird flächendeckend eingesetzt, es gibt proprietäre Lösungen (diese noch Nachschauen)
- [ ] Von Karsten Inventar zu Alpine Container zukommen lassen
- [x] Gemeinsam mit Karsten den MetaScan Prozess durchsprechen damit dieser in der Arbeit erklärt werden kann
- [x] Beispiel für CEP-04 geben lassen (evtl. GPL 2.0 mit Beispielen für Copyright Disclaimer gemeint?)
- [ ] Beispiel für CEP-06 geben lassen
- [x] klären was mit "wie oben" bei CEP-05 gemeint ist