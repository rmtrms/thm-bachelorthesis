// SPDX-License-Identifier: GPL-2.0-only
/*
 * COW (Copy On Write) tests.
 *
 *
 */
#define _GNU_SOURCE
#include <s AD|PROT_WRITE) in the parent before write access.
	 */
	{
		"Basic COW after fork() with mprotect() optimization",
		test_cow_in_parent_mprotect,
	},
	/*
	 * vmsplice() [R/O GUP] + unmap in the child; modify in t OT_WRITE) in the parent before write access.
	 */
	{
		"vmsplice() + unmap in child with mprotect() optimization",
		test_vmsplice_in_child_mprotect,
	},
	/*
	 * vmsplice() [R/O GUP] in parent before fork(), unma 