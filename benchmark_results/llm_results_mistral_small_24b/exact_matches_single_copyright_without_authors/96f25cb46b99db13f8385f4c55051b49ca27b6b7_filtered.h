/* SPDX-License-Identifier: GPL-2.0+ */
/*
 * PowerPC Memory Protection Keys management
 *
 * Copyright 2017, Ram Pai, IBM Corporation.
 */

#ifndef _ASM_POWERPC_KEYS_H
#define _ASM_POWERPC_KEYS_H

#incl | VM_PKEY_BIT1 | VM_PKEY_BIT2 | \
			    VM_PKEY_BIT3 | VM_PKEY_BIT4)

/* Override any generic PKEY permission defines */
#define PKEY_DISABLE_EXECUTE   0x4
#define PKEY_ACCESS_MASK       (PKEY_DISABLE_ACCESS | -0 permissions.
	 * pkey-0 is associated with every page in the kernel.
	 * If userspace denies any permission on pkey-0, the
	 * kernel cannot operate.
	 */
	if (pkey == 0)
		return init_val ? -EINVAL : 0;

	r 