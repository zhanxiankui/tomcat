package com.commons.study.threedemo;

import org.junit.Assert;
import org.junit.Test;

public class IntToHexTest {

	@Test
	public void test() {

		IntToHex intToHex = new IntToHex();
		System.out.println("=====" + intToHex.int2Hex(64));
		Assert.assertEquals("401", intToHex.int2Hex(64));
	}

	@Test
	public void test2() {

		IntToHex intToHex = new IntToHex();
		System.out.println("=====" + intToHex.int2Hex(163));
		Assert.assertEquals("A3", intToHex.int2Hex(163));

	}

	
	
	
	@Test
	public void parse() {
		
		
	      System.out.println("----"+( (int) 'A'));
	      System.out.println("----"+( (int) 'B'));
	      System.out.println("----"+( (int) 'c'));
	      
	      for(int i=10; i<20; i++)
	      System.out.println("----"+ (char) ( i+55));
			
		}
		
		
		
		
}
