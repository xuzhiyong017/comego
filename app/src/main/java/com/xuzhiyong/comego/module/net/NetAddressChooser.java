package com.xuzhiyong.comego.module.net;

import android.text.TextUtils;

import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JDayDayUp;
import com.duowan.fw.util.JFP;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiehao on 16-1-5.
 */
public class NetAddressChooser {

    private static final String sAddressKey = "__last_valid_server_address";
    private static final String sAddressTypeKey = "__address_type_test_or_official";
    private static final String sAddressTypeOfficial = "official";
    private static final String sAddressTypeTest = "test";
    private static final String sTestAddressStr = "localhost";
    private static final String sOfficialAddressStr = "cpssdk.yy.com";
    private static final String sNormalPort = "8080";
    private static final String sSecurityPort = "8099";
    private static final String sSuffix = "/comego/";

    private static String sAddressStr = sOfficialAddressStr;
    private static String sCurrentAddress = null;
    private static volatile long sDay = 0;
    private static volatile boolean sNeedChoose = false;
    private static final byte[] sLock = new byte[0];
    private static ArrayList<String> sDispatchIps = new ArrayList<String>();

    private static String bankupIP = "58.215.183.168";
    private static String[] serviceIps = new String[]{"58.215.183.172",
            "58.215.183.168", "122.193.200.171","122.193.200.167",
            "120.195.155.43", "120.195.155.39"};


    static {
        sAddressStr = JConfig.getString(sAddressTypeKey, sAddressTypeOfficial)
                .equals(sAddressTypeOfficial) ?
                sOfficialAddressStr : sTestAddressStr;
    }

    public static List<String> sTrustHostNames = new ArrayList<>(5);

    private static Retrofit mRetrofit;

    public static boolean isInTestEnvironment() {
        String env = JConfig.getString(sAddressTypeKey, sAddressTypeOfficial);
        return sAddressTypeTest.equals(env);
    }

    public static void switchEnvTest() {
        synchronized (sLock) {
            JConfig.putString(sAddressTypeKey, sAddressTypeTest);
            setCurrentSelectAddressByNetChange(sTestAddressStr);
            sAddressStr = sTestAddressStr;
            sNeedChoose = true;
            initRetrofit();
        }
    }

    public static void switchEnvOfficial() {
        synchronized (sLock) {
            JConfig.putString(sAddressTypeKey, sAddressTypeOfficial);
            setCurrentSelectAddressByNetChange(sOfficialAddressStr);
            sAddressStr = sOfficialAddressStr;
            sNeedChoose = true;
            initRetrofit();
        }
    }

    public static void initRetrofit() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(getUrl())
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static boolean gotNewDay() {
        long curDay = JDayDayUp.day(0);
        if (curDay != sDay) {
           // sDay = curDay;
            return true;
        }
        return false;
    }

    public static String getUrl() {
//        if (sNeedChoose || null == sCurrentAddress || gotNewDay()) {
//            synchronized (sLock) {
//                if (sNeedChoose || null == sCurrentAddress || gotNewDay()) {
//                    chooseBestAddress(sDispatchIps);
//                    sNeedChoose = false;
//                    sDay = JDayDayUp.day(0);
//                }
//            }
//        }
//        if(TextUtils.isEmpty(sCurrentAddress)){
//            sCurrentAddress = bankupIP;
//        }
//        if(!checkIpValid(sCurrentAddress)){
//            sCurrentAddress = bankupIP;
//        }
        sCurrentAddress = sTestAddressStr;
        return JStringUtils.combineStr("http://", sCurrentAddress, ":", sNormalPort, sSuffix);
    }

    /**
     * 当前ip是否是合法的ip
     * @param currip
     * @return
     */
    private static boolean checkIpValid(String currip){
        if(sAddressStr.equals(sAddressTypeOfficial)){
            if(serviceIps != null && serviceIps.length >0){
                for(String ip : serviceIps){
                    if(currip.equals(ip)){
                        return true;
                    }
                }
                return  false;
            }else {
                return true;
            }
        }
        return true;

    }

    public static String getHttpsUrl() {
        if (sNeedChoose || null == sCurrentAddress || gotNewDay()) {
            synchronized (sLock) {
                if (sNeedChoose || null == sCurrentAddress || gotNewDay()) {
                    chooseBestAddress(sDispatchIps);
                    sNeedChoose = false;
                    sDay = JDayDayUp.day(0);
                }
            }
        }
        if(TextUtils.isEmpty(sCurrentAddress)){
            sCurrentAddress = bankupIP;
        }
        if(!checkIpValid(sCurrentAddress)){
            sCurrentAddress = bankupIP;
        }
        return JStringUtils.combineStr("https://", sCurrentAddress, ":", sSecurityPort, sSuffix,"?r="+new Random(System.currentTimeMillis()).nextInt());
    }

    public static void fillDispatchIps(List<String> ips) {
        synchronized (sLock) {
            sDispatchIps.clear();

            if (!JFP.empty(ips)) {
                sDispatchIps.addAll(ips);
            }
        }
        sDay = 0;
    }

    private static void chooseBestAddress(ArrayList<String> dispatchIps) {
        String lastAddress = JConfig.getString(sAddressKey, sAddressStr);
        chooseBestAddress(lastAddress, dispatchIps, false);
    }

    /**
     * 清楚当前的网络状态
     */
    public static void clearNetState(){
        JConfig.putString(sAddressKey, sAddressStr);
        sNeedChoose = true;
        sCurrentAddress = null;
    }

    private static void chooseBestAddress(final String lastAddress, List<String> dispatchIps, boolean fromSmartDns){
        final int ntimeout = 7000;
        JLog.debug(NetModule.KNet, "choose best address with last address: (%s) and dispatchIps %s",
                lastAddress, getIpString(dispatchIps));

        try {
            InetAddress[] addresses;

            if(JFP.empty(dispatchIps)) {
                addresses = InetAddress.getAllByName(lastAddress);
                JLog.debug(NetModule.KNet, "choose best address by last address: %s", getAddressString(addresses));
            } else {
                addresses = new InetAddress[dispatchIps.size()];
                for(int i = 0; i < addresses.length; i++) {
                    addresses[i] = InetAddress.getByName(dispatchIps.get(i));
                }
                JLog.debug(NetModule.KNet, "choose best address by dispatchIps: %s", getAddressString(addresses));
            }

            final InetAddress[] allAddress = addresses;

            if (allAddress.length == 1) {
                setCurrentSelectAddress(allAddress[0].getHostAddress());
                return;
            }

            if(allAddress.length == 0) {
                setCurrentSelectAddress(lastAddress);
                return;
            }

            final int maxThread = 5;
            final AtomicBoolean finished = new AtomicBoolean(false);
            final AtomicInteger tryedThread = new AtomicInteger();
            final AtomicInteger choose = new AtomicInteger(-1);
            final ArrayList<Thread> speedTestThreads = new ArrayList<Thread>();
            try {
                int length = allAddress.length;
                int offset = allAddress.length - maxThread;
                if (offset > 0) {
                    // random choose
                    offset = (int)(Math.random() * offset);
                    length = maxThread;
                }else {
                    offset = 0;
                }
                for (int i = offset; i < length; i++) {
                    final int cur = i % allAddress.length;
                    speedTestThreads.add(new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Socket socket = new Socket();
                            try {
                                InetSocketAddress address = new InetSocketAddress(allAddress[cur], Integer.valueOf(sNormalPort));
                                JLog.debug(NetModule.KNet, "choose best address try connect(%s): %s", cur, address.toString());
                                socket.connect(address, ntimeout);
                                if (socket.isConnected() && finished.compareAndSet(false, true)) {
                                    // the first connection established
                                    choose.set(cur);
                                    synchronized (finished) {
                                        finished.notify();
                                    }
                                    JLog.debug(NetModule.KNet, "choose best address of connect(%s): %s", cur, address.toString());
                                    return;
                                }
                                if(tryedThread.decrementAndGet() == 0){
                                    // all connection errored
                                    synchronized (finished) {
                                        finished.notify();
                                    }
                                }
                            }  catch (IOException e) {
                                e.printStackTrace();
                                if(tryedThread.decrementAndGet() == 0){
                                    // all connection exception
                                    synchronized (finished) {
                                        finished.notify();
                                    }
                                    JLog.debug(NetModule.KNet, "choose best address out of connection, no valid address");
                                }
                            } finally{
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }));
                }
                // start the speed try threads
                tryedThread.set(speedTestThreads.size());
                for (int i = 0; i < speedTestThreads.size(); i++) {
                    speedTestThreads.get(i).start();
                }
                // wait the first connection build
                synchronized (finished) {
                    finished.wait();
                }
            } catch (InterruptedException e) {
                // thread killed by system or other reason
                setCurrentSelectAddress(lastAddress);
                e.printStackTrace();
            }
            // all can not connected
            if (choose.intValue() == -1) {
                choose.set((int)(Math.random() * allAddress.length));
            }
            // build address
            setCurrentSelectAddress(allAddress[choose.intValue()].getHostAddress());
        } catch (UnknownHostException e) {
            JLog.error(NetModule.KNet, e.toString());
            if (!fromSmartDns) {
                setBySmartDns(lastAddress);
            } else {
                setCurrentSelectAddress(lastAddress);
            }
            e.printStackTrace();
        } catch (Exception e) {
            JLog.error(NetModule.KNet, e.toString());
        }
    }

    /**
     * 测试与正式线切换
     * @am address
     */
    private static void setCurrentSelectAddressByNetChange(String address){
        sCurrentAddress = address;
        if (!sTrustHostNames.contains(address)) {
            sTrustHostNames.add(address);
        }
        JConfig.putString(sAddressKey, address);
        sDispatchIps.clear();

    }
    private static void setCurrentSelectAddress(String address) {
        if (address.contains(sOfficialAddressStr)) {
            address = bankupIP;
        }
        sCurrentAddress = address;
        if (!sTrustHostNames.contains(address)) {
            sTrustHostNames.add(address);
        }

        JConfig.putString(sAddressKey, address);


    }

    private static void setBySmartDns(String lastAddress) {
        chooseBestAddress(lastAddress,  Arrays.asList(serviceIps), true);
    }

    private static String getIpString(List<String> ips) {
        if (ips == null) {
            return "[]";
        }

        int cnt = ips.size();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String addr : ips ) {
            sb.append(addr);

            --cnt;
            if (cnt != 0) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String getAddressString(InetAddress[] addresses) {
        if (addresses == null) {
            return "[]";
        }

        int cnt = addresses.length;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (InetAddress addr : addresses ) {
            sb.append(addr.toString());

            --cnt;
            if (cnt != 0) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
