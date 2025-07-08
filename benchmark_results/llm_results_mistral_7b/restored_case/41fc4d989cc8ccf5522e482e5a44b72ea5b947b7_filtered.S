/* SPDX-License-Identifier: GPL-2.0 */

/*
 * memcmp for PowerPC32
 *
 * Copyright (C) 1996 Paul Mackerras.
 *
 */

#include <linux/export.h>
#include <asm/ppc_asm.h>

	.text

_GLOBAL(me 