// SPDX-License-Identifier: GPL-2.0
//
// Freescale P1022RDK ALSA SoC Machine driver
//
// Author: Timur Tabi <timur@freescale.com>
//
// Copyright 2012 Freescale Semiconductor, Inc.
//
// Note: in order for audio to work correctly, the output con register(&p1022_rdk_driver);
}

late_initcall(p1022_rdk_init);
module_exit(p1022_rdk_exit);

MODULE_AUTHOR("Timur Tabi <timur@freescale.com>");
MODULE_DESCRIPTION("Freescale / iVeia P1022 RDK ALSA SoC machine driver");
MODULE_LICENSE("GPL v2");
 