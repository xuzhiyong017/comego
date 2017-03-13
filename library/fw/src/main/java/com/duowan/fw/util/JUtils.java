package com.duowan.fw.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ListView.FixedViewInfo;
import android.widget.TextView;

import com.duowan.fw.BuildConfig;
import com.duowan.fw.Module;
import com.duowan.fw.root.BaseContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public class JUtils {

    private final static String channelTip = "ct";
    private final static String flowerTip1 = "ft1";
    private final static String flowerTip2 = "ft2";

    private final static String digit62Table = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static boolean isDebugConnected(){
    	return Debug.isDebuggerConnected();
    }
    public static boolean checkFlowerTip(boolean singleFlower) {
        File file = new File(BaseContext.gContext.getFilesDir().getAbsolutePath()
                             + File.separatorChar + (singleFlower?flowerTip1:flowerTip2));
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(Exception e){}
            return true;
        }
        return false;
    }
	
    public static boolean checkChannelTip(Activity act) {
        File verify = new File(act.getFilesDir().getAbsolutePath()
                               + File.separatorChar + channelTip);
        if (verify.exists()) {
            return false;
        }
        try {
            String nettype = getNetworkType();		    
            boolean isGPRS = nettype.equals(NetworkType.Mobile2G);
            if (isGPRS) {
                verify.createNewFile();
                return true;
            }			
        } catch (Exception e) {
        }
        return false;
    }	

    public static String stringMd5(String s) {
        try { 
        	// FIXME: a big exception ...
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte b[] = md.digest();

            StringBuffer buf = new StringBuffer("");
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String str = buf.toString();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getUrlParams(String url) {
        if (url != null) {
            int pramsStartPos = url.indexOf("?uid=");
            if (pramsStartPos != -1) {
                return url.substring(pramsStartPos);
            }
        }
        return null;
    }
	
    public static String replacePortraitUrlHost(String url, String newHost) {
        String params = getUrlParams(url);
        if (params != null) {
            return newHost + params;
        }
        return url;
    }

    public static boolean getFlags(int flags, int mask) {
        return 0 != (flags & mask);
    }

    public static int setFlags(int flags, boolean b, int mask) {
        if (b) {
            flags |= mask;
        } else {
            flags &= ~mask;
        }
        return flags;
    }
	
    public static boolean hasSimCard(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
            return false;
        }
        return true;
    }
   
    public static String getSubscriberId() {
        TelephonyManager tm = (TelephonyManager) BaseContext.gContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getSubscriberId();
        } catch (SecurityException e) {
            JLog.error("Utils.getSubscriberId", "Permission denied:" + e.getMessage());
        }
        return "";
    }

    public static String getPhoneNumber(){
        TelephonyManager tMgr = (TelephonyManager)BaseContext.gContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tMgr.getLine1Number();
        } catch (SecurityException e) {
            JLog.error("Utils.getPhoneNumber", "Permission denied:" + e.getMessage());
        }
        return "";
    }

    public static String getSimOperator() {
        TelephonyManager tm = (TelephonyManager) BaseContext.gContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }
	
    public static String sha1(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            java.security.MessageDigest sha1 = java.security.MessageDigest
                .getInstance("SHA1");
            byte[] digest = sha1.digest(str.getBytes());
            sb.append(bytesToHexString(digest));
        } catch (NoSuchAlgorithmException e) {
            JLog.error(JUtils.class, e);
        }
        return sb.toString();
    }

    public static String md5(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest
                .getInstance("MD5");
            byte[] digest = md5.digest(str.getBytes());
            sb.append(bytesToHexString(digest));
        } catch (NoSuchAlgorithmException e) {
            JLog.error(JUtils.class, e);
        }
        return sb.toString();
    }

    public static String fileMd5(String filePath) {
        File file = new File(filePath);
        return fileMd5(file);
    }

    public static String fileMd5(File file) {
        if (file == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int readCount = 0;
            java.security.MessageDigest md5 = java.security.MessageDigest
                .getInstance("MD5");
            while ((readCount = in.read(buffer)) != -1) {
                md5.update(buffer, 0, readCount);
            }
            sb.append(bytesToHexString(md5.digest()));
        } catch (FileNotFoundException e) {
            JLog.error(JUtils.class, e);
        } catch (NoSuchAlgorithmException e) {
            JLog.error(JUtils.class, e);
        } catch (IOException e) {
            JLog.error(JUtils.class, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            int val = b & 0xff;
            if (val < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hex) {
        final byte[] encodingTable = { (byte) '0', (byte) '1', (byte) '2',
                                       (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
                                       (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c',
                                       (byte) 'd', (byte) 'e', (byte) 'f' };
        final byte[] decodingTable = new byte[128];
        for (int i = 0; i < encodingTable.length; i++) {
            decodingTable[encodingTable[i]] = (byte) i;
        }
        decodingTable['A'] = decodingTable['a'];
        decodingTable['B'] = decodingTable['b'];
        decodingTable['C'] = decodingTable['c'];
        decodingTable['D'] = decodingTable['d'];
        decodingTable['E'] = decodingTable['e'];
        decodingTable['F'] = decodingTable['f'];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte b1, b2;
        int end = hex.length();
        while (end > 0) {
            if (!isSpace(hex.charAt(end - 1))) {
                break;
            }
            end--;
        }
        int i = 0;
        while (i < end) {
            while (i < end && isSpace(hex.charAt(i))) {
                i++;
            }
            b1 = decodingTable[hex.charAt(i++)];
            while (i < end && isSpace(hex.charAt(i))) {
                i++;
            }
            b2 = decodingTable[hex.charAt(i++)];
            out.write((b1 << 4) | b2);
        }
        return out.toByteArray();
    }

    public static boolean isSpace(char c) {
        return (c == '\n' || c == '\r' || c == '\t' || c == ' ');
    }

    public static byte[] getIPArray(int ip) {
        byte[] ipAddr = new byte[4];
        ipAddr[0] = (byte) ip;
        ipAddr[1] = (byte) (ip >>> 8);
        ipAddr[2] = (byte) (ip >>> 16);
        ipAddr[3] = (byte) (ip >>> 24);
        return ipAddr;
    }

    public static String getIpString(byte[] ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip[0] & 0xff);
        sb.append(".");
        sb.append(ip[1] & 0xff);
        sb.append(".");
        sb.append(ip[2] & 0xff);
        sb.append(".");
        sb.append(ip[3] & 0xff);
        return sb.toString();
    }

    public static String getIpString(int ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip & 0xff);
        sb.append(".");
        sb.append(ip >>> 8 & 0xff);
        sb.append(".");
        sb.append(ip >>> 16 & 0xff);
        sb.append(".");
        sb.append(ip >>> 24 & 0xff);
        return sb.toString();
    }

    public static int getPort(List<Integer> ports) {
        java.util.Random random = new java.util.Random(
                                                       System.currentTimeMillis());
        return ports.get(random.nextInt(ports.size()));
    }

    public static int getLittleEndianInt(byte[] buffer, int start) {
        int i = buffer[start + 0] & 0xff;
        i |= (buffer[start + 1] << 8) & 0xff00;
        i |= (buffer[start + 2] << 16) & 0xff0000;
        i |= (buffer[start + 3] << 24) & 0xff000000;
        return i;
    }

    public static byte[] toBytes(ByteBuffer buffer) {
        if (buffer == null) {
            return new byte[0];
        }
        int savedPos = buffer.position();
        int savedLimit = buffer.limit();
        try {
            byte[] array = new byte[buffer.limit() - buffer.position()];
            if (buffer.hasArray()) {
                int offset = buffer.arrayOffset() + savedPos;
                byte[] bufferArray = buffer.array();
                System.arraycopy(bufferArray, offset, array, 0, array.length);
                return array;
            } else {
                buffer.get(array);
                return array;
            }
        } finally {
            buffer.position(savedPos);
            buffer.limit(savedLimit);
        }
    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    public static int generateMsgId() {
        long l = System.currentTimeMillis() / 1000; // drop milliseconds
        int i = (int) (l & Integer.MAX_VALUE);
        return i;
    }

    public static long uint2long(int i) {
        long l = 0xffffffffL & i;
        return l;
    }

    public static long getJavaTimeFromUint32(int t) {
        return JTimeUtils.getJavaTimeFromUint32(t);
    }

    public static long getCurrentTime() {
        return JTimeUtils.getCurrentTime();
    }

    public static int javaTimeToUnit32(long time) {
        return JTimeUtils.javaTimeToUnit32(time);
    }

    public static String calcuateAge(String birth) {
        return JTimeUtils.calcuateAge(birth);
    }
	
    public static int getAgeByBirthday(String birth) {
        return JTimeUtils.getAgeByBirthday(birth);
    }

    public static String getDisplayTimeFromTimestamp(long timestamp) {
        return JTimeUtils.getDisplayTimeFromTimestamp(timestamp);
    }
	
    public static String getImei(Context context) {
    	// device id
        try {
            String imei =  ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
            if (imei != null && imei.length() > 0) return imei;
        } catch (SecurityException e) {
            JLog.error("Utils.getImei", "getImei exception:" + e.getMessage());
        }
        return "";
    }
    /**
     * Returns MAC address of the given interface name.
     * 
     * credits to http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device/13007325#13007325
     * 
     * Note: requires  <uses-permission android:name="android.permission.INTERNET " /> and 
     *                 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE " /> in AndroidManifest.xml
     * 
     * @param interfaceName eth0, wlan0 or NULL=use first interface 
     * @return  mac address or empty string
     */
    @SuppressLint("NewApi")
	public static String getMACAddress(String interfaceName) {
        String macaddress = "";
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));       
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                macaddress = buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return macaddress ;
    }
    
    public static final String macAddress = scanMacAddress(); 
    
    private static String scanMacAddress(){
    	String macWifi = getMACAddress("wlan0");
    	if (macWifi == null || macWifi.length() == 0) {
			macWifi = getMACAddress("eth0");
		}
    	if (macWifi == null) {
    		return "";
		}
    	return macWifi;
    }

//    public static void takePhotoFromCamera(UIActivity act, String filePath) {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        File out = new File(filePath);
//        out.delete();
//        Uri uri = Uri.fromFile(out);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        try {
//            act.startActivityForResult(intent, Constant.GET_PHOTO_FROM_CAMERA);
//        } catch (Exception e) {
//            YLog.error("Utils.takePotoFromCamera", e);
//        }
//    }

    public static void selectPhoto(Activity act) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            act.startActivityForResult(intent, JConstant.GET_PHOTO_FROM_GALLERY);
        } catch (Exception e) {
            JLog.error("Utils.selectPhoto", e);
        }
    }

    public static String getPhotoNameFromUrl(int uid, String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
    
    public static boolean isSameWeek(long t1, long t2) {
        return JTimeUtils.isSameWeek(t1, t2);
    }
	
    public static boolean isSameDay(long t1, long t2) {
        return JTimeUtils.isSameDay(t1, t2);
    }

    public static int getDayOfWeek(long t) {
        return JTimeUtils.getDayOfWeek(t);
    }

    public static boolean isHttpUrl(String url) {
        final String urlKeyword = "http://";
        return (url.indexOf(urlKeyword) != -1);
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.length() != 11 || !phone.startsWith("1"))
            return false;

        if(!isNumber(phone))
            return false;
		
        return true;
    }
	
    public static boolean isPCPhotoUrl(String url) {
        final String pcKeyword = ".com/user_logo/";
        int index = url.indexOf(pcKeyword);
        return (index != -1);
    }

    public static boolean isDebugMode(Context context) {
        boolean debuggable = false;
        ApplicationInfo appInfo = null;
        PackageManager packMgmr = context.getPackageManager();
        try {
            appInfo = packMgmr.getApplicationInfo(context.getPackageName(),
                                                  PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            debuggable = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
        }
        return debuggable;
    }

    public static boolean externalStorageExist() {
        return JFileUtils.externalStorageExist();
    }

    public static void openLocationSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public static String getPostTimeString(long postTime, boolean showToday) {
        return JTimeUtils.getPostTimeString(postTime, showToday);
    }

    private static Map<String, String> FILE_MIMES = new HashMap<String, String>();
    static {
        FILE_MIMES.put(".bmp", "image/bmp");
        FILE_MIMES.put(".gif", "image/gif");
        FILE_MIMES.put(".jpe", "image/jpeg");
        FILE_MIMES.put(".jpeg", "image/jpeg");
        FILE_MIMES.put(".jpg", "image/jpeg");
        FILE_MIMES.put(".png", "image/png");
        FILE_MIMES.put(".speex", "audio/speex");
        FILE_MIMES.put(".spx", "audio/speex");
        //FILE_MIMES.put(Constant.VOICE_EXT, "audio/speex");
    }

    public static String getFileMime(String fileName) {
        Set<String> keys = FILE_MIMES.keySet();
        for (String key : keys) {
            if (fileName.toLowerCase().endsWith(key)) {
                return FILE_MIMES.get(key);
            }
        }
        return "*/*";
    }

    public static String getFileExt(String fileName) {
        final int pos = fileName.lastIndexOf(".");
        return pos == -1 ? "" : fileName.toLowerCase().substring(pos);
    }

    public static long merge(int high32, int low32) {
        long l = 0xffffffffL & low32;
        l |= (0xffffffffL & high32) << 32;
        return l;
    }

    public static long getBroadcastMsgId(int i) {
        long l = System.currentTimeMillis();
        l |= (0xffffffffL & i) << 32;
        return l;
    }

    public static class StrLocation {
        public String longitude;
        public String latitude;
    }

    public static int getLocalIpInt() {
        String ip = getLocalIpAddress();
        return str2Ip(ip);
    }
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                     .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                         .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            JLog.error("Utils.getLocalIpAddress", e);
        }
        return null;
    }
	
    public static StrLocation getLocationString(Location location) {
        StrLocation strLocation = new StrLocation();
        strLocation.longitude = cutDecimalTail(Double.toString(location
                                                               .getLongitude()));
        strLocation.latitude = cutDecimalTail(Double.toString(location
                                                              .getLatitude()));
        return strLocation;
    }

    private static String cutDecimalTail(String decimal) {
        final int MAX_DECIMAL_LENGTH = 7;
        final char point = '.';
        int end = decimal.indexOf(point) + MAX_DECIMAL_LENGTH;
        if (end < decimal.length()) {
            return decimal.substring(0, end);
        } else {
            return decimal;
        }
    }

    public static void setRoundCornerPhotoTo(ImageView image, Bitmap bmp,
                                             float roundPx) {
        Bitmap roundCornerBmp = JImageUtils.getRoundedCornerBitmap(bmp,
                                                                    roundPx);
        image.setImageBitmap(roundCornerBmp);
    }

    public static boolean isEventInBound(MotionEvent event, View view) {
        float x = event.getRawX();
        float y = event.getRawY();

        int[] pos = new int[] { 0, 0 };
        view.getLocationOnScreen(pos);
        int width = view.getWidth();
        int height = view.getHeight();

        boolean xIn = (x >= pos[0] && x <= pos[0] + width);
        boolean yIn = (y >= pos[1] && y <= pos[1] + height);
        return xIn && yIn;
    }

    private static int byte2int(byte b) {
        int l = b & 0x07f;
        if (b < 0) {
            l |= 0x80;
        }
        return l;
    }

    public static int str2Ip(String ip) {
        try {
            InetAddress ipAddr = InetAddress.getByName(ip);
            byte[] bytes = ipAddr.getAddress();

            int a = byte2int(bytes[0]);
            int b = byte2int(bytes[1]);
            int c = byte2int(bytes[2]);
            int d = byte2int(bytes[3]);

            int result = (d << 24) | (c << 16) | (b << 8) | a;
            return result;
        } catch (UnknownHostException e) {
            JLog.error("Utils.str2Ip", e);
        }
        return 0;
    }

    public static List<android.util.Pair<String, Integer>> parseBusiCardMuiltiParts(
                                                                                    String text) {
        List<android.util.Pair<String, Integer>> ret = new ArrayList<android.util.Pair<String, Integer>>();
        String[] strArr = text.split(";");
        if (strArr.length > 0) {
            for (String str : strArr) {
                if (str.length() == 0) {
                    continue;
                }
                String[] strArr2 = str.split(":");
                if (strArr2.length >= 1) {
                    if (strArr2.length == 1) {
                        ret.add(android.util.Pair.create(strArr2[0], 0));
                    } else {
                        if (strArr2[1].matches("^\\d+$")) {
                            ret.add(android.util.Pair.create(strArr2[0],
                                                             Integer.valueOf(strArr2[1])));
                        } else {
                            ret.add(android.util.Pair.create(strArr2[0], 0));
                        }
                    }
                }
            }
        }
        return ret;
    }

    public static String buildBusiCardMuiltiParts(
                                                  List<android.util.Pair<String, Integer>> list) {
        StringBuilder sb = new StringBuilder();
        for (android.util.Pair<String, Integer> t : list) {
            if (sb.length() > 0) {
                sb.append(';');
            }
            sb.append(t.first).append(":").append(t.second);
        }
        return sb.toString();
    }

    public static boolean isValidEmail(String email) {
        Pattern p = Pattern
            .compile("^[_a-zA-Z0-9]+(\\.[_a-zA-Z0-9]+)*@[a-zA-Z0-9_-]+(\\.[a-z0-9A-Z-_]+)+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

//    public static CharSequence getVerifyContents(Context context,
//                                                 Pair<String, Integer> pair) {
//        if (pair == null) {
//            return "";
//        }
//        StringBuilder msg = new StringBuilder(pair.first);
//        if (pair.second == ModelConstant.Verify.NOT_VERIFY) {
//            msg.append("<font color=\"red\"> (")
//                .append(context.getString(R.string.str_not_verify))
//                .append(")</font>");
//        } else {
//            msg.append("<font color=\"green\"> (")
//                .append(context.getString(R.string.str_already_verify))
//                .append(")</font>");
//        }
//        return Html.fromHtml(msg.toString());
//    }

    public static String toUtf8(String str) {
        String ret = "";
        try {
            ret = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ret = str;
        }
        return ret;
    }

    public static InputFilter NumberOnlyEditFilter = new InputFilter() {

        private boolean isNumber(char chr) {
            return (chr >= '0' && chr <= '9');
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            if (source != null) {
                CharSequence subSrc = source.subSequence(start, end);
                int length = subSrc.length();
                String ret = "";
                for (int i = 0; i < length; i++) {
                    char chr = subSrc.charAt(i);
                    if (isNumber(chr)) {
                        ret += chr;
                    }
                }
                return ret;
            }
            return null;
        }

    };

    /*public static String getVersionName(Context context) {
      String version = "";
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = null;
      try {
      pi = pm.getPackageInfo(context.getPackageName(), 0);
      version = pi.versionName;
      } catch (NameNotFoundException e) {
      }
      return version;
      }*/

//    public static View createFixedView(Context c, int textId, OnClickListener listener) {
////        View view = LayoutInflater.from(c).inflate(
////                                                   R.layout.im_broadcast_header, null);
//////        ImageButton info = (ImageButton) view.findViewById(R.id.text_info);
//////        info.setText(textId);
//////        info.setOnClickListener(listener);
////        return view;
//    }

    public static ArrayList<FixedViewInfo> getHeaderOrFooterArray(ListView list, View view) {
        if (view == null) {
            return null;
        }
        FixedViewInfo info = list.new FixedViewInfo();
        info.view = view;
        info.data = "";
        info.isSelectable = false;
        ArrayList<FixedViewInfo> infoArray = new ArrayList<FixedViewInfo>();
        infoArray.add(info);
        return infoArray;
    }

    public static void setClickableLinkText(final Context context,
                                            final TextView textView, String text, int start, int end,
                                            final OnClickListener onClickListener) {
        Spannable spans = SpannableStringBuilder.valueOf(text);
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                onClickListener.onClick(textView);
            }
        };
        spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spans);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
            .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }
	
    public static class NetworkType {
        public static final String Unknown = "";
        public static final String Wifi = ",w";
        public static final String Mobile4G = ",4";
        public static final String Mobile3G = ",3";
        public static final String Mobile2G = ",2";
    }
	
    public static String getNetworkType() {
        String networkType = NetworkType.Unknown;
        ConnectivityManager cm = (ConnectivityManager) BaseContext.gContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            int type = netInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                networkType = NetworkType.Wifi;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                int subType = netInfo.getSubtype();
                if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
                    networkType = NetworkType.Mobile4G;
                } else if (subType == TelephonyManager.NETWORK_TYPE_1xRTT
                    || subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_HSPA
                    || subType == TelephonyManager.NETWORK_TYPE_HSUPA) {
                    networkType = NetworkType.Mobile3G;
                } else {
                    networkType = NetworkType.Mobile2G;
                }
            }
        }
        return networkType;
    }

    public static final int NET_WIFI = 1;
    public static final int NET_2G = 2;
    public static final int NET_3G = 3;
    public static final int NET_LEGACY = 4; // legacy client
    public static final int UNKNOW_NETWORK_TYPE = 5;
	
    public static int getMyNetworkType() {
        int networkType = UNKNOW_NETWORK_TYPE;
        ConnectivityManager cm = (ConnectivityManager) BaseContext.gContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            int type = netInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                networkType = NET_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                int subType = netInfo.getSubtype();
                if (subType == TelephonyManager.NETWORK_TYPE_1xRTT
                    || subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_HSPA
                    || subType == TelephonyManager.NETWORK_TYPE_HSUPA) {
                    networkType = NET_3G;
                } else {
                    networkType = NET_2G;
                }
            }
        }
        return networkType;
    }
		
    public static final int COLOR_MSG_NOT_SEND = 0xFFd78c34;
    public static final int COLOR_MSG_ALREADY_SENT = 0xFF79c480;
    public static final int COLOR_MSG_ARRIVAL = 0xFF6aa670;
    public static final int COLOR_MSG_READ = 0xFF646B6E;
	
//    public static void setMsgState(TextView stateView, int state, boolean isNormalMsg) {
//        switch (state) {
//        case ModelConstant.OutGoingMsgState.STATE_NOT_READY:
//            stateView.setText(R.string.str_im_sending);
//            break;
//        case ModelConstant.OutGoingMsgState.STATE_NOT_SEND:
//            if (!isNormalMsg && NetworkUtil.isNetworkAvailable())
//                stateView.setText(R.string.str_im_uploading);
//            else
//                stateView.setText(R.string.str_im_sending);
//            stateView.setTextColor(COLOR_MSG_NOT_SEND);
//            break;
//        case ModelConstant.OutGoingMsgState.STATE_REACHED_SERVER:
//        case ModelConstant.OutGoingMsgState.STATE_SEND:
//            stateView.setText(R.string.str_im_sent);
//            stateView.setTextColor(COLOR_MSG_ALREADY_SENT);
//            break;
//        case ModelConstant.OutGoingMsgState.STATE_ARRIVAL:
//            stateView.setText(R.string.str_im_arrived);
//            stateView.setTextColor(COLOR_MSG_ARRIVAL);
//            break;
//        case ModelConstant.OutGoingMsgState.STATE_READ:
//            stateView.setText(R.string.str_im_read);
//            stateView.setTextColor(COLOR_MSG_READ);
//            break;
//        default:
//            stateView.setText(null);
//            break;
//        }
//    }
    
//    public static String getFriendPlatformNetworkType(UserInfo user, boolean showPlatform) {
//        String type = "";
//        String blank = " ";
//        if (user == null) {
//            return type;
//        }
//        int intPlatform = 0;
//        int intNetwork = 0;
//        try {
//            intPlatform = Integer.parseInt(user.getPlatform());
//        } catch (Exception e) {
//            YLog.error(Utils.class, "platform format error!");
//        }
//        try {
//            intNetwork = Integer.parseInt(user.getNetwork());
//        } catch (Exception e) {
//            YLog.warn(Utils.class, "network format error or network is null!");
//        }
//        if (showPlatform || intPlatform == ModelConstant.PlatformType.WINDOWS) {
//            switch (intPlatform) {
//            case ModelConstant.PlatformType.WINDOWS:
//                type = getString(R.string.str_platform_pc) + blank;
//                type = showPlatform ? type + getString(R.string.str_online) : type;
//                return type;
//            case ModelConstant.PlatformType.ANDROID:
//                type = getString(R.string.str_platform_android) + blank;
//                break;
//            case ModelConstant.PlatformType.IOS:
//                type = getString(R.string.str_platform_ios) + blank;
//                break;
//            case ModelConstant.PlatformType.SYMBIAN:
//                type = getString(R.string.str_platform_symbian) + blank;
//                break;
//            }
//        }
//        switch (intNetwork) {
//        case NET_WIFI:
//            type += getString(R.string.str_wifi);
//            break;
//        case NET_2G:
//            if(intPlatform == ModelConstant.PlatformType.IOS) {
//                type += getString(R.string.str_ios_2G);
//            } else {
//                type += getString(R.string.str_2G);
//            }
//            break;
//        case NET_3G:
//            type += getString(R.string.str_3G);
//            break;
//        default:
//            break;
//        }
//        if (showPlatform && !StringUtils.isNullOrEmpty(type)) {
//            type += blank + getString(R.string.str_online);
//        }
//        return type;
//    }
	
    public static String getString(int resId) {
        return BaseContext.gContext.getResources().getString(resId);
    }
	
//    public static boolean offlineOrNetErrorRemind(Context context, YService service) {
//        if (!NetworkUtil.isNetworkAvailable()) { //network unavailable
//            Utils.showNetworkConfigDialog(context);
//            return true;
//        }
//        if (service.isOnline() == false) { // offline
//            Toast.makeText(context,
//                           getString(R.string.str_network_connect_fail),
//                           Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//    }
	
    public static boolean isRegPhone(String uname){
        if(uname.startsWith("1")){
            int chr = uname.charAt(uname.length() -1);
            if((chr<48 || chr>57)){
                return true;
            }   

        }else{
            return false;
        }
        return false;
    }
    public static boolean isActOnTop(Activity act) {
        if (act == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String topActName = cn.getShortClassName();
        int idx = topActName.lastIndexOf('.');
        if (idx >= 0) {
            topActName = topActName.substring(idx + 1);
        }
        String actName = act.getClass().getSimpleName();
        if (topActName.equalsIgnoreCase(actName)) {
            return true;
        }
        return false;
    }
    
    public static boolean isNameMatchMobilePattern(String name) {
        return (name != null && name.matches("1\\d{10}(y*|s*)"));
    }

    public static void printThreadStacks(String tag, boolean useYLog, boolean fullLog, boolean release) {
        printStackTraces(Thread.currentThread().getStackTrace(), tag, useYLog, fullLog, release);
    }
    
    public static void printStackTraces(StackTraceElement[] traces, String tag, boolean useYLog, 
        boolean fullLog, boolean release) {
        printLog(tag, "------------------------------------", useYLog, release);
        for (StackTraceElement e : traces) {
            String info = e.toString();
            if (fullLog || info.indexOf(Module.gMainContext.getPackageName()) != -1) {
                printLog(tag, info, useYLog, release);
            }
        }
        printLog(tag, "------------------------------------", useYLog, release);
    }
    
    private static void printLog(String tag, String log, boolean useYLog, boolean release) {
        if (useYLog) {
            if (release) {
                JLog.info(tag, log);
            }
            else {
                JLog.debug(tag, log);
            }
        }
        else {
            if (release) {
                Log.i(tag, log);
            }
            else {
                Log.d(tag, log);
            }
        }
    }
	
//    public static String getVersionString() {
//        PackageManager pm = YYMobile.gContext.getPackageManager();
//        PackageInfo pi = null;
//        try {
//            pi = pm.getPackageInfo(YYMobile.gContext.getPackageName(), 0);
//        }
//        catch (NameNotFoundException e) { // Never happen 
//            return null;
//        }
//        String version = pi.versionName;
//        if (version == null) {
//            int major = YYMobile.gContext.getResources().getInteger(R.integer.major);
//            int minor = YYMobile.gContext.getResources().getInteger(R.integer.minor);
//            int build = YYMobile.gContext.getResources().getInteger(R.integer.build);
//            version = String.valueOf(major) + "." + String.valueOf(minor) + "." + String.valueOf(build);
//        }
//        return version;
//    }
    
    public static class ChinaOperator {
        public static final String CMCC = "CMCC";
        public static final String CTL = "CTL";
        public static final String UNICOM = "UNICOM";
        public static final String UNKNOWN = "Unknown";        
    }
    
    public static String getOperator() {
        String sim = JUtils.getSimOperator();
        if (sim.startsWith("46003") || sim.startsWith("46005")) {
            return ChinaOperator.CTL;
        } else if (sim.startsWith("46001") || sim.startsWith("46006")) {
            return ChinaOperator.UNICOM;
        } else if (sim.startsWith("46000") || sim.startsWith("46002") 
                   || sim.startsWith("46007") || sim.startsWith("46020")){
            return ChinaOperator.CMCC;
        }
        else {
            return ChinaOperator.UNKNOWN;
        }
    }
    
    /*public static String getSpNumber() {
        String number = "";
        YConfig config = YConfig.getInstance(BaseApp.gContext);
        if (getOperator() == ChinaOperator.CTL) {
            number = config.getString(ENTRY.SP_TELCOM, null);
            if (number == null) {
                number = "106590200106721";
            }
        } else if (getOperator() == ChinaOperator.UNICOM) {
            number = config.getString(ENTRY.SP_UNICOM, null);
            if (number == null) {
                number = "10655020033021";
            }
        } else if (getOperator() == ChinaOperator.CMCC) {
            number = config.getString(ENTRY.SP_CMCC, null);
            if (number == null) {
                number = "10657555272121";
            }
        }
        return number;
    }*/
    
    public static String getMobileFromPassport(String passport) {
    	if(passport == null) {
            return "";
    	}
        if (passport.startsWith("1") && passport.length() > 11) {
            String mobile = passport.substring(0, 11);
            if (isValidPhoneNumber(mobile)) {
                return mobile;
            }
        }
        return "";
    }
    
    public static String getMobileFromUserName(String userName) {
    	if(userName == null) {
            return "";
    	}
        if (userName.startsWith("1") && userName.length() >= 11) {
            String mobile = userName.substring(0, 11);
            if (isValidPhoneNumber(mobile)) {
                return mobile;
            }
        }
        return "";
    }
    
//    public static Ver getApkVersion(Context ctx, String apkPath) {
//        PackageManager pm = ctx.getPackageManager();
//        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,
//                                                           PackageManager.GET_ACTIVITIES);
//        if(packageInfo != null) {
//            String s = packageInfo.versionName;
//            if(JStringUtils.isNullOrEmpty(s)) {
//                return null;
//            }
//            int index1 = s.indexOf(".");
//            int index2 = s.indexOf(".", index1+1);
//            Ver ver = new Ver();
//            try {
//                int major = Integer.parseInt(s.substring(0,index1));
//                int minor = Integer.parseInt(s.substring(index1+1,index2));
//                int build = Integer.parseInt(s.substring(index2+1,s.length()));
//                YLog.debug("yy", "getApkVersion major:" + major + " minor = " + minor + " build = " + build);
//                ver.mMajor = major;
//                ver.mMinor = minor;
//                ver.mBuild = build;
//            } catch (Exception e) {
//                YLog.warn("yy", "format version error");
//                return null;
//            }
//            return ver;
//        } else {
//            return null;
//        }
//    }
  
//    public static void setLatestMessageForFriendList(UserInfo info, RichTextView tv) {
//        String message = null;
//        MessageInfo msg = info.getLatestMessage();
//        if (msg != null) {
//            message = msg.getText();
//        }
//        tv.setTextForConvList(null);
//        if ((!info.isForum() && StringUtils.isNullOrEmpty(message) && info.getUnreadMessageCount() > 0) 
//            || (message != null && message.indexOf(Constant.VOICE_START_TAG) != -1)) {
//            tv.setTextForConvList(YYMobile.gContext.getString(R.string.str_name_voice_msg));
//        }
//        else if (message != null) {
//            if (message.indexOf(Constant.IMAGE_START_TAG) != -1) {
//                tv.setTextForConvList(YYMobile.gContext.getString(R.string.str_name_image_msg));
//            }
//            else {
//                int endIndex = message.indexOf(Constant.PHONE_END_TAG);
//                if (endIndex != -1) {
//                    tv.setTextForConvList(message.substring(endIndex + Constant.PHONE_END_TAG.length()));
//                }
//                else {
//                    tv.setTextForConvList(message);
//                }
//            }
//        }
//    }

    public static int checkPass(String pass, String name) {
        int len = pass.length();
        if (len == 0)
            return 0;
        if (len > 20) {
            return 2;
        }
        
        // it is prohibited that secret is part of the account.
        if (name != null && name.length() > 0 && pass.indexOf(name) >= 0) {
            return 6;
        }
        boolean allDigit = true;
        boolean allSame = true;
        boolean order = true;
        for (int i = 0; i < len; i++) {
            char c = pass.charAt(i);
            if (c == ' ') { // No spaces.
                return 3;
            }
            if (((int) c) > 256) { // No Chinese characters(including signs).
                return 4;
            }
            
            // It is prohibited that all chars are digits.
            if (c > '9' || c < '0') {
                allDigit = false;
            }
            if (i > 0) {
                char c2 = pass.charAt(i - 1);
                // It is prohibitted that all chars are the same one.
                if (allSame && c != c2) {
                    allSame = false;
                }
                if (order && (c >= 'a' && c <= 'z') && (c - c2 != 1)) {
                    order = false;
                }
            } else {
                if (!(c >= 'a' && c <= 'z')) {
                    order = false;
                }
            }
        } // for
        if (len < 6) {
            return 1;
        }
        if (allDigit) {
            return 5;
        }
        if (allSame) {
            return 7;
        }
        if (order) {
            return 8;
        }
        return 0;
    }
	
    public static boolean isNumber(String str) {
    	return JStringUtils.isAllDigits(str);
    }

    public static interface INewMsgCountListener {
        public void onCountChange(int msgCount, boolean hasMissedCall);
    }

    public static boolean sendSMS(Activity activity, String phoneNumber, String content) {
        try {
            //			Uri smsUri = Uri.parse("tel:" + phoneNumber);
            //			Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
            //			intent.putExtra("address", phoneNumber);
            //			intent.putExtra("sms_body", content);
            //			intent.setType("vnd.android-dir/mms-sms");
            //			activity.startActivity(intent);
			
            Uri uri = Uri.parse("smsto:" + phoneNumber);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body",content);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            //Toast.makeText(activity, R.string.str_device_notsupport_sms, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    
    public static boolean startApp(Context context, String packageName, String activity){
    	try {
		    Intent intent = new Intent();
		    ComponentName componentName = new ComponentName(packageName, activity);
		    intent.setComponent(componentName);
		    if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
		    context.startActivity(intent);
    		return true;
		} catch (Exception e) {
			return false;
		}
    }

	public static boolean installPackage(Context context, String filePath) {
		if (JStringUtils.isNullOrEmpty(filePath)) {
			return false;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}

		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			JLog.error("DownloadHelper", "installPackage path:" + filePath + "; exception:" + e.toString());
			return false;
		}
	}
    
    public static boolean startApp(Context context, String packageName){
    	try {
		    Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);  
		    if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
		    context.startActivity(intent);
    		return true;
		} catch (Exception e) {
			return false;
		}
    }
	
    public static boolean deviceShouldPlaySound(Context context) {
        boolean shouldPlay = true;
        try {
            AudioManager audioMgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            switch (audioMgr.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                shouldPlay = false;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                shouldPlay = false;
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                shouldPlay = true;
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return shouldPlay;
    }
    
    private final static int SHA1_LENGTH = 40; //SHA1 digest consists of 40 hex digits, total 160 bits
    public static String getHashIfPassIsPlainText(String password) {
        //if password is plain text, it's length will be shorter than SHA1_LENGTH
        if (!JStringUtils.isNullOrEmpty(password) && password.length() < SHA1_LENGTH) {
            return JUtils.sha1(password);
        }
        else {
            return password;
        }
    }
    
//    public static ProgressButton addRefreshButtonToList(Context c, ListView list, OnClickListener listener) {
//        LayoutInflater layoutInflater = LayoutInflater.from(c);
//        View container = layoutInflater.inflate(R.layout.progress_button_container, null);
//        if (container != null) {
//            container.setClickable(false);
//            list.addHeaderView(container);
//            ProgressButton progressButton = (ProgressButton) container.findViewById(R.id.progress_button);
//            if (progressButton != null) {
//                progressButton.setOnClickListener(listener);
//            }
//            return (ProgressButton) container.findViewById(R.id.progress_button);
//        }
//        return null;
//    }
    
//    public static void saveBindMobileToAccountDB(String bindMobile) {
//    	AccountDBHelper accountDB =  AccountDBHelper.getInstance(); 
//        List<AccountInfo> allAccount = accountDB.getAccountList();
//        for(AccountInfo accountTmp: allAccount) {
//            if(StringUtils.isNullOrEmpty(accountTmp.getBindMobile()) == false 
//               && accountTmp.getBindMobile().equals(bindMobile)) {
//                accountDB.updateAccountBindMobile(accountTmp.getUsername(), "");
//            }
//            if(accountTmp.getPassport().equals(LoginInfo.getInstance().getPassport())) {
//                accountDB.updateAccountBindMobile(accountTmp.getUsername(), bindMobile);
//            }
//        }
//    }
    
    public static void openNetworkConfig(Context c) {
    	JNetworkUtil.openNetworkConfig(c);
    }
        
    public static Pair<String, String> parseKickedForumMsg(String msg) {
        if (JStringUtils.isNullOrEmpty(msg)) {
            return null;
        }
        String[] infos = msg.split(";");
        String first = null, second = null;
        if (infos.length > 0) {
            first = infos[0];
        }
        if (infos.length > 1) {
            second = infos[1];
        }
        return Pair.create(first, second);
    }
    
//    public static String getOriginFilePath(String msg,boolean isAudio){
//        if(msg == null || msg.length() == 0){
//            return "";
//        }
//        
//        String beginTag,endTag;
//        if (isAudio) {
//            beginTag = Constant.VOICE_START_TAG;
//            endTag = Constant.VOICE_END_TAG;
//        }
//        else {
//            beginTag = Constant.IMAGE_START_TAG;
//            endTag = Constant.IMAGE_END_TAG;
//        }
//        
//        int beginIndex = msg.indexOf(beginTag);
//        int endIndex = msg.indexOf(endTag, beginIndex);    
//        boolean invaildRange = beginIndex > 0 && beginIndex < endIndex && endIndex < msg.length();
//        if(beginIndex == endIndex || invaildRange){
//            return "";
//        }
//        String info = msg.substring(beginIndex + beginTag.length(),
//                                    endIndex);
//        return info;
//    }
    
    public static class TailManager {
        private static Object sLocker = new Object();
        private final int ONE_HOUR = 1000 * 60 * 60;

        class TailInfo {
            int forumId;
            long timeMilis;
            int times;
        }

        @SuppressLint("UseSparseArrays")
		private HashMap<Integer, TailInfo> hashMap = new HashMap<Integer, TailInfo>();

        private static TailManager tailManager;

        public static TailManager getInstance() {
            if (tailManager != null) {
                return tailManager;
            }

            synchronized (sLocker) {
                if (tailManager != null) {
                    return tailManager;
                }
                tailManager = new TailManager();
            }
            return tailManager;
        }

        private TailManager() {

        }

        public boolean needAddTail(int forumId) {
            boolean ret;
            TailInfo info = hashMap.get(forumId);
            if (info == null) {
                info = new TailInfo();
                info.forumId = forumId;
                info.timeMilis = System.currentTimeMillis();
                info.times = 1;
                hashMap.put(forumId, info);
                ret = true;
            }
            else {
                info.times++;
                if (info.times % 10 == 1) {
                    ret = true;
                }
                else {
                    ret = false;
                }
                long t = System.currentTimeMillis();
                if (t - info.timeMilis > ONE_HOUR) {
                    info.times = 0;
                    info.timeMilis = t;
                }
            }
            return ret;
        }
    }
   
    public static String decodeUri(String uri) {
        if (JStringUtils.isNullOrEmpty(uri)) {
            return uri;
        }
        int index = uri.indexOf('%');
        if (index != -1) {
            uri = Uri.decode(uri);
        }
        return uri;
    }
    
    public static boolean availableMemInSDcard() {
        return JFileUtils.availableMemInSDcard();
    }
    
    private static final int MINUTE_TO_SECOND = 60; 
    public static String getShortDuritionText(int seconds) {
        long minute = seconds / 60;
        long second = seconds - minute * MINUTE_TO_SECOND;
        return String.format("%02d:%02d", minute, second);
    }
    
    /**
     * Get human readable file size.
     * @param bytes Num of bytes.
     */
    public static String getHumanReadableFileSize(long bytes) {
    	// less than 1K, show it in Bs, less than 1M, show it in KBs, otherwise show in MBs.
    	if (bytes < 1024) {
            return getFileSizeInBytes(bytes);
    	}
    	return (bytes >> 20) == 0 ?
            getFileSizeInKBytes(bytes) : getFileSizeInMBytes(bytes);
    }
    
    public static String getFileSizeInBytes(long bytes) {
        return String.format("%dB", bytes);
    }
    
    public static String getFileSizeInKBytes(long bytes) {
        long kbs = (bytes >> 10);
        return String.format("%dK", kbs);
    }
    
    public static String getFileSizeInMBytes(long bytes) {
        float kbs = bytes / 1024;
        float mbs = kbs / 1024;
        DecimalFormat df = new DecimalFormat("0.00M");
        String ret = df.format(mbs);
        return ret;
    }
    
    public static String getDuration(int secs) {
        if (secs <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int minutes = secs / 60;
        int seconds = secs % 60;
        if (minutes < 10) {
            sb.append(0);
        }
        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append(0);
        }
        sb.append(seconds);
        return sb.toString();
    }
    
    public static int roundTo(int x, int bound, int delta) {
        return x > bound || Math.abs(x-bound) <= delta ? bound : x;
    }

    public static int toSec(long millis) { return JTimeUtils.toSec(millis); }

    public static long toMillis(int sec) { return JTimeUtils.toMillis(sec); }

    public static class Relay {
        protected final static long DEFAULT_TM = 600; //[ms]
        protected long mLen;
        protected long mPrevClick;

        public Relay() { this(DEFAULT_TM); }

        public Relay(long ms) {
            mLen = ms;
            mPrevClick = System.currentTimeMillis();
        }

        public boolean tooShort() {
            long t = System.currentTimeMillis();
            boolean r = (t - mPrevClick) < mLen;
            mPrevClick = t;
            return r;
        }
    }

    public static class WatchDog {
        private static final long WAIT_TIMEOUT = 2000;
        private long mWaitStart = 0;
        private int mWaitingValue;

        public WatchDog(int x) {
            mWaitingValue = x;
        }

        public boolean isAlive(int cmd) {
            if (cmd != mWaitingValue) {
                mWaitStart = 0;
                return true;
            }
            if (mWaitStart == 0) {
                mWaitStart = System.currentTimeMillis();
                return true;
            }
            if (System.currentTimeMillis() - mWaitStart > WAIT_TIMEOUT) {
                mWaitStart = 0;
                return false;
            }
            return true;
        }
    }
    
    public static long signed62SysToDex(String intIn62) throws Exception {
        if (!isValid62Integer(intIn62)) {
            throw new NumberFormatException();
        }

        boolean isNegative = (intIn62.charAt(0) == '-');
        int size = digit62Table.length();
        long res = 0;
        long power = 1;

        int firstIntPos = isNegative ? 1 : 0;
        for (int i = intIn62.length(); --i >= firstIntPos;) {
            long src1 = res;
            long src2 = digit62Table.indexOf(intIn62.charAt(i)) * power;
            res = res + src2;

            if ((res < src1) && (res < src2)) {
                // over flow
                if (((res != Long.MIN_VALUE) || !isNegative)) {
                    throw new ArithmeticException();
                }
            }
            power = power * size;
        }

        if (isNegative) {
            return -res;
        }
        else {
            return res;
        }
    }

    public static String signedDexTo62Sym(long integer) {
        if (integer == 0) {
            return "0";
        }
        else if (integer == Long.MIN_VALUE) {
            return "-aZl8N0y58M8";
        }

        boolean isNegative = integer < 0;
        int size = digit62Table.length();
        StringBuffer res = new StringBuffer();

        long src = Math.abs(integer);
        while (src > 0) {
            res.append(digit62Table.charAt((int) (src % size)));
            src = src / size;
        }

        res.reverse();

        if (isNegative) {
            res.insert(0, '-');
        }

        return res.toString();
    }

    private static boolean isValid62Integer(String intIn62) {
        if (intIn62 == null || intIn62.length() <= 0) {
            return false;
        }

        if (intIn62.charAt(0) == '-' && intIn62.length() <= 1) {
            return false;
        }

        for (int i = 0; i < intIn62.length(); i++) {
            char c = intIn62.charAt(i);
            if (!(c >= '0' && c <= '9') && !(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
                if (i == 0 && c == '-') {
                    continue;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
    private static final int PADDING = 6;
    public static void setLeftIconToTextView(int iconId, TextView tv, int fontSize) {
        Drawable icon = BaseContext.gContext.getResources().getDrawable(iconId);
        if (icon != null) {
            icon.setBounds(0, 0, fontSize, fontSize);
            tv.setCompoundDrawables(icon, null, null, null);
            tv.setCompoundDrawablePadding(PADDING);
        }
    }
//      
//    public static SparseArray<FP.Tuple<Integer, Integer, Boolean>> parseRankStr(String text) {
//        SparseArray<FP.Tuple<Integer, Integer, Boolean>> sa = new SparseArray<FP.Tuple<Integer, Integer, Boolean>>();
//        if (!JStringUtils.isNullOrEmpty(text)) {
//            String[] strArr = TextUtils.split(text, ",");
//            if (strArr.length > 0) {
//                for (String str : strArr) {
//                    if (str.length() == 0) {
//                        continue;
//                    }
//                    
//                    FP.Tuple<Integer, Integer, Boolean> t = null;
//                    
//                    String[] strArr2 = TextUtils.split(str, ":");
//
//                    if (strArr2.length == 3) {
//                        try {
//                            t = FP.makeTuple(Integer.parseInt(strArr2[0]), Integer.parseInt(strArr2[1]),
//                                    Boolean.parseBoolean(strArr2[1]));
//                        }
//                        catch (NumberFormatException e) {
//                        }
//                        if (t != null) {
//                            sa.put(t.a, t);
//                        }
//                    }
//                }
//            }
//        }
//        return sa;
//    }
//
//    public static String buildRankStr(List<FP.Tuple<Integer, Integer, Boolean>> list) {
//        StringBuilder sb = new StringBuilder();
//        for (FP.Tuple<Integer, Integer, Boolean> t : list) {
//            if (sb.length() > 0) {
//                sb.append(',');
//            }
//            sb.append(t.a).append(":").append(t.b).append(":").append(t.c);
//        }
//        return sb.toString();
//    }
    
    public static Spannable getColorSpannable(String s, int color, int start, int end) {
        Object obj = new ForegroundColorSpan(color);
        Spannable span = new SpannableString(s);
        span.setSpan(obj, start, end, Spannable.SPAN_MARK_POINT);
        return span;
    }
    
//    public static void showLoginPcDialog(final Context context) {
//        CustomDialog dlg = new CustomDialog(context);
//        dlg.setTitle(context.getString(R.string.str_pc_login_yy));
//
//        boolean pred = false; //PwdUtils.emptyPwd();
//
//        String passport = LoginInfo.getInstance().getPassport();
//        String str = context.getString(pred ? R.string.str_login_pc_tip_not_set_pass
//                : R.string.str_login_pc_tip_set_pass, passport);
//        int start = str.indexOf('???') + 2;
//        int end = start + passport.length();
//        dlg.setMessage(getColorSpannable(str, 0xff3089e4, start, end));
//
//        if (pred) {
//            dlg.setButton(context.getString(R.string.str_set_password), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //PwdUtils.inst((UIActivity) context).setPwd();
//                }
//            }, context.getString(R.string.str_cancel), null);
//        }
//        else {
//            dlg.setButton(context.getString(R.string.ids_str_button_ok), null);
//        }
//
//        dlg.show();
//    }
    
    public static boolean isHeadsetPlugged(Context context) {
        AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioMgr.isWiredHeadsetOn();
    }
    
   // will lookup super class
    public static Method getDeclaredMethod(final Object obj, final String name, Class<?>... parameterTypes) {
    	assert obj != null;
    	Method method = null;
    	Class<?> cls = obj.getClass();
    	while (cls != null && method == null) {
    		try {
    			method = cls.getDeclaredMethod(name, parameterTypes);
    		} catch (NoSuchMethodException e) {
    			cls = cls.getSuperclass();
    		}
    	}
    	return method;
    }

    public static void jAssert(boolean cond) {
        if (BuildConfig.DEBUG && !cond) {
            JLog.error("ASSERT!", "================ Assert! ===============\n"
                    + JLog.getCurrentStackTrace());
        }
    }
    
    public static class JRawContact implements Comparable<JRawContact>{
    	public String phonenumber;
    	public String displayname;
    	public long contactid;
    	public int contacttype;
    	
    	/**获取库Phon表字段**/  
        private static final String[] PHONES_PROJECTION = new String[] {  
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID, Phone.TYPE };  
         
        /**联系人显示名称**/  
        private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
          
        /**电话号码**/  
        private static final int PHONES_NUMBER_INDEX = 1;  
          
        /**头像ID**/  
        // private static final int PHONES_PHOTO_ID_INDEX = 2;  
         
        /**联系人的ID**/  
        private static final int PHONES_CONTACT_ID_INDEX = 3;  
        
        /**联系人类型**/
        private static final int PHONES_CONTACT_TYPE = 4;
        
        @Override
		public boolean equals(Object other){
			if(other == null){
				return false;
			}
			if(!(other instanceof JRawContact)){
				return false;
			}
			
			if(phonenumber.equals(((JRawContact)other).phonenumber)){
				return true;
			}
			return false;
		}
		
		@Override
		public int compareTo(JRawContact another) {
			int result = phonenumber.compareTo(another.phonenumber);
			return result;
		}
		
		@Override
		public int hashCode(){
			return phonenumber.hashCode();
		}
    }
    
    public static List<JRawContact> queryContact(Context context){
    	List<JRawContact> contacts = new ArrayList<JRawContact>();
        
    	/**得到手机通讯录联系人信息**/
    	ContentResolver resolver = context.getContentResolver();

    	// 获取手机联系人
    	Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, JRawContact.PHONES_PROJECTION, null, null, null);
		try{
    	if (phoneCursor != null) {
    		while (phoneCursor.moveToNext()) {

    			//得到手机号码
    			String phoneNumber = phoneCursor.getString(JRawContact.PHONES_NUMBER_INDEX);
    			//当手机号码为空的或者为空字段 跳过当前循环
    			if (TextUtils.isEmpty(phoneNumber))
    				continue;
    			
    			JRawContact contact = new JRawContact();
    			contact.phonenumber = phoneNumber;
    			contact.displayname = phoneCursor.getString(JRawContact.PHONES_DISPLAY_NAME_INDEX);
                contact.contactid = phoneCursor.getLong(JRawContact.PHONES_CONTACT_ID_INDEX);
                contact.contacttype = phoneCursor.getInt(JRawContact.PHONES_CONTACT_TYPE);
    			
    			contacts.add(contact);
    		}
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(phoneCursor != null){
	    		phoneCursor.close();
    		}
    	}
		
    	return contacts;
    }
    
    /** 
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) 
     *  
     * @param context 
     */  
    public static void cleanInternalCache(Context context) {  
        deleteFilesByDirectory(context.getCacheDir());  
    }  
   
    /** 
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) 
     *  
     * @param context 
     */  
    public static void cleanDatabases(Context context) {  
    	String path =  context.getFilesDir().getPath();
    	path = path.substring(0, path.lastIndexOf(File.separator)+1);
        deleteFilesByDirectory(new File( path + "/databases"));  
    }  
   
    /** 
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) 
     *  
     * @param context 
     */  
    public static void cleanSharedPreference(Context context) {  
    	String path =  context.getFilesDir().getPath();
    	path = path.substring(0, path.lastIndexOf(File.separator)+1);
        deleteFilesByDirectory(new File(path + "/shared_prefs"));  
    }  
   
    /** 
     * 按名字清除本应用数据库 
     *  
     * @param context 
     * @param dbName 
     */  
    public static void cleanDatabaseByName(Context context, String dbName) {  
        context.deleteDatabase(dbName);  
    }  
   
    /** 
     * 清除/data/data/com.xxx.xxx/files下的内容 
     *  
     * @param context 
     */  
    public static void cleanFiles(Context context) {  
        deleteFilesByDirectory(context.getFilesDir());  
    }  
   
    /** 
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) 
     *  
     * @param context 
     */  
    public static void cleanExternalCache(Context context) {  
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
            deleteFilesByDirectory(context.getExternalCacheDir());  
        }  
    }  
   
    /** 
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 
     *  
     * @param filePath 
     */  
    public static void cleanCustomCache(String filePath) {  
        deleteFilesByDirectory(new File(filePath));  
    }  
   
    /** 
     * 清除本应用所有的数据 
     *  
     * @param context 
     * @param filepath 
     */  
    public static void cleanApplicationData(Context context, String... filepath) {  
        cleanInternalCache(context);  
        cleanExternalCache(context);  
        cleanDatabases(context);  
        cleanSharedPreference(context);  
        cleanFiles(context);  
        for (String filePath : filepath) {  
            cleanCustomCache(filePath);  
        }  
    }  
   
    /** 
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 
     *  
     * @param directory 
     */  
    private static void deleteFilesByDirectory(File directory) {  
        if (directory != null && directory.exists() && directory.isDirectory()) {  
            for (File item : directory.listFiles()) {  
                item.delete();  
            }  
        }  
    }


    public static boolean isRemoteProcess(Context context) {
        boolean isRemote = false;
        String processName = getProcessName(context);
        if ((null != processName && !processName.equals(context.getPackageName()))
                || null == processName) {
            isRemote = true;
        }
        return isRemote;
    }

    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : processInfos) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static void openStrictMode(Context context){
        if (JConstant.debuggable){
            StrictMode.enableDefaults();
        }
    }
}
