package com.commons.study.threedemo;

/**
 *  二叉树类。定义了二叉树的数据结构，以及二叉树的构造，取第n层结点等操作
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxk
 * @createdate 2019年8月1日
 */
 public class TNode {
	String value;

	TNode left, right;
	public TNode() {

	}

	public TNode(String value) {
		this.value = value;
		left = null;
		right = null;
	}

	public TNode(String value, TNode lt, TNode rt) {
		this.value = value;
		left = lt;
		right = rt;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TNode getLeft() {
		return left;
	}

	public void setLeft(TNode left) {
		this.left = left;
	}

	public TNode getRight() {
		return right;
	}

	public void setRight(TNode right) {
		this.right = right;
	}

}