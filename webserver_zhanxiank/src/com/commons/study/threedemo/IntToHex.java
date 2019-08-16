package com.commons.study.threedemo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 整数转成16进制
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月2日
 */

public class IntToHex {
	private final int targ = 16; //需要转换的进制。

	/**
	 *  整数转成16进制
	 * @param num
	 * @return
	 */
	public String int2Hex(int num) {
		if (num < 0) {
			return null; //不处理负数。
		}
		StringBuilder sb = new StringBuilder();
		int temp = num / targ;
		int mark = num % targ;
		sb.append(num2Char(mark));
		while (temp > 0) {
			num = temp;
			mark = num % targ;
			sb.append(num2Char(mark));
			temp = num / targ;
		}

		return sb.reverse().toString();
	}

	/** 数字变成对应的字符串
	* 10进制以上的数在计算机中以 A,B,C,D等表示。这个方法只对应了16进制以下。
	* @param n
	* @return
	*/

	public String num2Char(int n) {
		if (n < 10) {
			return String.valueOf(n);
		}

		return String.valueOf((char) (n + 55));

	}
}
