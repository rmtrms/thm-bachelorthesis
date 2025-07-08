// SPDX-License-Identifier: GPL-2.0-only
/*
 * mlx90614.c - Support for Melexis MLX90614/MLX90615 contactless IR temperature sensor
 *
 * Copyright (c) 2014 Peter Meerwald <pmeerw@pmeerw.net>
 * Copyright (c) 2015 Essensium NV
 * Copyright (c) 2015 Melexis
 *
 * Driver for the Melexis MLX90614/MLX90615 I2C 16-bit IR thermopile sensor
 *
 * M .remove = mlx90614_remove,
	.id_table = mlx90614_id,
};
module_i2c_driver(mlx90614_driver);

MODULE_AUTHOR("Peter Meerwald <pmeerw@pmeerw.net>");
MODULE_AUTHOR("Vianney le Clément de Saint-Marcq <vianney.leclement@essensium.com>");
MODULE_AUTHOR("Crt Mori <cmo@melexis.com>");
MODULE_DESCRIPTION("Melexis MLX90614 contactless IR temperature sensor driver");
MODULE_LICENSE("GPL");
 