package com.commons.study.threedemo;

import java.util.ArrayDeque;
import java.util.Queue;



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
				if (count == level ) {
					sb.append(getValue(temp));
				}
				if(count>level){
					break;
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
