package com.hotent.platform.ip.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.BeanUtils;

public class StringUtil
{
  public static String escape(String src)
  {
    StringBuffer tmp = new StringBuffer();
    tmp.ensureCapacity(src.length() * 6);

    for (int i = 0; i < src.length(); ++i)
    {
      char j = src.charAt(i);

      if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || (Character.isUpperCase(j)))
      {
        tmp.append(j);
      } else if (j < 256) {
        tmp.append("%");
        if (j < '\16')
          tmp.append("0");
        tmp.append(Integer.toString(j, 16));
      } else {
        tmp.append("%u");
        tmp.append(Integer.toString(j, 16));
      }
    }
    return tmp.toString();
  }

  public static String replaceVariable(String template, Map<String, String> map) throws Exception
  {
    Pattern regex = Pattern.compile("\\{(.*?)\\}");
    Matcher regexMatcher = regex.matcher(template);
    while (regexMatcher.find()) {
      String key = regexMatcher.group(1);
      String toReplace = regexMatcher.group(0);
      String value = (String)map.get(key);
      if (value != null)
        template = template.replace(toReplace, value);
      else
        throw new Exception(new StringBuilder().append("没有找到[").append(key).append("]对应的变量值，请检查表变量配置!").toString());
    }

    return template;
  }

  public static String unescape(String src)
  {
	  StringBuffer tmp = new StringBuffer();
	    tmp.ensureCapacity(src.length());
	    int lastPos = 0; int pos = 0;

	    while (lastPos < src.length()) {
	      pos = src.indexOf("%", lastPos);
	      if (pos == lastPos) {
	        if (src.charAt(pos + 1) == 'u') {
	          char ch = (char)Integer.parseInt(src.substring(pos + 2, pos + 6), 16);

	          tmp.append(ch);
	          lastPos = pos + 6;
	        } else {
	          char ch = (char)Integer.parseInt(src.substring(pos + 1, pos + 3), 16);

	          tmp.append(ch);
	          lastPos = pos + 3;
	        }
	      }
	      else if (pos == -1) {
	        tmp.append(src.substring(lastPos));
	        lastPos = src.length();
	      } else {
	        tmp.append(src.substring(lastPos, pos));
	        lastPos = pos;
	      }
	    }

	    return tmp.toString();  }

  public static boolean isExist(String content, String begin, String end)
  {
    String tmp = content.toLowerCase();
    begin = begin.toLowerCase();
    end = end.toLowerCase();
    int beginIndex = tmp.indexOf(begin);
    int endIndex = tmp.indexOf(end);

    return ((beginIndex != -1) && (endIndex != -1) && (beginIndex < endIndex));
  }

  public static String trimPrefix(String toTrim, String trimStr)
  {
    while (toTrim.startsWith(trimStr))
      toTrim = toTrim.substring(trimStr.length());

    return toTrim;
  }

  public static String trimSufffix(String toTrim, String trimStr)
  {
    while (toTrim.endsWith(trimStr))
      toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());

    return toTrim;
  }

  public static String trim(String toTrim, String trimStr)
  {
    return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
  }

  public static String escapeHtml(String content)
  {
    return StringEscapeUtils.escapeHtml(content);
  }

  public static String unescapeHtml(String content)
  {
    return StringEscapeUtils.unescapeHtml(content);
  }

  public static boolean isEmpty(String str)
  {
    if (str == null)
      return true;

    return (str.trim().equals(""));
  }

  public static boolean isNotEmpty(String str)
  {
    return (!(isEmpty(str)));
  }

  public static String replaceVariable(String template, String repaceStr) {
    Pattern regex = Pattern.compile("\\{(.*?)\\}");
    Matcher regexMatcher = regex.matcher(template);
    if (regexMatcher.find()) {
      String toReplace = regexMatcher.group(0);
      template = template.replace(toReplace, repaceStr);
    }
    return template;
  }

  public static String subString(String str, int len, String chopped)
  {
    if ((str == null) || ("".equals(str)))
      return "";
    char[] chars = str.toCharArray();
    int cnLen = len * 2;
    String tmp = "";
    boolean isOver = false;
    int iLen = 0;
    for (int i = 0; i < chars.length; ++i) {
      int iChar = chars[i];
      if (iChar <= 128)
        iLen += 1;
      else
        iLen += 2;
      if (iLen >= cnLen) {
        isOver = true;
        break;
      }

      tmp = new StringBuilder().append(tmp).append(String.valueOf(chars[i])).toString();
    }
    if (isOver)
      tmp = new StringBuilder().append(tmp).append(chopped).toString();

    return tmp;
  }

  public static String subString(String str)
  {
    int len = 25;
    String tmp = AppConfigUtil.get("titleLen");
    String regex = "<(?:(?:/([^>]+)>)|(?:!--([\\S|\\s]*?)-->)|(?:([^\\s/>]+)\\s*((?:(?:\"[^\"]*\")|(?:'[^']*')|[^\"'<>])*)/?>))";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    if (matcher.find())
      return str;

    if (isNotEmpty(tmp))
      len = Integer.parseInt(tmp);

    return subString(str, len, "...");
  }

  public static boolean isNumberic(String s)
  {
    if (StringUtils.isEmpty(s))
      return false;
    boolean rtn = validByRegex("^[-+]{0,1}\\d*\\.{0,1}\\d+$", s);
    if (rtn)
      return true;

    return validByRegex("^0[x|X][\\da-eA-E]+$", s);
  }

  public static boolean isInteger(String s)
  {
    boolean rtn = validByRegex("^[-+]{0,1}\\d*$", s);
    return rtn;
  }

  public static boolean isEmail(String s)
  {
    boolean rtn = validByRegex("(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*", s);

    return rtn;
  }

//增加身份证验证 
  public static boolean isIdcard(String s)
  {
	  boolean rtn = validByRegex("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", s);

	/*  boolean rtn = validByRegex("^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", s);*/
	  return rtn;
  }
  
//增加组织机构代码验证 
  public static boolean isOrgCode(String s)
  {
	  boolean rtn = validByRegex("(^([0-9A-Z]){8}-[0-9|X]$)", s);

	  return rtn;
  }
  
  public static boolean isMobile(String s)
  {
    boolean rtn = validByRegex("^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", s);

    return rtn;
  }

  public static boolean isPhone(String s)
  {
    boolean rtn = validByRegex("(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?", s);

    return rtn;
  }

  public static boolean isZip(String s)
  {
    boolean rtn = validByRegex("^[0-9]{6}$", s);
    return rtn;
  }

  public static boolean isQq(String s)
  {
    boolean rtn = validByRegex("^[1-9]\\d{4,9}$", s);
    return rtn;
  }

  public static boolean isIp(String s)
  {
    boolean rtn = validByRegex("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", s);

    return rtn;
  }

  public static boolean isChinese(String s)
  {
    boolean rtn = validByRegex("^[一-龥]+$", s);
    return rtn;
  }

  public static boolean isChrNum(String s)
  {
    boolean rtn = validByRegex("^([a-zA-Z0-9]+)$", s);
    return rtn;
  }

  public static boolean isUrl(String url)
  {
    return validByRegex("(http://|https://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?", url);
  }

  public static Boolean isJson(String json)
  {
    if (isEmpty(json))
      return Boolean.valueOf(false);
    try {
      JSONObject.fromObject(json);
      return Boolean.valueOf(true);
    } catch (JSONException e) {
      try {
        JSONArray.fromObject(json);
        return Boolean.valueOf(true); } catch (JSONException ex) { }
    }
    return Boolean.valueOf(false);
  }

  public static boolean validByRegex(String regex, String input)
  {
    Pattern p = Pattern.compile(regex, 2);
    Matcher regexMatcher = p.matcher(input);
    return regexMatcher.find();
  }

  public static boolean isNumeric(String str)
  {
    int i = str.length();
    do if (--i < 0) break;
    while (Character.isDigit(str.charAt(i)));
    return false;
  }

  public static String makeFirstLetterUpperCase(String newStr)
  {
    if (newStr.length() == 0)
      return newStr;

    char[] oneChar = new char[1];
    oneChar[0] = newStr.charAt(0);
    String firstChar = new String(oneChar);
    return new StringBuilder().append(firstChar.toUpperCase()).append(newStr.substring(1)).toString();
  }

  public static String makeFirstLetterLowerCase(String newStr)
  {
    if (newStr.length() == 0)
      return newStr;

    char[] oneChar = new char[1];
    oneChar[0] = newStr.charAt(0);
    String firstChar = new String(oneChar);
    return new StringBuilder().append(firstChar.toLowerCase()).append(newStr.substring(1)).toString();
  }

  public static String formatParamMsg(String message, Object[] args)
  {
    for (int i = 0; i < args.length; ++i)
      message = message.replace(new StringBuilder().append("{").append(i).append("}").toString(), args[i].toString());

    return message;
  }

  public static String formatParamMsg(String message, Map<String, ?> params)
  {
    if (params == null)
      return message;
    Iterator keyIts = params.keySet().iterator();
    while (keyIts.hasNext()) {
      String key = (String)keyIts.next();
      Object val = params.get(key);
      if (val != null)
        message = message.replace(new StringBuilder().append("${").append(key).append("}").toString(), val.toString());
    }

    return message;
  }

  public static StringBuilder formatMsg(CharSequence msgWithFormat, boolean autoQuote, Object[] args)
  {
    int argsLen = args.length;
    boolean markFound = false;

    StringBuilder sb = new StringBuilder(msgWithFormat);

    if (argsLen > 0) {
      for (int i = 0; i < argsLen; ++i) {
        String flag = new StringBuilder().append("%").append(i + 1).toString();
        int idx = sb.indexOf(flag);

        while (idx >= 0) {
          markFound = true;
          sb.replace(idx, idx + 2, toString(args[i], autoQuote));
          idx = sb.indexOf(flag);
        }
      }

      if (args[(argsLen - 1)] instanceof Throwable) {
        StringWriter sw = new StringWriter();
        ((Throwable)args[(argsLen - 1)]).printStackTrace(new PrintWriter(sw));

        sb.append("\n").append(sw.toString());
      } else if ((argsLen == 1) && (!(markFound))) {
        sb.append(args[(argsLen - 1)].toString());
      }
    }
    return sb;
  }

  public static StringBuilder formatMsg(String msgWithFormat, Object[] args) {
    return formatMsg(new StringBuilder(msgWithFormat), true, args);
  }

  public static String toString(Object obj, boolean autoQuote) {
    StringBuilder sb = new StringBuilder();
    if (obj == null) {
      sb.append("NULL");
    }
    else if (obj instanceof Object[]) {
      for (int i = 0; i < ((Object[])(Object[])obj).length; ++i)
        sb.append(((Object[])(Object[])obj)[i]).append(", ");

      if (sb.length() > 0)
        sb.delete(sb.length() - 2, sb.length());
    }
    else {
      sb.append(obj.toString());
    }

    if ((autoQuote) && (sb.length() > 0) && (((sb.charAt(0) != '[') || (sb.charAt(sb.length() - 1) != ']'))) && (((sb.charAt(0) != '{') || (sb.charAt(sb.length() - 1) != '}'))))
    {
      sb.insert(0, "[").append("]");
    }
    return sb.toString();
  }

  public static String returnSpace(String str) {
    String space = "";
    if (!(str.isEmpty())) {
      String[] path = str.split("\\.");
      for (int i = 0; i < path.length - 1; ++i)
        space = new StringBuilder().append(space).append("&nbsp;&emsp;").toString();
    }

    return space;
  }

  public static synchronized String encryptSha256(String inputStr)
  {
    MessageDigest md;
    try
    {
      md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(inputStr.getBytes("UTF-8"));
      return new String(Base64.encodeBase64(digest)); } catch (Exception e) {
    }
    return null;
  }

  public static synchronized String encryptMd5(String inputStr) {
	  try {
	      MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(inputStr.getBytes());
	      byte[] digest = md.digest();
	      StringBuffer sb = new StringBuffer();
	      for (byte b : digest) {
	        sb.append(Integer.toHexString(b & 0xFF));
	      }

	      return sb.toString(); } catch (Exception e) {
	    }
	    return null;
  }

  public static String getArrayAsString(List<String> arr)
  {
    if ((arr == null) || (arr.size() == 0))
      return "";
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < arr.size(); ++i) {
      if (i > 0)
        sb.append(",");
      sb.append((String)arr.get(i));
    }
    return sb.toString();
  }

  public static String getArrayAsString(String[] arr)
  {
    if ((arr == null) || (arr.length == 0))
      return "";
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < arr.length; ++i) {
      if (i > 0)
        sb.append("#");
      sb.append(arr[i]);
    }
    return sb.toString();
  }

  public static String getSetAsString(Set<?> set)
  {
    if ((set == null) || (set.size() == 0))
      return "";
    StringBuffer sb = new StringBuffer();
    int i = 0;
    Iterator it = set.iterator();
    while (it.hasNext()) {
      if (i++ > 0)
        sb.append(",");
      sb.append(it.next().toString());
    }
    return sb.toString();
  }

  public static String hangeToBig(double value)
  {
    char[] hunit = { 25342, 20336, 20191 };
    char[] vunit = { 19975, 20159 };
    char[] digit = { 38646, 22777, 36144, 21441, 32902, 20237, 38470, 26578, 25420, 29590 };
    String zheng = "整";
    String jiao = "角";
    String fen = "分";
    char yuan = 22278;
    long midVal = (long) (value * 100.0D);
    String valStr = String.valueOf(midVal);

    String head = valStr.substring(0, valStr.length() - 2);
    int len = head.length();
    if (len > 12)
      return "值过大";

    String rail = valStr.substring(valStr.length() - 2);

    String prefix = "";
    String suffix = "";

    if (rail.equals("00"))
      suffix = zheng;
    else {
      suffix = new StringBuilder().append(digit[(rail.charAt(0) - '0')]).append(jiao).append(digit[(rail.charAt(1) - '0')]).append(fen).toString();
    }

    char[] chDig = head.toCharArray();
    char zero = '0';
    byte zeroSerNum = 0;
    for (int i = 0; i < chDig.length; ++i) {
      int idx = (chDig.length - i - 1) % 4;
      int vidx = (chDig.length - i - 1) / 4;
      if (chDig[i] == '0') {
        zeroSerNum = (byte)(zeroSerNum + 1);
        if (zero == '0') {
          zero = digit[0];
        } else if ((idx == 0) && (vidx > 0) && (zeroSerNum < 4)) {
          prefix = new StringBuilder().append(prefix).append(vunit[(vidx - 1)]).toString();
          zero = '0';
        }
      }
      else {
        zeroSerNum = 0;
        if (zero != '0') {
          prefix = new StringBuilder().append(prefix).append(zero).toString();
          zero = '0';
        }
        prefix = new StringBuilder().append(prefix).append(digit[(chDig[i] - '0')]).toString();
        if (idx > 0)
          prefix = new StringBuilder().append(prefix).append(hunit[(idx - 1)]).toString();
        if ((idx == 0) && (vidx > 0))
          prefix = new StringBuilder().append(prefix).append(vunit[(vidx - 1)]).toString();
      }
    }

    if (prefix.length() > 0)
      prefix = new StringBuilder().append(prefix).append(yuan).toString();
    return new StringBuilder().append(prefix).append(suffix).toString();
  }

  public static String jsonUnescape(String str)
  {
    return str.replace("&quot;", "\"").replace("&nuot;", "\n");
  }

  public static String htmlEntityToString(String dataStr)
  {
    dataStr = dataStr.replace("&apos;", "'").replace("&quot;", "\"").replace("&gt;", ">").replace("&lt;", "<").replace("&amp;", "&");

    int start = 0;
    int end = 0;
    StringBuffer buffer = new StringBuffer();

    while (start > -1) {
      int system = 10;
      if (start == 0) {
        int t = dataStr.indexOf("&#");
        if (start != t)
          start = t;

        if (start > 0)
          buffer.append(dataStr.substring(0, start));
      }

      end = dataStr.indexOf(";", start + 2);
      String charStr = "";
      if (end != -1) {
        charStr = dataStr.substring(start + 2, end);

        char s = charStr.charAt(0);
        if ((s == 'x') || (s == 'X')) {
          system = 16;
          charStr = charStr.substring(1);
        }
      }
      try
      {
        if (isNotEmpty(charStr)) {
          char letter = (char)Integer.parseInt(charStr, system);
          buffer.append(new Character(letter).toString());
        }

      }
      catch (NumberFormatException e)
      {
      }

      start = dataStr.indexOf("&#", end);
      if (start - end > 1) {
        buffer.append(dataStr.substring(end + 1, start));
      }

      if (start == -1) {
        int length = dataStr.length();
        if (end + 1 != length)
          buffer.append(dataStr.substring(end + 1, length));
      }
    }

    return buffer.toString();
  }

  public static String stringToHtmlEntity(String str)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < str.length(); ++i) {
      char c = str.charAt(i);

      switch (c)
      {
      case '\n':
        sb.append(c);
        break;
      case '<':
        sb.append("&lt;");
        break;
      case '>':
        sb.append("&gt;");
        break;
      case '&':
        sb.append("&amp;");
        break;
      case '\'':
        sb.append("&apos;");
        break;
      case '"':
        sb.append("&quot;");
        break;
      default:
        if ((c < ' ') || (c > '~')) {
          sb.append("&#x");
          sb.append(Integer.toString(c, 16));
          sb.append(';');
        } else {
          sb.append(c);
        }
      }
    }
    return sb.toString();
  }

  public static String encodingString(String str, String from, String to)
  {
    String result = str;
    try {
      result = new String(str.getBytes(from), to);
    } catch (Exception e) {
      result = str;
    }
    return result;
  }

  public static String comdify(String value)
  {
    DecimalFormat df = null;
    if (value.indexOf(".") > 0) {
      int i = value.length() - value.indexOf(".") - 1;
      switch (i)
      {
      case 0:
        df = new DecimalFormat("###,##0");
        break;
      case 1:
        df = new DecimalFormat("###,##0.0");
        break;
      case 2:
        df = new DecimalFormat("###,##0.00");
        break;
      case 3:
        df = new DecimalFormat("###,##0.000");
        break;
      case 4:
        df = new DecimalFormat("###,##0.0000");
        break;
      default:
        df = new DecimalFormat("###,##0.00000");
      }
    }
    else
    {
      df = new DecimalFormat("###,##0");
    }
    double number = 0D;
    try {
      number = Double.parseDouble(value);
    } catch (Exception e) {
      number = 0D;
    }
    return df.format(number);
  }

  public static String convertScriptLine(String arg, Boolean flag)
  {
    if (StringUtils.isEmpty(arg))
      return arg;
    String origStr = "\n"; String targStr = "/n";
    if (!(flag.booleanValue())) {
      origStr = "/n";
      targStr = "\n";
    }
    String[] args = arg.split(origStr);
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < args.length; ++i) {
      sb.append(args[i]);
      if (args.length != i + 1)
        sb.append(targStr);
    }
    return sb.toString();
  }

  public static String convertLine(String arg, Boolean flag)
  {
    if (StringUtils.isEmpty(arg))
      return arg;
    String origStr = "\n"; String targStr = "/n";
    if (!(flag.booleanValue())) {
      origStr = "/n";
      targStr = "\n";
    }
    String[] args = arg.split(origStr);
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < args.length; ++i) {
      sb.append(StringUtils.deleteWhitespace(args[i]));
      if (args.length != i + 1)
        sb.append(targStr);
    }
    return sb.toString();
  }

  public static String deleteWhitespaceLine(String arg)
  {
    if (StringUtils.isEmpty(arg))
      return arg;
    String origStr = "\n";
    String[] args = arg.split(origStr);
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < args.length; ++i) {
      sb.append(StringUtils.deleteWhitespace(args[i]));
      if (args.length != i + 1)
        sb.append(origStr);
    }
    return sb.toString();
  }

  public static String parseText(String arg)
  {
    if (StringUtils.isEmpty(arg))
      return arg;
    String[] args = arg.split("\n");
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < args.length; ++i) {
      sb.append(args[i]);
      if (args.length != i + 1)
        sb.append("</br>");
    }
    return sb.toString();
  }

  public static String replaceNotVisable(String str)
  {
    char[] ary = str.toCharArray();
    List list = new ArrayList();
    for (int i = 0; i < ary.length; ++i) {
      int c = ary[i];
      if (!(isViable(c)))
        break;
   list.add(Character.valueOf((char)c));
    }
    Object[] aryc = (Object[])list.toArray();
    char[] arycc = new char[aryc.length];
    for (int i = 0; i < aryc.length; ++i)
      arycc[i] = ((Character)aryc[i]).charValue();

    String out = new String(arycc);
    return out;
  }

  private static boolean isViable(int i)
  {
    return ((i == 0) || (i == 13) || ((i >= 9) && (i <= 10)) || ((i >= 11) && (i <= 12)) || ((i >= 28) && (i <= 126)) || ((i >= 19968) && (i <= 40869)));
  }

  public static String replaceAll(String toReplace, String replace, String replaceBy)
  {
    replaceBy = replaceBy.replaceAll("\\$", "\\\\\\$");
    return toReplace.replaceAll(replace, replaceBy);
  }

  public static String stringFormat2Json(String json) {
	  StringBuilder sb = new StringBuilder();
	    int size = json.length();
	    for (int i = 0; i < size; i++) {
	      char c = json.charAt(i);
	      switch (c) {
	      case '\b':
	        sb.append("\\b");
	        break;
	      case '\f':
	        sb.append("\\f");
	        break;
	      case '\n':
	        sb.append("\\n");
	        break;
	      case '\r':
	        sb.append("\\r");
	        break;
	      case '\t':
	        sb.append("\\t");
	        break;
	      case '\013':
	      default:
	        sb.append(c);
	      }
    }

    return sb.toString();
  }

  public static String getNumber(Object value, Object isShowComdify, Object decimalValue, Object coinValue)
  {
    if (value == null)
      return "";
    String val = value.toString();

    if (isShowComdify != null) {
      boolean result = Boolean.valueOf(isShowComdify.toString()).booleanValue();
      Double douvalue = Double.valueOf(Double.parseDouble(val));
      DecimalFormat df = new DecimalFormat("");
      val = df.format(douvalue);
      if (!(result))
        val = val.replace(",", "");

    }

    if (decimalValue != null) {
      int len = Integer.parseInt(decimalValue.toString());

      if (len > 0) {
        int idx = val.indexOf(".");
        if (idx == -1) {
          val = new StringBuilder().append(val).append(".").append(getZeroLen(len)).toString();
        } else {
          String intStr = val.substring(0, val.indexOf("."));
          String decimal = val.substring(val.indexOf(".") + 1);
          if (decimal.length() > len) {
            Double douvalue = Double.valueOf(Double.parseDouble(val.replace(",", "")));

            DecimalFormat df = new DecimalFormat("");
            df.setMaximumFractionDigits(len);
            String tmp = df.format(douvalue);
            if (tmp.indexOf(".") == -1) {
              val = new StringBuilder().append(intStr).append(".").append(getZeroLen(len)).toString();
            } else {
              decimal = tmp.substring(tmp.indexOf(".") + 1);
              val = new StringBuilder().append(intStr).append(".").append(decimal).toString();
            }
          } else if (decimal.length() < len) {
            int tmp = len - decimal.length();
            val = new StringBuilder().append(val).append(getZeroLen(tmp)).toString();
          }
        }
      }
    }
    if (coinValue != null)
      val = new StringBuilder().append(coinValue.toString()).append(val).toString();

    return val;
  }

  private static String getZeroLen(int len) {
    String str = "";
    for (int i = 0; i < len; ++i)
      str = new StringBuilder().append(str).append("0").toString();

    return str;
  }

  public static String removeHTMLTag(String htmlStr)
  {
    if (isEmpty(htmlStr))
      return "";
    htmlStr = Jsoup.clean(htmlStr, Whitelist.none());
    htmlStr = htmlEntityToString(htmlStr);
    return htmlStr.trim();
  }

  public static boolean contain(String str, String searchStr)
  {
    return contain(str, searchStr, ",", true);
  }

  public static boolean contain(String str, String searchStr, String argumentSeparator, boolean isIgnoreCase)
  {
	  if (isEmpty(str))
	      return false;
	    if (isEmpty(argumentSeparator))
	      argumentSeparator = ",";
	    String[] aryStr = str.split(argumentSeparator);
	    return contain(aryStr, searchStr, isIgnoreCase);
  }

  public static boolean contain(String[] aryStr, String searchStr, boolean isIgnoreCase)
  {
	  if (BeanUtils.isEmpty(aryStr))
	      return false;
	    for (String str : aryStr) {
	      if (isIgnoreCase) {
	        if (str.equalsIgnoreCase(searchStr))
	          return true;
	      }
	      else if (str.equals(searchStr)) {
	        return true;
	      }
	    }

	    return false;
  }

  public static int getCount(String str, int type)
  {
    int len = str.length();
    int chineseCount = 0;
    int letterCount = 0;
    int blankCount = 0;
    int numCount = 0;
    int otherCount = 0;
    for (int i = 0; i < len; ++i) {
      char tem = str.charAt(i);
      Character.UnicodeBlock ub = Character.UnicodeBlock.of(tem);
      if (((tem > 'A') && (tem < 'Z')) || ((tem > 'a') && (tem < 'z')))
        ++letterCount;
      else if (tem == ' ')
        ++blankCount;
      else if ((tem > '0') && (tem < '9'))
        ++numCount;
      else if ((ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) || (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) || (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) || (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) || (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) || (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION))
      {
        ++chineseCount;
      }
      else ++otherCount;
    }

    switch (type) {
    case -1:
      return chineseCount;
    case 0:
      return letterCount;
    case 1:
      return blankCount;
    case 2:
      return numCount;
    case 3:
      return otherCount;
    }
    return otherCount;
  }

  public static int getTotalSize(String str) {
    int chineseCount = getCount(str, -1);
    int letterCount = getCount(str, 0);
    int blankCount = getCount(str, 1);
    int numCount = getCount(str, 2);
    int otherCount = getCount(str, 3);
    return (chineseCount + (letterCount + numCount) / 3 + blankCount / 4 + otherCount * 3 / 4);
  }
  public static String[] stringToString(String str) {
	   List list=new ArrayList();
	   list.add(str);
	    return (String[]) list.toArray();
	  }
}