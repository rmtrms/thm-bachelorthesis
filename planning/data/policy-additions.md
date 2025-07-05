# Ergänzungen zur Policy / Besondere Fälle

## Punkt am Ende des Holders entfernen

- der ScanCode Service fügt die Punkte am Ende eines Copyrights hinzu, die extrahierten Holders enthalten diese dann oft auch.
- die Punkte am Ende sollen entfernt werden, falls diese nicht Teil der Bezeichnung des Holders sind.
- Der Punkt ist Teil der Bezeichnung bei z.B. kürzeln wie "Inc.", "Ltd." und "Corp.".

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

- Das ScanCode Toolkit extrahiert Autoren die als Liste vorliegen oft als einen String der mehrere Autoren enthält.
- Unsere Erwartungshaltung ist, dass Autoren, auch wenn sie in einem Block gelistet werden, einzeln erfasst werden.

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
"authors" : [ "Mengdong Lin <mengdong.lin@intel.com>", "Yao Jin <yao.jin@intel.com>", "Liam Girdwood <liam.r.girdwood@linux.intel.com>" ]
}
```

### Beispiel 2

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