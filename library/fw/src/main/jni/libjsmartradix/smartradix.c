#include <stdlib.h>
#include <memory.h>
#include "smartradix.h"

#define __CP(...) (__VA_ARGS__)
#ifndef __countof
#define __countof(array) (sizeof(array)/sizeof(array[0]))
#endif

/**
 * The Return Value Same As Snprintf
 * */
int smartRadixDesc(
        __xin const unsigned long long ssize[], 
        __xin const SmartRadixCon *con,
        __xin int idx, 
        __xin int smart,
        __xin size_t buflen,
        __xout char* buf) {
    // build the size unit string
    int len = 0; 
    int offset = 0;
    if (idx) do {
        if (ssize[idx-1] == 0) {
            continue;
        }
        offset = snprintf(buf+len, buflen-len, "%llu%s", ssize[idx-1], con->radix[idx-1].name);
        /**
         * http://www.cplusplus.com/reference/cstdio/snprintf/
         * int snprintf ( char * s, size_t n, const char * format, ... );
         * 
         * The number of characters that would have been written if n had been sufficiently large, 
         * not counting the terminating null character.
         * If an encoding error occurs, a negative number is returned.
         * Notice that only when this returned value is non-negative and less than n, 
         * the string has been completely written.
         * */
        if (offset < 0 || offset >= (int)buflen) {
            break;
        }
        len += offset;
    } while(--idx>0 && --smart>0 && offset > 0);

    return len;
}

/**
 * */
int smartRadixChange(
        __xin unsigned long long size, 
        __xin const SmartRadixCon *con,
        __xin int maxu,
        __xout unsigned long long ssize[]) {
    unsigned short idx = 0;
    ssize[0] = size;

    __CP("total-size %llu\n", size);

    // calc size
    if (size) do {
        ssize[idx+1] = ssize[idx] / con->radix[idx].radix;
        ssize[idx] = ssize[idx] % con->radix[idx].radix; 

        __CP("offset[%d]-size %llu, %llu\n", idx, ssize[idx], ssize[idx+1]);
    } while(++idx<con->len && ssize[idx] > 0 && --maxu>=0);

    // max calc unit
    if (ssize[idx] > 0 ) {
        ssize[idx-1] = (ssize[idx] * con->radix[idx-1].radix) + ssize[idx-1];
    }
    return idx;
}

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
        __xout unsigned long long ssize[]) {
    signed int sign = lfs > rfs ? 1 : (lfs == rfs ? 0 : -1);
    unsigned long long diff = sign > 0 ? lfs - rfs : rfs - lfs;
    unsigned short idx = smartRadixChange(diff, con, maxu, ssize);
    ssize[idx] = sign; // append the sign to ssize
    return idx;
}

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
        ) {
    int len = 0;
    unsigned short idx = 0;
    unsigned long long *ssize;
    unsigned long long ssizedefaults[10] = {0};
    SmartRadixCon con = {radix, radixcnt};
    if (radixcnt + 1 < 10) {
        ssize = ssizedefaults;
    } else {
        ssize = (unsigned long long*)calloc(1, sizeof(unsigned long long) * (radixcnt + 1));
    }

    idx = smartRadixDiff(lfs, rfs, &con, maxu, ssize);

    // build the size unit string
    if (idx) {
      len = smartRadixDesc(ssize, &con, idx, smart, buflen, buf);
      if (suffixs && len > 0 && (size_t)len < buflen) {
          smart = snprintf(buf + len, buflen-len, "%s", suffixs[ssize[idx] + 1]);
          if (smart>0 && (size_t)smart<buflen-len) {
              len += smart;
          }
      }
    } else {
        len = snprintf(buf, buflen, "%s", suffixs[ssize[idx] + 1]);
    }    
    __CP("%s\n", buf);
    
    if (ssize != ssizedefaults) {
        free(ssize);
    }
    return len;
}

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
        __xout char* buf) {
    unsigned short idx = 0;
    unsigned long long *ssize;
    unsigned long long ssizedefaults[10] = {0};
    SmartRadixCon con = {radix, radixcnt};
    if (radixcnt + 1 < 10) {
        ssize = ssizedefaults;
    } else {
        ssize = (unsigned long long*)calloc(1, sizeof(unsigned long long) * (radixcnt + 1));
    }

    if (ssize == NULL) {
        return -1;
    }
    // get the radix base format
    idx = smartRadixChange(size, &con, maxu, ssize);
   
    // build the size unit string
    buflen = smartRadixDesc(ssize, &con, idx, smart, buflen, buf);
    __CP("%s\n", buf);

    if (ssize != ssizedefaults) {
        free(ssize);
    }
    return buflen;
}

const SmartRadix _allsizes[] = {
    {"Bytes", 1024}, 
    {"Kb", 1024}, 
    {"Mb", 1024}, 
    {"Gb", 1024}, 
    {"Tb", 1024}, 
    {"Pb", 1024}, 
    {"Eb", 1024}, 
    {"Zb", 1024}, 
    {"Yb", 1024}};
const SmartRadix _allsizes_en[] = {
    {"Bytes", 1024}, 
    {"Kb", 1024}, 
    {"Mb", 1024}, 
    {"Gb", 1024}, 
    {"Tb", 1024}, 
    {"Pb", 1024}, 
    {"Eb", 1024}, 
    {"Zb", 1024}, 
    {"Yb", 1024}};
int smartBytes(
        __xin unsigned long long size, 
        __xin int maxunit, 
        __xin int smart, 
        __xin size_t buflen, 
        __xout char *buf) {
    return smartSizeRadixDesc(size, 
            __countof(_allsizes), 
            _allsizes, 
            maxunit, 
            smart, buflen, buf);
}

const SmartRadix _alltimes[] = {
    //{"nano", 1000},
    //{"micro", 1000},
    {"毫秒", 1000},
    {"秒", 60}, 
    {"分", 60}, 
    {"小时", 24}, 
    {"天", 30}, 
    {"月", 12},
    {"年", 365}};
const SmartRadix _alltimes_en[] = {
    //{"nano", 1000},
    //{"micro", 1000},
    {"millis", 1000},
    {"secs", 60}, 
    {"minutes", 60}, 
    {"hour", 24}, 
    {"day", 30},
    {"month", 12}, 
    {"year", 365}};
int smartTime(
        __xin unsigned long long size, 
        __xin int maxunit, 
        __xin int smart, 
        __xin size_t buflen, 
        __xout char* buf) {
    return smartSizeRadixDesc(size, 
            __countof(_alltimes), 
            _alltimes, 
            maxunit, 
            smart, buflen, buf);
}

const char* _alltimessuffixs[] = {"前", "现在", "后"};
const char* _alltimessuffixs_en[] = {" before", " now", " after"};
int smartTimeDiff(
        __xin unsigned long long lfs, 
        __xin unsigned long long rfs, 
        __xin int maxunit, 
        __xin int smart, 
        __xin size_t buflen, 
        __xout char* buf) {

    return smartRadixDiffDesc(lfs, rfs, 
            __countof(_alltimes), 
            _alltimes, 
            maxunit, 
            smart, 
            _alltimessuffixs,
            buflen, 
            buf);

}

static void test_size(unsigned long long size, int maxu, int maxv) {
    char buf[256] = {0};
    smartBytes(576, maxu, maxv, 256, buf);
    smartBytes(1024 + 576, maxu, maxv, 256, buf);
    smartBytes(1024*1024*2 + 1024*5 + 576, maxu, maxv, 256, buf);
    smartBytes(size, maxu, maxv, 256, buf);
}

static void test_time(unsigned long long size, int maxu, int maxv) {
    char buf[256] = {0};
    smartTime(size, maxu, maxv, 256, buf);

    smartTimeDiff(0, 1000, maxu, 1, 256, buf);

    smartTimeDiff(1000, 1000, maxu, 1, 256, buf);

    smartTimeDiff(1000, 0, maxu, 2, 256, buf);
}

static int xx_main(int argc, const char* argv[]) {
    (void)argc;
    (void)argv;
    if(argc<3) {
        return 1;
    }
    int maxu = atoi(argv[1]);
    int maxv = atoi(argv[2]);
    unsigned long long size = atoll(argv[3]);

    test_size(size, maxu, maxv);

    test_time(size, maxu, maxv);

    return 0;
}
