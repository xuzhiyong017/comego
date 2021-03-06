#include "ringbuffer.h"
#include <memory.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>


#define _i_u(v) (void)v
#define _i_u_min(u, v) ((u) < (v) ? (u) : (v))

/**
 * */
typedef unsigned long long ilen_t;

/**
 * */
typedef struct _iringbuffer {
    volatile size_t write;
    volatile size_t read;

    volatile ilen_t writelen; // may be int64
    volatile ilen_t readlen;  // may be int64

    size_t flag;
    size_t capacity;

    char buf[];
}_iringbuffer;

/**
 * */
#define _irb(rb) ((_iringbuffer*)(rb - sizeof(_iringbuffer)))
#define _irbget(buffer) _iringbuffer *rb = _irb(buffer)

/**
 * */
iringbuffer irb_alloc(size_t capacity, int flag) {
    _iringbuffer *buffer = (_iringbuffer*)calloc(sizeof(struct _iringbuffer) + capacity + 1, 1);
    buffer->capacity = capacity;
    buffer->buf[capacity] = 0;
    buffer->flag = (size_t)flag;

    buffer->write = 0;
    buffer->writelen = 0;

    buffer->read = 0;
    buffer->readlen = 0;

    return (char*)buffer->buf;
}

/**
 * */
void irb_free(iringbuffer buffer) {
    _irbget(buffer);

    free(rb);
}

/**
 */
void irb_close(iringbuffer buffer) {
    _irbget(buffer);
    
    rb->flag |= irbflag_readchannelshut;
    rb->flag |= irbflag_writechannelshut;
}

/**
 */
void irb_shutdown(iringbuffer buffer, int flag) {
    _irbget(buffer);
    rb->flag |= flag;
}

/**
 * */
size_t irb_writestr(iringbuffer buffer, const char* str) {
    return irb_write(buffer, str, strlen(str));
}

/**
 * */
size_t irb_write(iringbuffer buffer, const char* value, size_t length) {
    size_t empty;
    size_t write;
    size_t finish = 0;
    size_t content;
    _irbget(buffer);
    
    // write to shut down rb
    if (rb->flag & irbflag_writechannelshut) {
        return -1;
    }

    if (finish < length) do {
        // should break when got shut down
        if (rb->flag & irbflag_writechannelshut) {
            break;
        }
        
        // current content size
        content = (size_t)(rb->writelen - rb->readlen);

        // overide buffer
        if (rb->flag & irbflag_override) {
            empty = length - finish;
        } else {
            empty = _i_u_min(rb->capacity - content, length - finish);
        }
        
        // no space continue
        if (empty == 0) {
            // the write channel have been shutdown
            if (rb->flag & irbflag_writechannelshut) {
                break;
            }
            // not block mode, will return right now
            if (!(rb->flag & irbflag_blockwrite)) {
                break;
            }
            // need sleep
            if (rb->flag & irbflag_writesleep) {
                sleep(0);
            }
            // can be dynamic resize the space
            continue;
        }

        // write data
        if (empty > 0) do {
            write = rb->capacity - rb->write;
            write = _i_u_min(write, empty);

            memcpy(rb->buf + rb->write, value + finish, write);
            rb->write += write;
            rb->writelen += write;
            if (rb->write >= rb->capacity) {
                rb->write = 0;
            }

            finish += write;
            empty -= write;
        } while(empty > 0);

    } while(finish < length && (rb->flag & irbflag_blockwrite));

    return finish;
}

/**
 * */
size_t irb_read(iringbuffer buffer, char* dst, size_t length) {
    size_t full;
    size_t read;
    size_t finish = 0;
    _irbget(buffer);
    
    // write to shut down rb
    if (rb->flag & irbflag_readchannelshut) {
        return -1;
    }

    if (finish < length) do {
        // write to shut down rb
        if (rb->flag & irbflag_readchannelshut) {
            break;
        }

        // write override it
        if (rb->flag & irbflag_override) {
            full = length -finish;
        } else {
            full = _i_u_min((size_t)(rb->writelen - rb->readlen), length - finish);
        }
        
        // no content continue
        if (full == 0) {
            // the write channel have been shutdown
            if (rb->flag & irbflag_writechannelshut) {
                break;
            }
            // the read operating is not block, will return right now
            if (!(rb->flag & irbflag_blockread)) {
                break;
            }
            // need sleep
            if (rb->flag & irbflag_readsleep) {
                sleep(0);
            }
            // can be dynamic resize the space
            continue;
        }

        // read data
        if (full > 0) do {
            read = rb->capacity - rb->read;
            read = _i_u_min(read, full);

            memcpy(dst + finish, rb->buf + rb->read, read);
            rb->read += read;
            rb->readlen += read;
            if (rb->read >= rb->capacity) {
                rb->read = 0;
            }

            finish += read;
            full -= read;
        } while(full > 0);

    } while(finish < length && (rb->flag & irbflag_blockread));

    return finish;
}

/**
 * */
size_t irb_ready(iringbuffer buffer) {
    _irbget(buffer);
    return rb->writelen - rb->readlen;
}

/**
 * */
const char* irb_buf(iringbuffer buffer) {
    _irbget(buffer);
    return rb->buf;
}

/* Helper for irg_print() doing the actual number -> string
 * conversion. 's' must point to a string with room for at least
 * _IRB_LLSTR_SIZE bytes.
 *
 * The function returns the length of the null-terminated string
 * representation stored at 's'. */
#define _IRB_LLSTR_SIZE 21

size_t _irb_ll2str(char *s, long long value) {
    char *p, aux;
    unsigned long long v;
    size_t l;

    /* Generate the string representation, this method produces
     * an reversed string. */
    v = (value < 0) ? -value : value;
    p = s;
    do {
        *p++ = '0'+(v%10);
        v /= 10;
    } while(v);
    if (value < 0) *p++ = '-';

    /* Compute length and add null term. */
    l = p-s;
    *p = '\0';

    /* Reverse the string. */
    p--;
    while(s < p) {
        aux = *s;
        *s = *p;
        *p = aux;
        s++;
        p--;
    }
    return l;
}

/* Identical _irb_ll2str(), but for unsigned long long type. */
size_t _irb_ull2str(char *s, unsigned long long v) {
    char *p, aux;
    size_t l;

    /* Generate the string representation, this method produces
     * an reversed string. */
    p = s;
    do {
        *p++ = '0'+(v%10);
        v /= 10;
    } while(v);

    /* Compute length and add null term. */
    l = p-s;
    *p = '\0';

    /* Reverse the string. */
    p--;
    while(s < p) {
        aux = *s;
        *s = *p;
        *p = aux;
        s++;
        p--;
    }
    return l;
}

/* This function is similar to sdscatprintf, but much faster as it does
 * not rely on sprintf() family functions implemented by the libc that
 * are often very slow. Moreover directly handling the sds string as
 * new data is concatenated provides a performance improvement.
 *
 * However this function only handles an incompatible subset of printf-alike
 * format specifiers:
 *
 * %s - C String
 * %i - signed int
 * %I - 64 bit signed integer (long long, int64_t)
 * %u - unsigned int
 * %U - 64 bit unsigned integer (unsigned long long, uint64_t)
 * %% - Verbatim "%" character.
 */
size_t irb_fmt(iringbuffer rb, const char * fmt, ...) {
    const char *f = fmt;
    size_t i;
    va_list ap;

    char next, *str;
    size_t l;
    long long num;
    unsigned long long unum;

    char buf[_IRB_LLSTR_SIZE];

    va_start(ap,fmt);
    f = fmt;    /* Next format specifier byte to process. */
    i = 0;
    while(*f) {
        
        /* Make sure there is always space for at least 1 char. */
        switch(*f) {
        case '%':
            next = *(f+1);
            f++;
            switch(next) {
            case 's':
            case 'S':
                str = va_arg(ap,char*);
                l = strlen(str);//(next == 's') ?  : sdslen(str);
                irb_write(rb, str, l);
                i += l;
                break;
            case 'i':
            case 'I':
                if (next == 'i')
                    num = va_arg(ap,int);
                else
                    num = va_arg(ap,long long);
                {
                    l = _irb_ll2str(buf,num);
                    irb_write(rb, buf, l);
                    i += l;
                }
                break;
            case 'u':
            case 'U':
                if (next == 'u')
                    unum = va_arg(ap,unsigned int);
                else
                    unum = va_arg(ap,unsigned long long);
                {
                    l = _irb_ull2str(buf,unum);
                    irb_write(rb, buf, l);
                    i += l;
                }
                break;
            default: /* Handle %% and generally %<unknown>. */
                irb_write(rb, f, 1);
                break;
            }
            break;
        default:
            irb_write(rb, f, 1);
            break;
        }
        f++;
    }
    va_end(ap);

    return i;
}

// printf 
size_t irb_vprintf(iringbuffer rb, const char* fmt, va_list ap) {
    va_list cpy;
    char staticbuf[1024], *buf = staticbuf;
    size_t buflen = strlen(fmt)*2;
    size_t writelen = 0;

    /* We try to start using a static buffer for speed.
     * If not possible we revert to heap allocation. */
    if (buflen > sizeof(staticbuf)) {
        buf = (char*)malloc(buflen);
        if (buf == NULL) return 0;
    } else {
        buflen = sizeof(staticbuf);
    }

    /* Try with buffers two times bigger every time we fail to
     * fit the string in the current buffer size. */
    while(1) {
        buf[buflen-2] = '\0';
        va_copy(cpy,ap);
        writelen = vsnprintf(buf, buflen, fmt, cpy);
        va_end(cpy);
        if (buf[buflen-2] != '\0') {
            if (buf != staticbuf) free(buf);
            buflen *= 2;
            buf = (char*)malloc(buflen);
            if (buf == NULL) return 0;
            continue;
        }
        break;
    }

    /* Finally concat the obtained string to the SDS string and return it. */
    irb_write(rb, buf, writelen);
    if (buf != staticbuf) free(buf);
    return writelen;
}

size_t irb_printf(iringbuffer rb, const char *fmt, ...) {
    va_list ap;
    size_t len;
    va_start(ap, fmt);
    len = irb_vprintf(rb, fmt, ap);
    va_end(ap);
    return len;
}

/**
* http://linux.die.net/man/2/gettimeofday

* gives the number of seconds and microseconds since the Epoch
* 
* struct timeval {
*    time_t      tv_sec;     //  seconds 
*    suseconds_t tv_usec;    //  microseconds 
* };
* 
* seconds
* millisecond
* microsecond
* nanosecond
* picosecond
* femtosecond
*/

// get current system time in microsecond 
int64_t irb_getmicros() {
     struct timeval tv;
         
     gettimeofday(&tv, NULL);
     return (int64_t)tv.tv_sec * 1000 * 1000 + tv.tv_usec;
}

// get current system time in millisecond
int64_t irb_getmillis() {
    return irb_getmicros() / 1000;
}

