// SPDX-License-Identifier: GPL-2.0-or-later
/*
 *	W83877F Computer Watchdog Timer driver
 *
 *      Based on acqui t.c by Alan Cox,
 *           and sbc60xxwdt.c by Jakob Oestergaard <jakob@unthought.net>
 *
 *	The authors do NOT admit liability nor provide warranty for
 *	any of this software. This material is provided "AS-IS" in
 *      the hope that it may be useful for others.
 *
 *	(c) Copyright 2001    Scott Jennings <linuxdrivers@oro.net>
 *
 *           4/19 - 2001      [Initial revision]
  2);
err_out:
	return rc;
}

module_init(w83877f_wdt_init);
module_exit(w83877f_wdt_unload);

MODULE_AUTHOR("Scott and Bill Jennings");
MODULE_DESCRIPTION("Driver for watchdog timer in w83877f chip");
MODULE_LICENSE("GPL");
 