package com.zhcw.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;


import com.zhcw.lib.base.BaseApplication;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;

import androidx.core.app.ActivityCompat;

/**
 * 设备信息
 */
public class DeviceID {
    private Context context;

    private DeviceID(Context context) {
        this.context = context;
    }

    private static DeviceID instance = null;

    public static DeviceID getInstance() {
        if (instance == null)
            instance = new DeviceID(BaseApplication.getAppContext());
        return instance;
    }

    public static void onDestroy() {
        instance = null;
    }

    public String getImei() {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return szImei;
        }
        szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }

    public String getDeviceId() {
        String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        return android_id;
    }

    public String getImsi() {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String szImsi = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return szImsi;
        }
        szImsi = TelephonyMgr.getSubscriberId();
        return szImsi;
    }

    public String getSN() {
        String SerialNumber = Build.SERIAL;
        //System.out.println("getSN() = "+SerialNumber);
        return SerialNumber;
    }

    public String getSSN() {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String SimSerialNumber = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return SimSerialNumber;
        }
        SimSerialNumber = TelephonyMgr.getSimSerialNumber();
        return SimSerialNumber;
    }

    public String getBuildID() {
//        System.err.println(Build.BRAND);
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        //System.out.println("getBuildID() = "+m_szDevIDShort);
        return m_szDevIDShort;
    }

    public String getAndroidID() {
        String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        //System.out.println("getAndroidID() = "+m_szAndroidID);
        return m_szAndroidID;
    }

    public String getMAC() {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        //System.out.println("getMAC() = "+m_szWLANMAC);
        return m_szWLANMAC;
    }

    public String getBlueTooth() {
        BluetoothAdapter m_BluetoothAdapter = null;
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        //System.out.println("getBlueTooth() = "+m_szBTMAC);
        return m_szBTMAC;
    }

    public String getFileMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        // System.out.println("getFileMac() = "+macSerial);
        return macSerial;
    }

    /**
     *   deviceId （md5）
     * @return
     */
    public String getDeviceID() {
        String m_szLongID = getImei() +
                getImsi() +
                getSN() +
                getSSN() +
                getBuildID() +
                getAndroidID();
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();

        //System.out.println("getDeviceID() = "+m_szUniqueID);
        return m_szUniqueID;
    }
}
