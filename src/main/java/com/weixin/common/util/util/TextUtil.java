package com.weixin.common.util.util;

public class TextUtil {
	public static boolean notEmpty(String str){
		if(str!=null&&!str.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isEmpty(String str){
		if(str!=null&&!str.trim().equals("")){
			return false;
		}else{
			return true;
		}
	}

	public static String getNotEmptyStr(String str){
		if(notEmpty(str)){
			return str;
		}
		return "";
	}

	//删除String数组中的空值
	public static String[] replaceNull(String[] str){
		//用StringBuffer来存放数组中的非空元素，用“;”分隔
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<str.length; i++) {
			if("".equals(str[i])) {
				continue;
			}
			sb.append(str[i]);
			if(i != str.length - 1) {
				sb.append(";");
			}
		}
		//用String的split方法分割，得到数组
		str = sb.toString().split(";");
		return str;
	}
}
