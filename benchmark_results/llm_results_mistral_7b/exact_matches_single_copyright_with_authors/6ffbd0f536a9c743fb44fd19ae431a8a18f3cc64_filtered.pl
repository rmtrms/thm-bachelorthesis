#! /usr/bin/env perl
# Copyright 2006-2020 The OpenSSL Project Authors. All Rights Reserved.
#
# Licensed under the Apache License 2.0 (the "License").  You may not use
# this file except in compliance with the License.  You can obtain a copy
# in the file LICENSE in the source distribution or at
# https://www.openssl.org/source/license.html

#
# ====================================================================
# Written by Andy Polyakov <appro@openssl.org> for the OpenSSL
# project. The module is, however, dual licensed under OpenSSL and
# CRYPTOGAMS licenses depending on where you obtain it. For further
# details see m$_",(8..10));
my $Kx="%xmm11";
my @V=($A,$B,$C,$D,$E)=("%eax","%ebx","%ecx","%edx","%ebp");	# size optimization
my @T=("%esi","%edi");
my $j=0;
my $rx=0;
my $K_XX_XX="%r14";
my $fp="%r11";

my $_rol=sub { &rol(@ 