package com.commons.study.threedemotest;

import org.junit.Assert;
import org.junit.Test;

import com.commons.study.threedemo.BinaryTree;
import com.commons.study.threedemo.TNode;

/**
 * 这是是测试二叉树的类，二叉树的功能比较多，junit测试写的麻烦，故写一个测试类。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月2日
 */

public class TestBinaryTree {

	@Test
	public void test() {

		BinaryTree bt = new BinaryTree("A");

		TNode t1 = bt.add(bt.getRoot(), "B", true);
		TNode t2 = bt.add(bt.getRoot(), "D", false);
		TNode t3 = bt.add(t1, "G", true);
		TNode t4 = bt.add(t1, "H", false);
		TNode t5 = bt.add(t2, "C", true);
		TNode t6 = bt.add(t2, "F", false);
		Assert.assertEquals("A", bt.treeLevel(bt.getRoot(), 1));
		Assert.assertEquals("BD", bt.treeLevel(bt.getRoot(), 2));
		Assert.assertEquals("GHCF", bt.treeLevel(bt.getRoot(), 3));
	}

	@Test
	public void test1() { //构造第二棵书

		BinaryTree bt = new BinaryTree("A");
		TNode t1 = bt.add(bt.getRoot(), "B", true);
		TNode t2 = bt.add(bt.getRoot(), "C", false);
		TNode t3 = bt.add(t1, "D", true);
		TNode t4 = bt.add(t1, "E", false);
		TNode t5 = bt.add(t2, "F", true);
		TNode t6 = bt.add(t2, "G", false);
		TNode t7 = bt.add(t3, "H", true);
		TNode t8 = bt.add(t3, "J", false);
		Assert.assertEquals("A", bt.treeLevel(bt.getRoot(), 1));
		Assert.assertEquals("BC", bt.treeLevel(bt.getRoot(), 2));
		Assert.assertEquals("DEFG", bt.treeLevel(bt.getRoot(), 3));
		Assert.assertEquals("HJ", bt.treeLevel(bt.getRoot(), 4));

	}
}
