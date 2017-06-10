package org.daijie.core.util.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * 表单验证工具类
 * 
 */
public class ValidateUtil {

	/// ///
	/// 任意字符串 /// 全角字符串 ///
	/// 全角空格为12288,半角空格为32
	/// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248 ///
	/**
	 * 半角转全角(SBC case)
	 */
	public static String toSBC(String input) { // 半角转全角：
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/// /// 转半角的函数(DBC case) ///
	/// 任意字符串

	/// 半角字符串 ///
	/// 全角空格为12288，半角空格为32
	/// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248 ///
	/** 
	 * 全角转半角的函数(DBC case) 
	 */
	public static String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 是否为空
	 * 
	 */
	public static boolean isNotNull(String str) {
		if (str == null) {
			return false;
		}
		if ("".equals(str.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * 是否为中文
	 * 
	 */
	public static boolean isZnCh(String str) {
		boolean bl = true;
		if (!isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				String s = str.substring(i, i + 1);
				byte[] b = s.getBytes();
				if (b.length == 1) {
					bl = false;
					break;
				}
			}
		}
		return bl;
	}

	/**
	 * 是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isHasChinese(String str) {
		if (!ValidateUtil.isEmpty(str)) {
			String regex = ".*[\u4E00-\u9FA5]+.*";
			return matText(regex, str);
		}
		return false;
	}

	/**
	 * 是否为邮箱
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean isMail(String mail) {
		if (!isEmpty(mail)) {
			String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
			return matText(regex, mail);
		}
		return false;
	}

	/**
	 * 是否为手机号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (!isEmpty(mobile)) {
			String regex = "^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$";
			return matText(regex, mobile);
		}
		return false;
	}

	/**
	 * 是否为数字, 只允许为整形
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNum(String num) {
		if (!isEmpty(num)) {
			String regex = "[0-9]*";
			return matText(regex, num);
		}
		return false;
	}

	/**
	 * 是否为数字 包括整形，小数，并且必须含几位小数 0 表示不含小数
	 * 
	 * @param number
	 * @param diag
	 * @return
	 */
	public static boolean isNum(String num, int diag) {
		int index = num.indexOf(".");
		if (index < 0 || diag == 0) {
			return isNum(num);
		} else {
			String num1 = num.substring(0, index);
			String num2 = num.substring(index + 1);
			if (num2.length() != diag) {
				return false;
			}
			return isNum(num1) && isNum(num2);
		}
	}

	/**
	 * 验证是否为数字，并且该数字取值范围合法，包含小数位
	 * 
	 * @param num
	 * @param min
	 * @param max
	 * @param diag
	 *            小数位 0 表示无小数
	 * @return
	 */
	public static boolean isNum(String num, double min, double max, int diag) {
		if (!isNum(num, diag)) {
			return false;
		}
		if (Double.valueOf(num).doubleValue() < min) {
			return false;
		}
		if (Double.valueOf(num).doubleValue() > max) {
			return false;
		}
		return true;
	}

	/**
	 * 验证传入参数，是否位于集合内
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isInArray(String param, String[] params) {
		if (isEmpty(param)) {
			return false;
		}
		for (String p : params) {
			if (param.equals(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串是否小于传入长度
	 * 
	 * @param num
	 * @param length
	 * @return
	 */
	public static boolean isInLength(String str, int min, int max) {
		if (!isEmpty(str)) {
			if (str.length() > min && str.length() <= max) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否为日期格式 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		if (!isEmpty(date)) {
			String dp1 = "\\d{4}-\\d{2}-\\d{2}";
			String dp2 = "^((\\d{2}(([02468][048])|([13579][26]))"
					+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
					+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
					+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
					+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
					+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

			if (matText(dp1, date)) {
				if (matText(dp2, date)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						sdf.parse(date);
						return true;
					} catch (ParseException e) {
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !ValidateUtil.isEmpty(str);
	}

	/**
	 * 是否为布尔类型字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBoolean(String str) {
		if (!isEmpty(str)) {
			if ("true".equals(str.toLowerCase()) || "false".equals(str.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否存在于枚举类型内
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static boolean isInEnum(String str, Class<?> c) {
		if (!isEmpty(str)) {
			for (Object o : c.getEnumConstants()) {
				if (o.toString().equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean matText(String expression, String text) {
		Pattern p = Pattern.compile(expression); // 正则表达式
		Matcher m = p.matcher(text); // 操作的字符串
		boolean b = m.matches();
		return b;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isInEnumName(String str, Class<?> c) {
		if (!isEmpty(str)) {
			for (Object o : c.getEnumConstants()) {
				if (((Enum) o).name().equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	public static boolean isBankCard(String cardId) {
		char bit = getBankCardCode(cardId.substring(0, cardId.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param cardId
	 * @return
	 */
	public static char getBankCardCode(String cardId) {
		if (cardId == null || cardId.trim().length() == 0 || !cardId.matches("\\d+")) {
			return 'N';
		}
		char[] chs = cardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	/**
	 * 信用卡验证
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isCreditCard(String number) {
		int sumOdd = 0;
		int sumEven = 0;
		int length = number.length();
		int[] wei = new int[length];
		for (int i = 0; i < number.length(); i++) {
			wei[i] = Integer.parseInt(number.substring(length - i - 1, length - i));
		}
		for (int i = 0; i < length / 2; i++) {
			sumOdd += wei[2 * i];
			if ((wei[2 * i + 1] * 2) > 9) {
				wei[2 * i + 1] = wei[2 * i + 1] * 2 - 9;
			} else {
				wei[2 * i + 1] *= 2;
			}
			sumEven += wei[2 * i + 1];
		}
		if ((sumOdd + sumEven) % 10 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为数字 包括整形，小数
	 * 
	 * @param number
	 * @param diag
	 * @return
	 */
	public static boolean isNumber(String num) {
		int index = num.indexOf(".");
		if (index < 0) {
			return isNum(num);
		} else {
			String num1 = num.substring(0, index);
			String num2 = num.substring(index + 1);

			return isNum(num1) && isNum(num2);
		}
	}
}