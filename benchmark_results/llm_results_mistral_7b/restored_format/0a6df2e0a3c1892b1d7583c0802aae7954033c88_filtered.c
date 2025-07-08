/*

Copyright 1989, 1998  The Open Group

Permission to use, copy, modify, distribute, and sell this software and its
documentation for any purpose is hereby granted without fee, provided that
the above copyright notice appear in all copies and that both that
copyright notice and this permission notice appear in supporting
documentation.

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PR 
other dealings in this Software without prior written authorization
from The Open Group.

*/
/*
 * Copyright 1995 by FUJITSU LIMITED
 * This is source code modified by FUJITSU LIMITED under the Joint
 * Development Agreement for the CDE/Motif PST.
 */


#ifdef HAVE_C ersion used in XTextExt16.c since row == 0.  This is used when
 * max_byte2 is not zero.  A further optimization would do the check for
 * min_byte1 being zero ahead of time.
 */

#define CI_GET_ROWZERO_CHAR_INFO racter information */
{
    int i;				/* iterator */
    Bool singlerow = (fs->max_byte1 == 0);  /* optimization */
    int nfound = 0;			/* number of characters found */
    XCharStruct *def;			/* info about default char */
    unsigned char *us;  		/* be 8bit clean */

    if (singlerow) {			/* optimization */
	CI_GET_DEFAULT_INFO_1D (fs, def);
    } else {
	CI_GET_DEFAULT_INFO_2D (fs, def);
    }

    *d c = (unsigned) *us;	/* since about to do macro */
	register XCharStruct *cs;

	if (singlerow) {		/* optimization */
	    CI_GET_CHAR_INFO_1D (fs, uc, def, cs);
	} else {
	    CI_GET_ROWZERO_CHAR_INFO_2D (fs, uc,  tring,
    int count)
{
    int i;				/* iterator */
    Bool singlerow = (fs->max_byte1 == 0);  /* optimization */
    XCharStruct *def;			/* info about default char */
    unsigned char *us;  		/* be 8bit clean */
    int width = 0;			/* RETURN value */

    if (singlerow) {			/* optimization */
	CI_GET_DEFAULT_INFO_1D (fs, def);
    } else {
	CI_GET_DEFAULT_INFO_2D (fs, def);
    }

    if c = (unsigned) *us;	/* since about to do macro */
	register XCharStruct *cs;

	if (singlerow) {		/* optimization */
	    CI_GET_CHAR_INFO_1D (fs, uc, def, cs);
	} else {
	    CI_GET_ROWZERO_CHAR_INFO_2D (fs, uc,  tring,
    int count)
{
    int i;				/* iterator */
    Bool singlerow = (fs->max_byte1 == 0);  /* optimization */
    XCharStruct *def;			/* info about default char */
    unsigned char *us;  		/* be 8bit clean */
    int height = 0;			/* RETURN value */

    if (singlerow) {			/* optimization */
	CI_GET_DEFAULT_INFO_1D (fs, def);
    } else {
	CI_GET_DEFAULT_INFO_2D (fs, def);
    }

    if c = (unsigned) *us;	/* since about to do macro */
	register XCharStruct *cs;

	if (singlerow) {		/* optimization */
	    CI_GET_CHAR_INFO_1D (fs, uc, def, cs);
	} else {
	    CI_GET_ROWZERO_CHAR_INFO_2D (fs, uc,  