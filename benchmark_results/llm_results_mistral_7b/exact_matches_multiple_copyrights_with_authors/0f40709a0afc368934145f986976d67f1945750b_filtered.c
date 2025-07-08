// SPDX-License-Identifier: GPL-2.0-only
/*
 * Mac80211 SPI driver for ST-Ericsson CW1200 device
 *
 * Copyright (c) 2011, Sagrad Inc.
 * Author:  Solomon Peachy <speachy@sagrad.com>
 *
 * Based on cw1200_sdio.c
 * Copyright (c) 2010, ST-Ericsson
 * Author: Dmitry Tarnyagin <dmitry.tarnyagin@lockless.no>
 */

#include <linux/module.h>
#include <linux/gpi "cw1200.h"
#include "hwbus.h"
#include <linux/platform_data/net-cw1200.h>
#include "hwio.h"

MODULE_AUTHOR("Solomon Peachy <speachy@sagrad.com>");
MODULE_DESCRIPTION("mac80211 ST-Ericsson CW1200 SPI driver");
MODULE_LICENSE("GPL");
MODULE_ALIAS("spi:cw1200_wlan_spi");

/* #define SPI_DEBUG */

struct hwbus_priv {
	struct  