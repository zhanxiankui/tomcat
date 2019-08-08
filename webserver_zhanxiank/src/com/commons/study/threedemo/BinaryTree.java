package com.commons.study.threedemo;

import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.Test;

/**
 *  二叉树类。定义了二叉树的数据结构，以及二叉树的构造，取第n层结点等操作
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxk
 * @createdate 2019年8月1日
 */
class TNode {
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

public class BinaryTree {
	private TNode root;

	/**
	 * 构造空的二叉树。
	 */
	public BinaryTree() {
		this.root = new TNode();
	}

	public BinaryTree(String value) {
		this.root = new TNode(value);
	}

	/**
	 *  添加结点
	 * @param parent
	 * @param value
	 * @param isleft
	 * @return
	 */

	public TNode add(TNode parent, String value, boolean isleft) {

		if (parent == null || parent.value == null) {
			throw new RuntimeException("结点为空，不能添加");
		}
		TNode tNode = new TNode(value);
		if (isleft) {

			parent.left = tNode;
		}
		else {

			parent.right = tNode;
		}

		return tNode;
	}

	public String getValue(TNode node) {

		return node.value;
	}

	public boolean isEmpty() {
		return root.value == null;
	}

	public TNode getRoot() {

		return root;
	}

	public String treeLevel(TNode root, int level) {
		if (root == null) {
			throw new RuntimeException("树为空");
		}

		int count = 1;
		StringBuilder sb = new StringBuilder();
		Queue<TNode> queue = new ArrayDeque<TNode>();
		TNode temp;
		queue.add(root);
		int leavenum = 0;

		while (!queue.isEmpty()) {

			leavenum = queue.size(); //上一层结点个数
			while (leavenum > 0) {
				leavenum--;

				temp = queue.remove();

				if (count > level - 1) {
					if (count > level) {
						break;
					}
					sb.append(getValue(temp));
				}

				if (temp.left != null) {
					queue.add(temp.left);

				}

				if (temp.right != null) {
					queue.add(temp.right);

				}

			}

			count++;
		}

		return sb.toString();

	}

}
