
Example :
Text:
/****************************************************************************
 * Copyright (c) 2011-2016 Synaptics Incorporated                           *
 * Copyright (c) 2011 Unixphere                                             *
 *                                                                          *

Output:
{
"copyrights" : [ "Copyright (c) 2011-2016 Synaptics Incorporated\nCopyright (c) 2011 Unixphere" ],
"holders" : [ "Synaptics Incorporated", "Unixphere" ],
"authors" : [ ]
}

Example :
Text:
* Copyright 2011-2012 Hauke Mehrtens <hauke@hauke-m.de>
*
* Based on ssb-ohci driver
* Copyright 2007 Michael Buesch <m@bues.ch>
*
* Derived from the OHCI-PCI driver
* Copyright 1999 Roman Weissgaerber
* Copyright 2000-2002 David Brownell
* Copyright 1999 Linus Torvalds
* Copyright 1999 Gregory P. Smith
*
* Derived from the USBcore related parts of Broadcom-SB
* Copyright 2005-2011 Broadcom Corporation

Output:
{
"copyrights" : [ "Copyright 2011-2012 Hauke Mehrtens <hauke@hauke-m.de>", "Copyright 2007 Michael Buesch <m@bues.ch>", "Copyright 1999 Roman Weissgaerber\nCopyright 2000-2002 David Brownell\nCopyright 1999 Linus Torvalds\nCopyright 1999 Gregory P. Smith", "Copyright 2005-2011 Broadcom Corporation" ],
"holders" : [ "Hauke Mehrtens <hauke@hauke-m.de>", "Michael Buesch <m@bues.ch>", "Roman Weissgaerber", "David Brownell", "Linus Torvalds", "Gregory P. Smith", "Broadcom Corporation" ],
"authors" : [ ]
}

Example :
Text:
/*
* Copyright (c) 2010-2011 Atheros Communications Inc.
* Copyright (c) 2011-2012 Qualcomm Atheros Inc.
*
* Permission to use, copy, modify, and/or distribute this software for any
* purpose with or without fee is hereby granted, provided that the above
* copyright notice and this permission notice appear in all copies.
*
* THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
* WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
* ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES

Output:
{
"copyrights" : [ "Copyright (c) 2010-2011 Atheros Communications Inc.\nCopyright (c) 2011-2012 Qualcomm Atheros Inc." ],
"holders" : [ "Atheros Communications Inc.", "Qualcomm Atheros Inc." ],
"authors" : [ ]
}
