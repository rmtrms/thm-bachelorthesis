# Edge Cases

## Authors

### Case 1

Some authors provide a URL. 
- Is the URL considered part of the extracted author information?
- Is the url supposed to be normalized?

```
"author": "Dr.-Ing. Mario Heiderich, Cure53 <mario@cure53.de> (https://cure53.de/)",
```

--> keep URL `"Dr.-Ing. Mario Heiderich, Cure53 <mario@cure53.de> (https://cure53.de/)"`

```
* @author https://github.com/shellac
```

--> keep URL `"https://github.com/shellac"`

```
},
  "author": {
    "name": "Kir Belevich",
    "email": "kir@belevi.ch",
    "url": "https://github.com/deepsweet"
  },
  "contributors": [
    {
      "name": "Sergey Belov",
      "email": "peimei@ya.ru",
      "url": "https://github.com/arikon"
    }
```

--> keep URL `"Kir Belevich <kir@belevi.ch> https://github.com/deepsweet", "Sergey Belov <peimei@ya.ru> https://github.com/arikon"`

```
},
  "author": "Isaac Z. Schlueter <i@izs.me> (http://blog.izs.me/)",
  "license": "ISC"
}
```

--> keep URL `"Isaac Z. Schlueter <i@izs.me> (http://blog.izs.me/)"`

```
"author": {
		"name": "Mathias Bynens",
		"url": "https://mathiasbynens.be/"
	},
```

--> keep URL `"Mathias Bynens https://mathiasbynens.be/"`

```
"author": "The Babel Team (https://babel.dev/team)",
```

--> keep URL `"The Babel Team (https://babel.dev/team)"`

### Case 2

Some authors provide obfuscated e-mails.
- Do we want to normalize them?

```
! Contributed by Walter Spector <w6ws at earthlink dot net>
```

### Case 3

Some authors provide an affiliation.
- Do we want to extract them together with the name and e-mail?

```
 * Authors:
 *	Mitsuru KANDA @USAGI
 *	Kazunori MIYAZAWA @USAGI
 *	Kunihiro Ishiguro <kunihiro@ipinfusion.com>
 *		IPv6 support
 *	YOSHIFUJI Hideaki @USAGI
 *		Split up af-specific portion
```

--> keep affiliations `"Mitsuru KANDA @USAGI"`

```
/*
	MIT License http://www.opensource.org/licenses/mit-license.php
	Author Tobias Koppers @sokra
*/
```

--> keep affiliations `"Tobias Koppers @sokra"`

```
/*
	MIT License http://www.opensource.org/licenses/mit-license.php
	Author Florent Cailhol @ooflorent
*/
```

--> keep affiliations `"Florent Cailhol @ooflorent"`

### Case 4

Some contributions are hard to determine as authorship.

```
Author:
      - Hartmut Rick <linux@rick.claranet.de>

      - Special thanks to Jean Delvare for careful checking
	of the code and many helpful comments and suggestions.
```

--> `"Hartmut Rick <linux@rick.claranet.de>"`

```
/// Some inspiration was taken from Seatest by Keith Nicholas and
/// from STest which is a fork of Seatest by Jia Tan.
```

--> `"Jia Tan"`

```
! PR fortran/39414: PROCEDURE statement double declaration bug
!
! Discovered by Paul Thomas <pault@gcc.gnu.org>
! Modified by Janus Weil <janus@gcc.gnu.org>
```

--> `"Janus Weil <janus@gcc.gnu.org>"`

```
/*
    Vortex core low level functions.
	
 Author: Manuel Jander (mjander@users.sourceforge.cl)
 These functions are mainly the result of translations made
 from the original disassembly of the au88x0 binary drivers,
 written by Aureal before they went down.
 Many thanks to the Jeff Muizelaar, Kester Maddock, and whoever
 contributed to the OpenVortex project.
```

--> `"Manuel Jander (mjander@users.sourceforge.cl)", "Aureal", "Jeff Muizelaar", "Kester Maddock"`

```
! Reported by Tobias Burnus  <burnus@gcc.gnu.org>
! from http://j3-fortran.org/pipermail/j3/2010-December/004084.html
! submitted by Robert Corbett.
```

--> `"Robert Corbett"`

```
 * Authors:	Andreas Könsgen <ajk@comnets.uni-bremen.de>
 *              Ralf Baechle DL5RB <ralf@linux-mips.org>
 *
 * Quite a lot of stuff "stolen" by Joerg Reuter from slip.c, written by
 *
 *		Laurence Culhane, <loz@holmes.demon.co.uk>
 *		Fred N. van Kempen, <waltje@uwalt.nl.mugnet.org>
 */
```

--> `"Andreas Könsgen <ajk@comnets.uni-bremen.de>", "Ralf Baechle DL5RB <ralf@linux-mips.org>", "Joerg Reuter"`

```
// PR middle-end/65409
// Reported by Ignacy Gawedzki <bugs@qult.net>
```

--> none

```
*  Derived from LAPACK 3.0 routine CHGEQZ
*  Fails on i686-pc-cygwin with gcc-2.97 snapshots at -O2 and higher
*  PR fortran/1645
*
*  David Billinghurst, (David.Billinghurst@riotinto.com)
*  14 January 2001
*  Rewritten by Toon Moene (toon@moene.indiv.nluug.nl)
```

--> `"Toon Moene <toon@moene.indiv.nluug.nl>"`

### Case 5

Some authors have remarks like "and others" within the contributors list.
- Do we want to preserve this information?

```
Written by Natanael Copa <ncopa@alpinelinux.org>, Timo Teräs <timo.teras@iki.fi> and others.
```

--> maybe add "others" `"Natanael Copa <ncopa@alpinelinux.org>", "Timo Teräs <timo.teras@iki.fi>", "others"`

