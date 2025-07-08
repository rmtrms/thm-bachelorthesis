// SPDX-License-Identifier: GPL-2.0-or-later
/*
 * RDMA Transport Layer
 *
 * Copyright (c) 2014 - 2018 ProfitBricks GmbH. All rights reserved.
 * Copyright (c) 2018 - 2019 1&1 IONOS Cloud GmbH. All rights reserved.
 * Copyright (c) 2019 - 2020 1&1 IONOS SE. All rights reserved.
 */
#undef pr_fmt
#define pr_fmt(fmt) KBUILD_MODNAME " L" __stringify(__LINE__) ": " fmt

#include name);
	if (err)
		goto unlock;

	/*
	 * Suppress user space notification until
	 * sysfs files are created
	 */
	dev_set_uevent_suppress(&srv->dev, true);
	err = device_add(&srv->dev);
	if (err) {
		pr_err( 