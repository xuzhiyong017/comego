package com.duowan.fw.util;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JStringUtils {

	public static final String EMPTY_STR = "";

    public static final boolean IGNORE_CASE = true;
    public static final boolean IGNORE_WIDTH = true;

    public static boolean isNullOrEmpty(String str) {
        return JFP.empty(str);
    }

    public static boolean isAllWhitespaces(String str) {
        boolean ret = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static boolean isAllDigits(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean equal(String s1, String s2) {
        return equal(s1, s2, false);
    }

    public static boolean equal(String s1, String s2, boolean ignoreCase) {
        if (s1 != null && s2 != null) {
            if (ignoreCase) {
                return s1.equalsIgnoreCase(s2);
            }
            else {
                return s1.equals(s2);
            }
        }
        else {
            return ((s1 == null && s2 == null) ? true : false);
        }
    }

    public static Vector<String> parseMediaUrls(String str, String beginTag, String endTag) {
        Vector<String> list = new Vector<String>();
        if (!isNullOrEmpty(str)) {
            int beginIndex = str.indexOf(beginTag, 0);
            int endIndex = str.indexOf(endTag, 0);
            while ((beginIndex != -1 && endIndex != -1) && (endIndex > beginIndex)) {
                beginIndex += beginTag.length();
                String imgUrl = str.substring(beginIndex, endIndex);
                if (!isNullOrEmpty(imgUrl) && imgUrl.charAt(0) != '[') {
                    list.add(imgUrl);
                }
                endIndex += endIndex + endTag.length();
                beginIndex = str.indexOf(beginTag, endIndex);
                endIndex = str.indexOf(endTag, endIndex);
            }
        }
        return list;
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive ver.
     */
    public static int find(String pattern, String s) {
        return find(pattern, s, !IGNORE_CASE);
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive can be parameterized
     */
    public static int find(String pattern, String s, boolean ignoreCase) {
        return find(pattern, s, ignoreCase, !IGNORE_WIDTH);
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive and Full/Half width ignore can
     * be parameterized
     */
    public static int find(String pattern, String s, boolean ignoreCase, boolean ignoreWidth) {
        if (JFP.empty(s))
            return -1;
        pattern = JFP.ref(pattern);
        if (ignoreCase) {
            pattern = pattern.toLowerCase();
            s = s.toLowerCase();
        }
        if (ignoreWidth) {
            pattern = narrow(pattern);
            s = narrow(s);
        }
        return s.indexOf(pattern);
    }

    public static String narrow(String s) {
        if (JFP.empty(s))
            return "";
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; ++i)
            cs[i] = narrow(cs[i]);
        return new String(cs);
    }

    public static char narrow(char c) {
        int code = c;
        if (code >= 65281 && code <= 65373)// Interesting range
            return (char) (code - 65248); // Full-width to half-width
        else if (code == 12288) // Space
            return (char) (code - 12288 + 32);
        else if (code == 65377)
            return (char) (12290);
        else if (code == 12539)
            return (char) (183);
        else if (code == 8226)
            return (char) (183);
        else
            return c;
    }

    public static int ord(char c) { 
        if ('a' <= c && c <= 'z')
            return (int)c;
        if ('A' <= c && c <= 'Z')
            return c - 'A' + 'a';
        return 0;
    }

    public static int compare(String x, String y) {
        return JFP.ref(x).compareTo(JFP.ref(y));
    }
    
    public static String removeChar(String s, char c) {
    	StringBuffer r = new StringBuffer();
    	for (int i = 0; i < s.length(); i ++) {
    		char cur = s.charAt(i);
    		if (cur != c) r.append(cur);
    	}
    	return r.toString();
	}
    
    public static String combineStr(Object...objects) {
    	StringBuilder sb = new StringBuilder();
    	for(Object s : objects) {
    		sb.append(s.toString());
    	}
    	return sb.toString();
    }

	/**
	 * 加判空
	 * @param objects
	 * @return
	 */
	public static String safeCombineStr(Object...objects) {
		StringBuilder sb = new StringBuilder();
		for(Object s : objects) {
			if(s != null) {
				sb.append(s.toString());
			}
		}
		return sb.toString();
	}

    public static boolean checkLength(String str, int min, int max) {
        if (isNullOrEmpty(str)) {
            return false;
        }

        int length = str.length();
        return length <= max && length >= min;
    }

	public static boolean contains(String str, String searchStr) {
		return str != null && searchStr != null ? str.indexOf(searchStr) >= 0 : false;
	}

    public static String floatToPercentage(float n) {
        return String.format("%.0f",n*100)+"%";
    }

    public static boolean isValidURL(String urlString) {
        if (isNullOrEmpty(urlString)) {
            return false;
        }
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
        Pattern patt = Pattern. compile(regex );
        Matcher matcher = patt.matcher(urlString);
        return matcher.matches();
    }
}
