# Ergänzungen zur Policy / Besondere Fälle

## Punkt am Ende des Holders entfernen

- Der ScanCode Service (metaeffekt) fügt die Punkte am Ende eines Copyrights hinzu, die extrahierten Holders enthalten 
diese dann oft auch.
- Die Punkte am Ende sollen entfernt werden, falls diese nicht Teil der Bezeichnung des Holders sind.
- Der Punkt ist Teil der Bezeichnung bei z.B. Kürzeln wie:

- "Inc."
- "Ltd."
- "Corp."
- "LLC."
### Beispiel 1

```
|*       Copyright 1993-1999 NVIDIA, Corporation.  All rights reserved.      *|
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright 1993-1999 NVIDIA, Corporation.  All rights reserved." ],
"holders" : [ "NVIDIA, Corporation" ],
"authors" : [ ]
}
```

### Bespiel 2

```
*   Copyright (c) International Business Machines  Corp., 2000,2002
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright (c) International Business Machines  Corp., 2000,2002" ],
"holders" : [ "International Business Machines Corp." ],
"authors" : [ ]
}
```

---

## Autoren werden einzeln erfasst

- Das ScanCode Toolkit extrahiert Autoren, die als Liste vorliegen, oft als einen String der mehrere Autoren enthält.
- Unsere Erwartungshaltung ist, dass Autoren, auch wenn sie in einem Block gelistet werden, einzeln erfasst werden.

--> Diskussion: Maintainer sind möglicherweise keine Autoren da sie eventuell keinen sichtbaren Beitrag zum Material 
gelistet haben, hierzu wird eine Liste von Typen erstellt die erfasst werden, diese wird anschließend geprüft.

### Beispiel 1

```
Authors: Mengdong Lin <mengdong.lin@intel.com>
         Yao Jin <yao.jin@intel.com>
         Liam Girdwood <liam.r.girdwood@linux.intel.com>
```

#### extrahiertes JSON

```
{
"copyrights" : [ ],
"holders" : [ ],
"authors" : [ "Mengdong Lin <mengdong.lin@intel.com>", "Yao Jin <yao.jin@intel.com>", 
"Liam Girdwood <liam.r.girdwood@linux.intel.com>" ]
}
```

### Beispiel 2

- In diesem Beispiel ist eine sehr komplexe Variante von Autorenkennzeichen zu sehen, es werden verschiedene Arten von
Beitragenden (Maintainer, Developer, Contributors, usw.) genannt. das ScanCode Toolkit erkennt hier lediglich Jim Gettys 
und Warren Turkal.

```
Once upon a midnight hour, long ago, in a galaxy, far, far, away, Xlib
was originally developed by Jim Gettys, of Digital Equipment
Corporation (now part of HP).

Warren Turkal did the autotooling in October, 2003.

Josh Triplett, Jamey Sharp, and the XCB team (xcb@lists.freedesktop.org)
maintain the XCB support.

Individual developers include (in no particular order): Sebastien
Marineau, Holger Veit, Bruno Haible, Keith Packard, Bob Scheifler,
Takashi Fujiwara, Kazunori Nishihara, Hideki Hiura, Hiroyuki Miyamoto,
Katsuhisi Yano, Shigeru Yamada, Stephen Gildea, Li Yuhong, Seiji Kuwari.

The specifications and documentation contain extensive credits.
Conversion of those documents from troff to DocBook/XML was performed
by Matt Dew, with assistance in editing & formatting tool setup from
Gaetan Nadon and Alan Coopersmith.
```

#### extrahiertes JSON

```
{
"copyrights" : [ ],
"holders" : [ ],
"authors" : [ "Jim Getty", "Warren Turkal", "Josh Triplett", "Jamey Sharp", "the XCB team (xcb@lists.freedesktop.org)", 
"Sebastian Marineau", "Holger Veit", "Bruno Haible", "Keith Packard", "ob Scheifler", "Takashi Fujiwara", 
"Kazunori Nishihara", "Hideki Hiura", "Hiroyuki Miyamoto", "Katsuhisi Yano", "Shigeru Yamada", "Stephen Gildea", 
"Li Yuhong", "Seiji Kuwari", "Matt Dew", "Gaetan Nadon", "Alan Coopersmith" ]
}
```

---

## Holders werden einzeln erfasst

- Das Scancode Toolkit ist bei der Extraktion der Holders inkonsistent, manchmal werde diese getrennt erfasst, manchmal
in einem String inkl. Satzbau (z.B. "and").
- Unsere Erwartungshaltung ist, dass sowohl mehrere Holders eines Copyrights als auch mehrere Holders unterschiedlicher
Copyrights als einzelne Elemente unter "Holders" erfasst werden.

### Beispiel 1

```
//     (c) 2009-2021 Jeremy Ashkenas, Julian Gonggrijp, and DocumentCloud and Investigative Reporters & Editors
```

#### extrahiertes JSON

```
{
"copyrights" : [ "(c) 2009-2021 Jeremy Ashkenas, Julian Gonggrijp, and DocumentCloud and Investigative Reporters &
 Editors" ],
"holders" : [ "Jeremy Ashkenas", "Julian Gonggrijp", "DocumentCloud" ", "Investigative Reporters & Editors" ],
"authors" : [ ]
}
```

### Beispiel 2

```
 * Copyright 2008 Cisco Systems, Inc.  All rights reserved.
 * Copyright 2007 Nuova Systems, Inc.  All rights reserved.
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright 2008 Cisco Systems, Inc.  All rights reserved.\nCopyright 2007 Nuova Systems, Inc.  All
 rights reserved." ],
"holders" : [ "Nuova Systems, Inc.", "Cisco Systems, Inc." ],
"authors" : [ ]
}
```

---

## "Verweise auf Teile" werden einzeln erfasst

- In einigen Fällen werden Verweise gemacht, dass Teile einer Software auf einer Anderen basieren, die unter einem 
anderen Copyright stehen.
- Unsere Erwartungshaltung ist, dass das Copyright des Teils einzeln erfasst wird und der Verweis auf den Ursprung 
dieses Copyrights nicht Teil des Statements ist.

### Beispiel

- In diesem Beispiel handelt es sich um zwei getrennte Copyright-Statements bei dem der zweite den genannten Vermerk 
hat.

```
 * Copyright(c) 2008 - 2010 Realtek Corporation. All rights reserved.
 *
 * Based on the r8180 driver, which is:
 * Copyright 2004-2005 Andrea Merello <andrea.merello@gmail.com>, et al.
```

--> Diskussion: Policy um "all remarks that help identify the material or holder" ergänzen, somit würde der "Based on 
the..." Verweis Teil des Statement werden.

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright 2004-2005 Andrea Merello <andrea.merello@gmail.com>, et al.", "Copyright(c) 2008 - 2010
 Realtek Corporation. All rights reserved." ],
"holders" : [ "Realtek Corporation", "Andrea Merello et al." ],
"authors" : [ ]
}
```

---

## E-Mails werden bei Autoren erfasst, bei Holders auch

- Das Scancode Toolkit erfasst Holders ohne E-Mail-Adressen, wenn diese vorhanden sind. Bei Autoren hingegen werden 
diese übernommen.
- Da die E-Mail-Adressen zur Kontaktaufnahme mit dem Autor dienen, bzw. zur weiteren Identifikation des Holders 
beitragen, werden diese übernommen.

### Beispiel 1

```
 * Copyright 2004-2005 Andrea Merello <andrea.merello@gmail.com>, et al.
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright 2004-2005 Andrea Merello <andrea.merello@gmail.com>, et al." ],
"holders" : [ "Andrea Merello <andrea.merello@gmail.com>, et al." ],
"authors" : [ ]
}
```

### Beispiel 2

```
 Translation from C++ and adaptation for use in ALSA-Driver
 were made by Giuliano Pochini <pochini@shiny.it>
```

#### extrahiertes JSON

```
{
"copyrights" : [ ],
"holders" : [ ],
"authors" : [ "Giuliano Pochini <pochini@shiny.it>" ]
}
```

---

## Kontaktinformationen implizieren kein "Authorship"

- Manche Copyrights sind mit zusätzlichen Kontaktinformationen versehen, das ScanCode Toolkit erfasst diese nicht als 
Autoren.
- Unsere Erwartungshaltung ist, dass diese Kontaktinformationen keine Authorship implizieren, sondern nur eine 
Anlaufstelle für Fragen o.Ä. darstellen und deckt sich somit mit dem ScanCode Ergebnis.
- Frage: Soll diese Information anderweitig erfasst werden?
  - Ja, eventuell kann eine neue Kategorie "contact information" abgeleitet werden, für die Copyrights an sich spielt es allerdings keine Rolle
- Frage: Wenn Kontaktinformationen zusätzliche Möglichkeiten bieten, den Holder zu identifizieren oder Kontakt aufzunehmen, werden diese Teil des Holders?
  - Nein sie werden nicht dem Holder ergänzt, können aber eventuell als neue Information "contact information" erfasst werden

## Beispiel

```
 * Copyright(c) 2010 Larry Finger. All rights reserved.
 *
 * Contact information:
 * WLAN FAE <wlanfae@realtek.com>
 * Larry Finger <Larry.Finger@lwfinger.net>
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright(c) 2010 Larry Finger. All rights reserved." ],
"holders" : [ "Larry Finger" ],
"authors" : [ ]
}
```

---

## URLs am Ende des Copyright-Statements sind Teil des Statements 

- Manche Copyrights werden zusammen mit einer URL angegeben.
- Die URL wird als Teil des Copyrights gesehen, wenn anhand der Formatierung eine Zugehörigkeit zum Statement erkennbar ist.

## Beispiel 1

```
Copyright Echo Digital Audio Corporation (c) 1998 - 2004
All rights reserved
www.echoaudio.com
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright Echo Digital Audio Corporation (c) 1998 - 2004\n   All rights reserved\nwww.echoaudio.com" ],
"holders" : [ "Echo Digital Audio Corporation" ],
"authors" : [ ]
}
```

## Beispiel 2

```
// Copyright (c) 2011 Samsung Electronics Co., Ltd
//              http://www.samsung.com
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright (c) 2011 Samsung Electronics Co., Ltd\n             http://www.samsung.com" ],
"holders" : [ "Samsung Electronics Co., Ltd" ],
"authors" : [ ]
}
```

---

## Sonderfälle

### Sonderfall 1

- In diesem Sonderfall ist der Holder einerseits explizit mit E-Mail-Adresse genannt und andererseits sind weitere mit 
"et al." vermerkt.
- Da der Vermerk "et al." weitere Holders impliziert, muss der Vermerk erhalten bleiben.

```
 * Copyright 2004-2005 Andrea Merello <andrea.merello@gmail.com>, et al.
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright 2004-2005 Andrea Merello <andrea.merello@gmail.com>, et al." ],
"holders" : [ "Andrea Merello <andrea.merello@gmail.com>, et al." ],
"authors" : [ ]
}
```

### Sonderfall 2

- In diesem Sonderfall gibt es zwar einen Verweis auf einen Teil (siehe oben), der unter einem anderen Copyright steht, 
hier wirkt aber die Regel, dass Blöcke von Statements nicht aufgelöst werden stärker und somit wird das Statement inkl.
Verweis erfasst, da das Entfernen des "Portions"-Verweis zu sehr eine Interpretation des Copyrights darstellen würde.

```
* Copyright (c) 1996-1998 John D. Polstra.  All rights reserved.
* Copyright (c) 2001 David E. O'Brien
* Portions Copyright 2009 The Go Authors. All rights reserved.
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright (c) 1996-1998 John D. Polstra.  All rights reserved.\nCopyright (c) 2001 David E. O'Brien\n
Portions Copyright 2009 The Go Authors. All rights reserved." ],
"holders" : [ "John D. Polstra", "David E. O'Brien", "The Go Authors" ],
"authors" : [ ]
}
```

---

## Diskussion

### Fall 1

- Können E-Mails normalisiert werden?

```
 *   Copyright (c) International Business Machines  Corp., 2000,2002
 *   Modified by Steve French (sfrench@us.ibm.com)
```

--> Ja E-Mails können normalisiert werden, die Hauptsache ist, dass sie nach wie vor korrekt angegeben ist.

#### extrahiertes JSON

```
{
"copyrights" : [ "opyright (c) International Business Machines  Corp., 2000,2002" ],
"holders" : [ "International Business Machines  Corp.," ],
"authors" : [ "Steve French sfrench@us.ibm.com"]
}
```
---

### Fall 2

- Werden Whitespaces vor dem Statement erhalten, oder nur innerhalb des Blocks?
  - Nur innerhalb des Blocks, führende Leerzeichen im Statement erfüllen keinen semantischen Zweck.

```
|*                                                                           *|
|*       Copyright 1993-1999 NVIDIA, Corporation.  All rights reserved.      *|
|*                                                                           *|
|*     NOTICE TO USER:   The source code  is copyrighted under  U.S. and     *|
```

#### extrahiertes JSON

```
{
"copyrights" : [ "  Copyright 1993-1999 NVIDIA, Corporation.  All rights reserved." ],
"holders" : [ "NVIDIA, Corporation" ],
"authors" : [ ""]
}
```

---

### Beispiel für Formatierung

```
// Copyright (c) 2023 Cirrus Logic, Inc. and
//                    Cirrus Logic International Semiconductor Ltd.
```

#### extrahiertes JSON

```
{
"copyrights" : [ "Copyright (c) 2023 Cirrus Logic, Inc. and\                   Cirrus Logic International Semiconductor Ltd." ],
"holders" : [ "Cirrus Logic, Inc.", "Cirrus Logic International Semiconductor Ltd." ],
"authors" : [ ]
}
```

