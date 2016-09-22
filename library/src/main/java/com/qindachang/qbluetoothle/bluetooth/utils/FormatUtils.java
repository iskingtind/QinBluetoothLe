package com.qindachang.qbluetoothle.bluetooth.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qin on 2016/4/16.
 */
public class FormatUtils {

    public static String byte2HexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < 20; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + " ");
        }
        return stringBuilder.toString();
    }

    public static List<Integer> byte2Integer(byte[] bytes) {
        List<Integer> list = new ArrayList<>();
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            list.add(v);
        }
        return list;
    }

    public static List<Integer> byte2Integer(byte[] bytes, int len) {
        List<Integer> list = new ArrayList<>();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i=0;i<len;i++) {
            int v = bytes[i]&0xFF;
            list.add(v);
        }
        return list;
    }


    public static List<String> integer2String(List<Integer> list) {
        int count = list.size();
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strList.add(String.format("%s", list.get(i)));
        }
        return strList;
    }

    public static char ascii2Char(int ASCII) {
        return (char) ASCII;
    }

    public static short bytes2Short(byte[] bytes) {
        return (short) (((bytes[1] << 8) | bytes[0] & 0xff));
    }

}
