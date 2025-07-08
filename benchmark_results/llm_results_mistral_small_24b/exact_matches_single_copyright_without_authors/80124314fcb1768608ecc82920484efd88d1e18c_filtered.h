/*
 * Copyright 2015-2021 The OpenSSL Project Authors. All Rights Reserved.
 *
 * Licensed under the Apache License 2.0 (the "License").  You may not use
 * this file except in compliance with the License.  You can obtain a copy
 * in the file LICENSE in the source distribution or at
 * https://www.openssl.org/source/license.html
 */

#include <stddef.h>
#include <openssl/ct.h>
#include <openssl/evp.h>
#include <openssl/x5 to read and write integers in network-byte order.
 */

#define n2s(c,s)        ((s=(((unsigned int)((c)[0]))<< 8)| \
                            (((unsigned int)((c)[1]))    )),c+=2)

#define s2n(s,c)        ((c[0]=(unsigned char)(((s)>> 8)&0xff), \
                             c[2]=(unsigned char)(((l)    )&0xff)),c+=3)

#define n2l8(c,l)       (l =((uint64_t)(*((c)++)))<<56, \
                         l|=((uint64_t)(*((c)++)))<<48, \
                         l|=((uint64_t)(*((c)++)))<<40, \
                         l|=((uint64_t)(*((c)++)))<<32, \
                         l|=((uint64_t)(*((c)++)))<<24, \
                         l|=((uint64_t)(*((c)++)))<<16, \
                         l|=((uint64_t)(*((c)++)))<< 8, \
                         l|=((uint64_t)(*((c)++))))

#define l2n8(l,c)       (*((c)++)=(unsigned char)(((l)>>56)&0xff), \
                         *((c)++)=(unsigned char)(((l)>>48)&0xff), \
                         *((c)++)=(unsigned char)(((l)>>40)&0xff), \
                         *((c)++)=(unsigned char)(((l)>>32)&0xff), \
                         *((c)++)=(unsigned char)(((l)>>24)&0xff), \
                         *((c)++)=(unsigned char)(((l)>>16)&0xff), \
                         *((c)++)=(unsigned char)(((l)>> 8)&0xff), \
                         *((c)++)=(unsigned char)(((l)    )&0xff))

/* Signed Certificate Timestamp */
struct sct_st {
    sct_ver ication context.
 */
void SCT_CTX_free(SCT_CTX *sctx);

/*
 * Sets the certificate that the SCT was created for.
 * If *cert does not have a poison extension, presigner must be NULL.
 * If *cert does not hav CTX *sctx, X509 *cert, X509 *presigner);

/*
 * Sets the issuer of the certificate that the SCT was created for.
 * This is just a convenience method to save extracting the public key and
 * calling SCT_CTX_ , const X509 *issuer);

/*
 * Sets the public key of the issuer of the certificate that the SCT was created
 * for.
 * The public key must not be NULL.
 * Returns 1 on success, 0 on failure.
 */
__owur int S 