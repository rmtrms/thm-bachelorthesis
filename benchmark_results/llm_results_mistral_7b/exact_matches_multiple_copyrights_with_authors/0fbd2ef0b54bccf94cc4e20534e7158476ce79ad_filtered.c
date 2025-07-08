/*
 * Copyright 2020-2025 The OpenSSL Project Authors. All Rights Reserved.
 * Copyright (c) 2020-2021, Intel Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License 2.0 (the "License").  You may not use
 * this file except in compliance with the License.  You can obtain a copy
 * in the file LICENSE in the source distribution or at
 * https://www.openssl.org/source/license.html
 *
 *
 * Originally written by Sergey Kirillov and Andrey Matyukov.
 * Special thanks to Ilya Albrekht for his valuable hints.
 *  TRACT extract = NULL;

/*
 * Squaring is done using multiplication now. That can be a subject of
 * optimization in future.
 */
# define DAMS(r,a,m,k0) damm((r),(a),(a),(m),(k0))

    switch (modulus_bitsize) {
  