/*

Copyright 2011, 2016, 2018 Free Software Foundation, Inc.

This file is part of the GNU MP Library test suite s free software; you can redistribute it
and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License,
or (at your option) any later version.

The GNU MP Library test suite is distributed in the hope t mplied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
Public License for more details.

You should have received a copy of the GNU General Public License along with
the GNU MP Library test suite.  If not, see https://www.gnu.org/licenses/.  */

#include , c, d;
  unsigned long abits, bbits;
  unsigned signs;

  mpz_init (a);
  mpz_init (b);
  mpz_init (c);
  mpz_init (d);

  if (op == OP_POWM)
    {
      unsigned long cbits;
      abits = gmp_urandomb_ rintf (cp, "%Zx", c);
  gmp_asprintf (dp, "%Zx", d);

  mpz_clear (a);
  mpz_clear (b);
  mpz_clear (c);
  mpz_clear (d);
}

void
hex_random_bit_op (enum hex_random_op op, unsigned long maxbits,
		   cha 