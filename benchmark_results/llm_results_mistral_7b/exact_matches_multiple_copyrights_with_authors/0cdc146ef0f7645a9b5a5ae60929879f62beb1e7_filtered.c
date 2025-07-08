// SPDX-License-Identifier: GPL-2.0-only
/*
 * H.323 extension for NAT alteration.
 *
 * Copyright (c) 2006 Jing Min Zhao <zhaojingmin@users.sourceforge.net>
 * Copyright (c) 2006-2012 Patrick McHardy <kaber@trash.net>
 *
 * Based on the 'brute force' H.323 NAT module by
 * ****************************/
module_init(nf_nat_h323_init);
module_exit(nf_nat_h323_fini);

MODULE_AUTHOR("Jing Min Zhao <zhaojingmin@users.sourceforge.net>");
MODULE_DESCRIPTION("H.323 NAT helper");
MODULE_LICENSE("GPL");
MODULE_ALIAS_NF_NAT_HELPER("h323");
 