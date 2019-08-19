package com.commons.study.threedemotest;

import org.junit.Assert;
import org.junit.Test;

import com.commons.study.threedemo.IntToHex;

/**
 * 
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月13日
 */
public class IntToHexTest {

	@Test
	public void test() {

		IntToHex intToHex = new IntToHex();
		System.out.println("=====" + intToHex.int2Hex(64));  //正整数测试
		Assert.assertEquals("40", intToHex.int2Hex(64));
		System.out.println("=====" + intToHex.int2Hex(163));
		Assert.assertEquals("A3", intToHex.int2Hex(163));
	    String	output = intToHex.int2Hex(Integer.MAX_VALUE);		
		Assert.assertEquals("7FFFFFFF", output);   //边界测试

	}
	
		
}
