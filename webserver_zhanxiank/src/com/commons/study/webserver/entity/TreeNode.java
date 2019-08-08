package com.commons.study.webserver.entity;

import java.io.File;

public class TreeNode {
	
	String path; //文件目录。
	TreeNode firstChild; //第一个儿子。
	TreeNode nextSibling;//下一兄弟链
	
	public TreeNode(String path, TreeNode firstChild, TreeNode nextSibling) {
		super();
		this.path = path;
		this.firstChild = firstChild;
		this.nextSibling = nextSibling;
	}
			
}


class Tree{
	private TreeNode root=new TreeNode(null, null, null);
	
	public TreeNode getRoot() {
		return root;
	}
	
	
	public void buildFileTree(String path,TreeNode treeNode){
		
		File file=new File(path);
		
		File[] files=file.listFiles();
		
		for(int i=0; i<files.length; i++){	
			TreeNode newNode=new TreeNode(files[i].getPath(), null, null);
			
			if(newNode.firstChild==null){
				
				if(files[i].isFile()){	
					treeNode.firstChild=newNode;
				}else{
					treeNode.firstChild=newNode;
					buildFileTree(files[i].getPath(), newNode);					
				}

			}
			
			else{    //对兄弟的处理。
				
				TreeNode p=treeNode.firstChild;
				
				while (p.nextSibling!=null) {				
					p=p.nextSibling;   //移动指针。					
				}
				
				if(files[i].isFile()){
					p.nextSibling=newNode;
				}else{
					p.nextSibling=newNode;
					buildFileTree(files[i].getPath(), newNode);
				}
					
			}
				
		}
		
		
	}
	
	
	
	
	public void printTree(TreeNode root,int deep)
    {
        if (root.path!=null) //这个if是为了区分root节点，因为root节点data=null
        {
            for (int i = 0; i < deep; i++)//输出前置空格
                System.out.print("  ");
            System.out.println(root.path);
        }
        //画一下图，就能理解这个两个if
        if (root.firstChild!=null) 
            printTree(root.firstChild, deep+1);
        if (root.nextSibling!=null)
            printTree(root.nextSibling, deep);
    }

	
	
	
	
}