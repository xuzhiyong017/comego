package com.duowan.jni;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by jerryzhou on 15/10/27.
 */
public class JRingBuffer implements Closeable {
    ///
    protected long rb;

    ///
    public static enum JRBFlag{
        JRB_BlockRead(1),
        JRB_BlockWrite(1<<1),
        JRB_Override(1<<2),
        JRB_ReadChannelShut(1<<3),
        JRB_WriteChannelShut(1<<4),
        JRB_ReadSleep(1<<5),
        JRB_WriteSleep(1<<6),

        JRB_BlockNone(0);

        private final int flag;

        JRBFlag(int f) {
            this.flag = f;
        }

        public int getFlag() {
            return this.flag;
        }
    };

    /**
     * */
    public JRingBuffer() {
        this.rb = irb_alloc(4096, JRBFlag.JRB_BlockNone.getFlag());
    }

    /**
     * */
    public JRingBuffer(long capacity, int flag) {
        this.rb = irb_alloc(capacity, flag);
    }

    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    //////////////////////////////////////////
    // Closeable

    /**
     * */
    @Override
    public void close() throws IOException {
        if (this.rb != 0) {
            irb_free(this.rb);
            this.rb = 0;
        }
    }

    /**
     * */
    public void format(String format, Object ...args) {
        String s = String.format(format, args);
        this.write(s.getBytes());
    }

    /**
     * */
    public synchronized void write(byte[] bytes) {
        if (this.rb != 0) {
            irb_write(this.rb, bytes);
        }
    }

    /**
     * */
    public synchronized int  read(byte[] bytes) {
        if (this.rb != 0 && irb_ready(this.rb) > 0) {
            return irb_read(this.rb, bytes);
        }
        return 0;
    }

    /**
     * */
    public byte[] readAll() {
        int size = irb_ready(this.rb);
        if (size > 0) {
            byte[] b = new byte[size];
            size = this.read(b);
            if (size != b.length) {
                byte[] nb = new byte[size];
                System.arraycopy(b, 0, nb, 0, size);
                b = nb;
            }
            return b;
        }
        return null;
    }

    /**
     * */
    public String readAllAsString() {
        byte[] bytes = readAll();
        String msg = new String(bytes);
        return msg;
    }

    /**
     * */
    public int ready() {
        return irb_ready(this.rb);
    }

    /**
     * */
    public long handle() {
        return this.rb;
    }

    ///
    public static native long irb_alloc(long capacity, int flag);

    ///
    public static native void irb_free(long rb);

    ///
    public static native void irb_close(long rb);

    ///
    public static native void irb_shutdown(long rb, int flag);

    ///
    public static native int irb_write(long rb, byte[] bytes);

    ///
    public static native int irb_read(long rb, byte[] bytes);

    ///
    public static native int irb_ready(long rb);

    ///
    public static native long irb_getmicros();

    ///
    public static native long irb_getmillis();

    /// load library
    static {
        System.loadLibrary("jringbuffer");
    }
}
