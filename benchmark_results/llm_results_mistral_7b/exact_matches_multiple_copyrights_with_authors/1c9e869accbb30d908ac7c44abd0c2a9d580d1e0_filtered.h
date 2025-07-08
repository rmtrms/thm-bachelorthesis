/* SPDX-License-Identifier: GPL-2.0-only */
/*
 * Just-In-Time compiler for eBPF bytecode on 32-bit and 64-bit MIPS.
 *
 * Copyright (c) 2021 Anyfi Networks AB.
 * Author: Johan Almbladh <johan.almbladh@gmail.com>
 *
 * Based on code and ideas from
 * Copyright (c) 2017 Cavium, Inc.
 * Copyright (c) 2017 Shubham Bansal <illusionist.neo@gmail.com>
 * Copyright (c) 2011 Mircea Gherzan <mgherzan@gmail.com>
 */

#ifndef _BPF_JIT_COMP_H
#define _BPF_JIT_COMP_H

/* M AX_ITERATIONS	8

/*
 * Jump pseudo-instructions used internally
 * for branch conversion and branch optimization.
 */
#define JIT_JNSET	0xe0
#define JIT_JNOP	0xf0

/* Descriptor flag for PC-relative branch conver 