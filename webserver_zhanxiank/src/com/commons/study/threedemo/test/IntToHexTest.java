package com.commons.study.threedemo.test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.commons.study.threedemo.IntToHex;

public class IntToHexTest {

	@Test
	public void test() {

		IntToHex intToHex = new IntToHex();
		System.out.println("=====" + intToHex.int2Hex(64));  //正整数测试
		Assert.assertNotEquals("401", intToHex.int2Hex(64));
		Assert.assertNotEquals("40", intToHex.int2Hex(32));
		
		Assert.assertEquals("-6的16进制测试", "-6", intToHex.int2Hex(-6));
		Assert.assertEquals("-216的16进制测试", "-D8", intToHex.int2Hex(-216));
	}
	

	@Test
	public void test2() {
		IntToHex intToHex = new IntToHex();
		System.out.println("=====" + intToHex.int2Hex(163));
		Assert.assertEquals("A3", intToHex.int2Hex(163));
		
	    String	output = intToHex.int2Hex(Integer.MAX_VALUE);		
		Assert.assertEquals("7FFFFFFF", output);   //边界测试
	}

			
}
