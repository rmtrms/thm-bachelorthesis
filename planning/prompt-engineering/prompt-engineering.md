# Prompt Engineering

# Schritt 1 - Benchmark Daten überarbeiten

Da die Policy seit dem Benchmark überarbeitet wurde, muss der Benchmark Datensatz hin zur neuen Policy angepasst werden.
Dabei wird Folgendes verändert:

- Holder enthalten nun Email-Adressen
- Email Adressen in Holders & Authors werden normalisiert
- Punkt am Ende des Holders entfernt 
- Blöcke von Copyrights werden als Blöcke extrahiert
- Maintainer sind keine Autoren