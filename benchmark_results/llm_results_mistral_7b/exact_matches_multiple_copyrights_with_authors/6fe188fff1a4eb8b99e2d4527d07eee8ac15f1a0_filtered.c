// SPDX-License-Identifier: GPL-2.0-or-later
/* SCTP kernel implementation
 * Copyright (c) 1999-2000 Cisco, Inc.
 * Copyright (c) 1999-2001 Motorola, Inc.
 * Copyright (c) 2001-2003 International Business Machines Corp.
 * Copyright (c) 2001 Intel Corp.
 * Copyright (c) 2001 La Monte H.P. Yarroll
 *
 * This file is part of the SCTP kernel implementation
 *
 * This mod  to the
 * email address(es):
 *    lksctp developers <linux-sctp@vger.kernel.org>
 *
 * Written or modified by:
 *    La Monte H.P. Yarroll <piggy@acm.org>
 *    Karl Knutson          <karl@athena.chicago.il.us  SACK including chunks
		 * acknowledged by the new Cumulative TSN Ack and by Gap
		 * Ack Blocks. (updated by RFC4960 Errata 3.22)
		 *
		 * When partial_bytes_acked is greater than cwnd and
		 * before the ar 