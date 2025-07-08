// SPDX-License-Identifier: GPL-2.0
// Copyright (c) 2019 Facebook
#include <linux/bpf.h>
#include <linux/version.h>
#include <bpf/bpf_helpers.h>

#defi  < VAR_NUM; i++)
		q->var[i] = rnd;
	bpf_spin_unlock(&q->lock);
	err = 0;
err:
	return err;
}
char _license[] SEC("license") = "GPL";
 