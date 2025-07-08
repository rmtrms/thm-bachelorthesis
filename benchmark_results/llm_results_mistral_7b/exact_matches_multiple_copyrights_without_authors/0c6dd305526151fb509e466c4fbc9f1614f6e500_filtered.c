// SPDX-License-Identifier: GPL-2.0-or-later
/*
**  System Bus Adapter (SBA) I/O MMU manager
**
**	(c) Copyright 2000-2004 Grant Grundler <grundler @ parisc-linux x org>
**	(c) Copyright 2004 Naresh Kumar Inna <knaresh at india x hp x com>
**	(c) Copyright 2000-2004 Hewlett-Packard Company
**
**	Portions (c) 1999 Dave S. Miller (from sparc64 I/O MMU code)
**
**
**
** This module initializes the IOC (I/O Co ecutive free bits in resource bitmap.
 * Each bit represents one entry in the IO Pdir.
 * Cool perf optimization: search for log2(size) bits at a time.
 */
static unsigned long
sba_search_bitmap(struct ioc *ioc,  o the size of the range being purged. The size of the range
 * must be a power of 2. The "Cool perf optimization" in the
 * allocation routine helps keep that true.
 */
static void
sba_mark_invalid(struct ioc *io 