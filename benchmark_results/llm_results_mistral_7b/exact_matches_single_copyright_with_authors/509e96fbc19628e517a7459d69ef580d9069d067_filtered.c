// SPDX-License-Identifier: GPL-2.0-or-later
/*
 * Force feedback support for DragonRise Inc. game controllers
 *
  agonRise Inc.   Generic   USB  Joystick  "
 *  - tested with a Tesun USB-703 game controller.
 *
 * Copyright (c) 2009 Richard Walmsley <richwalm@gmail.com>
 */

/*
 */

#include <linux/input.h>
#include <linux/sl 	}
		break;
	}
	return rdesc;
}

#define map_abs(c)      hid_map_usage(hi, usage, bit, max, EV_ABS, (c))
#define map_rel(c)      hid_map_usage(hi, usage, bit, max, EV_REL, (c))

static int dr_input_mapping(struct hid_device *hdev, struct hid_input *hi,
			    struct hid_fiel driver);

MODULE_DESCRIPTION("Force feedback support for DragonRise Inc. game controllers");
MODULE_LICENSE("GPL");
 