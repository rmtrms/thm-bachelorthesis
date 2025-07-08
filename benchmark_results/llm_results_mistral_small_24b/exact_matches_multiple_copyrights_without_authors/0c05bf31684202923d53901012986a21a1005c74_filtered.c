// SPDX-License-Identifier: GPL-2.0-only
/*
 * PowerMac G5 SMU driver
 *
 * Copyright 2004 J. Mayer <l_indien@magic.fr>
 * Copyright 2005 Benjamin Herrenschmidt, IBM Corp.
 */

/*
 * TODO:
 *  - maybe add timeout to commands ?
 *  - ude <asm/smu.h>
#include <asm/sections.h>
#include <linux/uaccess.h>

#define VERSION "0.7"
#define AUTHOR  "(c) 2005 Benjamin Herrenschmidt, IBM Corp."

#undef DEBUG_SMU

#ifdef DEBUG_SMU
#define DPRINTK(fmt, ar smu");
        if (np == NULL)
		return -ENODEV;

	printk(KERN_INFO "SMU: Driver %s %s\n", VERSION, AUTHOR);

	/*
	 * SMU based G5s need some memory below 2Gb. Thankfully this is
	 * called at a time where  