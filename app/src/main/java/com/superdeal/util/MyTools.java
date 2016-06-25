package com.superdeal.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class MyTools {

    private final static String KEY_ALGORITHM = "AES";
    private final static String KEY = "kj&84%jd#ad,g.9/0";
    private final static int KEY_SIZE = 16;
    private final static String IV_SPEC = "d.f9@#q<34>k/kf^v8&qk";
    private final static int IV_SPEC_SIZE = 16;
    private final static String MODEL = "AES/CBC/PKCS5Padding";

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static byte[] encrypt(byte[] content) {
        try {
            SecretKeySpec skeySpec = getKey(KEY);
            IvParameterSpec iv = getIvKey(IV_SPEC);
            Cipher cipher = Cipher.getInstance(MODEL);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 解密
     *
     * @param content
     * @return
     */
    public static byte[] decrypt(byte[] content) {
        try {
            SecretKeySpec skeySpec = getKey(KEY);
            IvParameterSpec iv = getIvKey(IV_SPEC);
            Cipher cipher = Cipher.getInstance(MODEL);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(content);
        } catch (Exception e) {
        }
        return content;
    }

    /**
     * 对字符串进行加密
     *
     * @param clearText
     * @return
     */
    public static String encrypt(String clearText) {
        byte[] result = null;
        try {
            result = encrypt(clearText.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null && result.length > 0) {
            return changeHexToString(result, result.length);
        }

        return "";

    }

    /**
     * 对字符串进行解密
     *
     * @param encrypted
     * @return
     */
    public static String decrypt(String encrypted) {
        try {
            byte[] result = decrypt(hexStringToBytes(encrypted));
            String coentn = new String(result);
            return coentn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[KEY_SIZE];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new SecretKeySpec(arrB, KEY_ALGORITHM);
    }

    private static IvParameterSpec getIvKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[IV_SPEC_SIZE];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new IvParameterSpec(arrB);
    }

    // -----------------------------------------------------------------------------------------

    /**
     * 解压
     *
     * @param buffer
     * @return
     */
    public static byte[] revProcessData(byte[] buffer) throws Exception {
        Inflater decompressor = new Inflater();
        decompressor.setInput(buffer);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(buffer.length);
        byte[] buf = new byte[1024];

        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer = bos.toByteArray();

//		return decrypt(buffer);

        return buffer;
    }

    /**
     * MD5加密，32位
     *
     * @param str
     * @return
     */
    public static String getMD5_32(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 获取android设备的唯一编码(有无手机卡都可以，有无wifi都可以，wifi是否打开都可以，是否是手机都可以)
     *
     * @param context
     * @return 唯一序列号
     */
    public static String getUUID(Context context) {

        String deviceId = SP.getSp(context).getString(SP.DEVICE_ID, "");
        if (TextUtils.isEmpty(deviceId)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            // If running on an emulator
            if (deviceId == null || deviceId.trim().length() == 0 || deviceId.matches("0+")) {
                deviceId = (new StringBuilder("EMU")).append((new Random(System.currentTimeMillis())).nextLong()).toString();
            }
            SP.getEdit(context).putString(SP.DEVICE_ID, deviceId).commit();
        }

        return deviceId;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 把十六进制的数组变成十六进制字符串 比如输入39 90 94 ----输出399094
     *
     * @param buffer
     * @param size
     * @return
     */
    public static String changeHexToString(byte[] buffer, int size) {
        StringBuffer strbuf = new StringBuffer("");
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            int temp = buffer[i] & 0xFF;
            String strTemp = Integer.toHexString(temp).toUpperCase();
            if (strTemp.length() < 2) {
                strbuf.append(0);
            }
            strbuf.append(strTemp);
        }
        return strbuf.toString().toUpperCase();
    }

    /**
     * 把指定范围内的数据转成HEX字符串 比如输入39 90 94 ----输出399094
     *
     * @param buffer
     * @param size
     * @return
     */
    public static String changeHexToString(byte[] buffer, int start, int bytecount) {
        StringBuffer strbuf = new StringBuffer("");
        for (int i = 0; i < bytecount; i++) {
            int temp = buffer[start++] & 0xFF;
            String strTemp = Integer.toHexString(temp);
            if (strTemp.length() < 2) {
                strbuf.append(0);
            }
            strbuf.append(strTemp);
        }
        return strbuf.toString().toUpperCase();
    }

    /**
     * 把十六进制字符串变成byte数组 比如输入32383090---输出32 38 30 90
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase(Locale.getDefault());
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            data[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return data;
    }

    // 获取字符串对应的16进制数
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 把ascii变成16进制字符串 比如，输入SHU---534855
     *
     * @param Ascii
     * @return
     */
    public static String AsciiToHexString(String Ascii) {
        char[] ch = Ascii.toCharArray();
        String temp;
        StringBuilder tmpSB = new StringBuilder();
        for (int i = 0; i < ch.length; i++) {
            temp = String.format("%02x", (int) ch[i]);

            if (temp.length() == 4) {
                tmpSB.append(temp.substring(2, 4));
            } else {
                tmpSB.append(temp);
            }
        }

        return tmpSB.toString();
    }

    /**
     * 获取ABCDEFG
     *
     * @param index
     * @return
     */
    public static String getIndex(int index) {
        String[] buffer = "A&B&C&D&E&F&G&H&I&J&K&L&M&N&O&P&Q&R&S&T&U&V&W&X&Y&Z".split("&");

        if (index <= buffer.length) {
            return buffer[index] + ".";
        }

        return "";
    }

    /**
     * 获取月份简称
     *
     * @param index
     * @return
     */
    public static String getMonthString(int index) {
        String month = "";
        switch (index) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sep";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;
            default:
                break;
        }

        return month;
    }

    /**
     * 获取软件版本号
     *
     * @param ctx
     * @return
     */
    public static String getAppVersionName(Context ctx) {
        String result;
        try {
            PackageManager pm = ctx.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            result = String.valueOf(pi.versionName);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getAppVersionCode(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 是否有合法的Intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }
}
