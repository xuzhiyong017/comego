#ifndef _SMARTRADIX_H_
#define _SMARTRADIX_H_

#include <stdio.h>
#include <stdarg.h>
#include <stdint.h>

/* meaning for params*/
#define __xout
#define __xin

/* Set up for C function definitions, even when using C++ */
#ifdef __cplusplus
extern "C" {
#endif

/**
 * */
typedef struct SmartRadix {
    const char* name;
    int radix;
} SmartRadix;

/**
 * */
typedef struct SmartRadixCon {
    const SmartRadix *radix;
    int len;
}SmartRadixCon;

/**
 * The Return Value Same As Snprintf
 * */
int smartRadixDesc(
        __xin const unsigned long long ssize[], 
        __xin const SmartRadixCon *con,
        __xin int idx, 
        __xin int smart,
        __xin size_t buflen,
        __xout char* buf); 

/**
 * */
int smartRadixChange(
        __xin unsigned long long size, 
        __xin const SmartRadixCon *con,
        __xin int maxu,
        __xout unsigned long long ssize[]); 

/**
 * sign : ssize[idx]
 * 
 * lfs > rfs : sign == 1
 * lfs == rfs: sign == 0
 * lfs < rfs : sign == -1
 * */
int smartRadixDiff(
        __xin unsigned long long lfs, 
        __xin unsigned long long rfs, 
        __xin const SmartRadixCon *con,
        __xin int maxu, 
        __xout unsigned long long ssize[]); 

int smartRadixDiffDesc(
        __xin unsigned long long lfs, 
        __xin unsigned long long rfs, 
        __xin int radixcnt, 
        __xin const SmartRadix radix[], 
        __xin int maxu, 
        __xin int smart,
        __xin const char* suffixs[],
        __xin size_t buflen,
        __xout char* buf
        );

/**
 * The Return Value Same As Snprintf
 * */
int smartSizeRadixDesc(
        __xin unsigned long long size,
        __xin int radixcnt, 
        __xin const SmartRadix radix[], 
        __xin int maxu, 
        __xin int smart,
        __xin size_t buflen,
        __xout char* buf); 

/**
 * @param size : the size in bytes
 * @param maxunit : the max unit of desc , 0 bytes, 1 KB, ... 8 ZB
 * @param smart : the desc will take care of smart number unit, example,  2GB 1MB, 1KB 200Bytes
 * */
int smartBytes(
        __xin unsigned long long size, 
        __xin int maxunit, 
        __xin int smart, 
        __xin size_t buflen, 
        __xout char *buf); 

int smartTime(
        __xin unsigned long long size, 
        __xin int maxunit, 
        __xin int smart, 
        __xin size_t buflen, 
        __xout char* buf); 

int smartTimeDiff(
        __xin unsigned long long lfs, 
        __xin unsigned long long rfs, 
        __xin int maxunit, 
        __xin int smart, 
        __xin size_t buflen, 
        __xout char* buf); 

/* Ends C function definitions when using C++ */
#ifdef __cplusplus
}
#endif

#endif
