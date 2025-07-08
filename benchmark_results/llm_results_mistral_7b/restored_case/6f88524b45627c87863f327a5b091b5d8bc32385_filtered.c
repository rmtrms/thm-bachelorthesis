// SPDX-License-Identifier: GPL-2.0-only
/*
 * Copyright (C) 2016 Texas Instruments Incorporated - https://www.ti.com/
 *
 * Author: Keerthy <j-keerthy@ti.com>
 */

#include <linux/interrupt.h>
#include <linux/mfd/core.h>
#include  	.probe		= lp873x_probe,
	.id_table	= lp873x_id_table,
};
module_i2c_driver(lp873x_driver);

MODULE_AUTHOR("J Keerthy <j-keerthy@ti.com>");
MODULE_DESCRIPTION("LP873X chip family Multi-Function Device driver");
MODULE_LICENSE("GPL v2");
 