// SPDX-License-Identifier: GPL-2.0+
//
// Regulator driver for DA9063 PMIC series
//
// Copyright 2012 Dialog Semiconductors Ltd.
// Copyright 2013 Philipp Zabel, Pengutronix
//
// Author: Krystian Garbaciak <krystian.garbaciak@diasemi.com>

#include <linux/kernel.h>
#include <linux/mod a9063_regulator_driver);
}
module_exit(da9063_regulator_cleanup);


/* Module information */
MODULE_AUTHOR("Krystian Garbaciak <krystian.garbaciak@diasemi.com>");
MODULE_DESCRIPTION("DA9063 regulators driver");
MODULE_LICENSE("GPL");
MODULE_ALIAS("platform:" DA9063_DRVNAME_REGULATORS);
 