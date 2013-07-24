/*
 * $Logfile: /cms40/main/src/com/founder/enp/util/StringValueUtils.java $
 * $Revision: 1 $
 * $Date: 07-07-24 15:36 $
 * $Author: Zhang_lijie $
 * $History: StringValueUtils.java $
 *
 * *****************  Version 1  *****************
 * User: Zhang_lijie  Date: 07-07-24   Time: 15:36
 * Created in $/cms40/main/src/com/founder/enp/util
 * java主程序
 *
 * *****************  Version 3  *****************
 * User: Xie_jian     Date: 06-10-10   Time: 15:41
 * Updated in $/chinadaily/enp/src/com/founder/enp/util
 * webwork及ibatis改造
 *
 * *****************  Version 2  *****************
 * User: Liu_dong     Date: 06-08-02   Time: 10:53
 * Updated in $/chinadaily/enp/src/com/founder/enp/util
 *
 * *****************  Version 1  *****************
 * User: Liu_dong     Date: 06-07-20   Time: 23:04
 * Created in $/chinadaily/enp/src/com/founder/enp/util
 *
 * Copyright (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.gracefully.util;

import java.util.Map;

/**
 * 从String字符串中获取相应的值
 *
 * @author Liudong
 * @version 1.0
 * Date:2003-12-4
 */
public class StringValueUtils
{
	/**
	 * 获取字符串中的int值,
	 * 如果在转换过程中出现任何错误则返回缺省值
	 *
	 * @param value 输入的字符串
	 * @param defaultValue 缺省值
	 * @return 对应的int值
	 */
	public static int getInt(String value, int defaultValue)
	{
		int result = defaultValue;
		try
		{
			result = Integer.parseInt(value);
		}
		catch (Exception ex)
		{
		}
		return result;
	}

    /**
     * 获取WebWork ActionContext中的参数
     * WebWork 中获取到的参数为Map的形式
     * key 是String类型
     * value是String[]类型的
     * 本函数固定取字符数组中的第一个值
     *
     * @param map parameters
     * @param key 要取的参数名称
     * @param defaultValue 缺省值
     * @return
     */
    public static int getWWPInt(Map map, String key, int defaultValue) {
        Object obj = map.get(key);
        if(obj == null)
            return defaultValue;
        else {
            if(obj instanceof String[]) {
                String[] sa = (String[])obj;
                if(sa == null || sa.length < 1)
                    return defaultValue;
                else {
                    String s = sa[0];
                    return getInt(s, defaultValue);
                }
            }
            else
                return defaultValue;
        }
    }

	/**
	 * 获取字符串中的int值,
	 * 如果在转换过程中出现任何错误则返回缺省值<b>0</b>
	 *
	 * @param value 输入的字符串
	 * @return 对应的int值
	 */
	public static int getInt(String value)
	{
		return getInt(value, 0);
	}

    public static int getWWPInt(Map map, String key) {
        return getWWPInt(map, key, 0);
    }

	/**
	 * 获取字符串中的long值,
	 * 如果在转换过程中出现任何错误则返回缺省值
	 *
	 * @param value 输入的字符串
	 * @param defaultValue 缺省值
	 * @return 对应的long值
	 */
	public static long getLong(String value, long defaultValue)
	{
		long result = defaultValue;
		try
		{
			result = Long.parseLong(value);
		}
		catch (Exception ex)
		{
		}
		return result;
	}

	/**
	 * 获取字符串中的long值,
	 * 如果在转换过程中出现任何错误则返回缺省值<b>0</b>
	 *
	 * @param value 输入的字符串
	 * @return 对应的long值
	 */
	public static long getLong(String value)
	{
		return getLong(value, 0L);
	}

	/**
	 * 获取字符串中的float值,
	 * 如果在转换过程中出现任何错误则返回缺省值
	 *
	 * @param value 输入的字符串
	 * @param defaultValue 缺省值
	 * @return 对应的float值
	 */
	public static float getFloat(String value, float defaultValue)
	{
		float result = defaultValue;
		try
		{
			result = Float.parseFloat(value);
		}
		catch (Exception ex)
		{
		}
		return result;
	}

	/**
	 * 获取字符串中的float值,
	 * 如果在转换过程中出现任何错误则返回缺省值<b>0.0</b>
	 *
	 * @param value 输入的字符串
	 * @return 对应的float值
	 */
	public static float getFloat(String value)
	{
		return getFloat(value, 0.0F);
	}

	/**
	 * 获取字符串中的double值,
	 * 如果在转换过程中出现任何错误则返回缺省值
	 *
	 * @param value 输入的字符串
	 * @param defaultValue 缺省值
	 * @return 对应的double值
	 */
	public static double getDouble(String value, double defaultValue)
	{
		double result = defaultValue;
		try
		{
			result = Double.parseDouble(value);
		}
		catch (Exception ex)
		{
		}
		return result;
	}

	/**
	 * 获取字符串中的double值,
	 * 如果在转换过程中出现任何错误则返回缺省值<b>0.0</b>
	 *
	 * @param value 输入的字符串
	 * @return 对应的double值
	 */
	public static double getDouble(String value)
	{
		return getDouble(value, 0.0D);
	}

	/**
	 * <p>
	 * 获取字符串中的boolean值,
	 * 如果在转换过程中出现任何错误则返回缺省值
	 * </p>
	 * <p>
	 * 其中:
	 * <ul>
	 *   <li>true</li>
	 *   <li>y</li>
	 *   <li>yes</li>
	 *   <li>1</li>
	 *   <li>on</li>
	 * </ul>
	 * 对应的字符串会返回true
	 * <ul>
	 *   <li>false</li>
	 *   <li>n</li>
	 *   <li>no</li>
	 *   <li>0</li>
	 *   <li>off</li>
	 * </ul>
	 * 对应的字符串会返回false
	 * </p>
	 *
	 * @param value 输入的字符串
	 * @param defaultValue 缺省值
	 * @return 对应的boolean值
	 */
	public static boolean getBoolean(String value, boolean defaultValue)
	{
		boolean result = defaultValue;
		if ("true".equalsIgnoreCase(value)
			|| "y".equalsIgnoreCase(value)
			|| "yes".equalsIgnoreCase(value)
			|| "1".equals(value)
			|| "on".equalsIgnoreCase(value))
		{
			result = true;
		}
		else if (
			"false".equals(value)
				|| "n".equals(value)
				|| "no".equalsIgnoreCase(value)
				|| "0".equals(value)
				|| "off".equalsIgnoreCase(value))
		{
			result = false;
		}
		return result;
	}

	/**
	 * <p>
	 * 获取字符串中的boolean值,
	 * 如果在转换过程中出现任何错误则返回缺省值<b>false</b>
	 * </p>
	 * <p>
	 * 其中:
	 * <ul>
	 *   <li>true</li>
	 *   <li>y</li>
	 *   <li>yes</li>
	 *   <li>1</li>
	 *   <li>on</li>
	 * </ul>
	 * 对应的字符串会返回true
	 * <ul>
	 *   <li>false</li>
	 *   <li>n</li>
	 *   <li>no</li>
	 *   <li>0</li>
	 *   <li>off</li>
	 * </ul>
	 * 对应的字符串会返回false
	 * </p>
	 *
	 * @param value 输入的字符串
	 * @return 对应的boolean值
	 */
	public static boolean getBoolean(String value)
	{
		return getBoolean(value, false);
	}

	/**
	 * 将类似于"2003-12-11 00:00:00"的字符串转化为"2003-12-11"
	 * @param str	原始的包含日期和时间的字符串
	 * @return   日期字符串
	 */
	public static String getDateFromStr(String str)
	{
		String date = null;
		char[] date1 = str.toCharArray();
		char[] date2 = new char[date1.length];
		for (int i = 0; i < date1.length - 9; i++)
		{
			date2[i] = date1[i];
		}
		date = String.copyValueOf(date2).trim();
		return date;
	}

    /**
     * 将输入的字符串按二进制转换为int类型
     * @param str 要转换的字符串
     * @return int类型值
     */
    public static int getIntFromString(String str)
    {
        int result = 0;
        try
        {
            result = Integer.parseInt(str, 2);
        }
        catch(Exception ex)
        {
        }
        return result;
    }

    /**
     * 将输入的字符串按二进制转换为long类型值
     * @param str 要转换的字符串
     * @return 转换后的long值
     */
    public static long getLongFromString(String str)
    {
        long result = 0;
        try
        {
            result = Long.parseLong(str, 2);
        }
        catch(Exception ex)
        {
        }
        return result;
    }

    /**
     * 获取WebWork ActionContext中的参数
     * WebWork 中获取到的参数为Map的形式
     * key 是String类型
     * value是String[]类型的
     * 本函数固定取字符数组中的第一个值
     *
     * @param map parameters
     * @param key 要取的参数名称
     * @param defaultValue 缺省值
     * @return
     */
    public static String getWWPString(Map map, String key, String defaultValue) {
        Object obj = map.get(key);
        if(obj == null)
            return defaultValue;
        else {
            if(obj instanceof String[]) {
                String[] sa = (String[])obj;
                if(sa == null || sa.length < 1)
                    return defaultValue;
                else {
                    return sa[0];
                }
            }
            else
                return defaultValue;
        }
    }

    public static String getWWPString(Map map, String key) {
        return getWWPString(map, key, "");
    }
    /**
     * 获取WebWork ActionContext中的参数
     * WebWork 中获取到的参数为Map的形式
     * key 是String类型
     * value是String[]类型的
     * 本函数固定取字符数组中的第一个值
     *
     * @param map parameters
     * @param key 要取的参数名称
     * @param defaultValue 缺省值
     * @return
     */
    public static String[] getWWPStringArray(Map map, String key, String[] defaultValue) {
        Object obj = map.get(key);
        if(obj == null)
            return defaultValue;
        else {
            if(obj instanceof String[]) {
                String[] sa = (String[])obj;

                    return sa;
            }
            else
                return defaultValue;
        }
    }
}
