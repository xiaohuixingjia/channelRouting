package com.umpay.channelRouting.center.tree;

import java.util.List;
import java.util.Map;

/**
 * 底层节点抽象类
 * 
 * T--分支节点对象 (必须继承baseNode)
 * 
 * E--叶子节点对象
 * 
 * @author xuxiaojia
 */
@SuppressWarnings("rawtypes")
public abstract class BaseNode<T extends BaseNode, E> {
	/* 深度 */
	private int deep;
	/* 节点分支 */
	private Map<String, T> nodeBranchMap;
	/* 叶子 */
	private List<E> leafList;
	/* 当前节点是否拥有孩子 */
	private boolean isLast;
	/**
	 * 是末尾节点
	 */
	public static final boolean IS_LAST = true;
	/**
	 * 不是末尾节点
	 */
	public static final boolean IS_NOT_LAST = false;
	/**
	 * 根节点深度值
	 */
	public static final int ROOT_DEEP = 1;

	public BaseNode(int deep, Map<String, T> nodeBranchMap, List<E> leafList, boolean isLast) {
		super();
		this.deep = deep;
		this.nodeBranchMap = nodeBranchMap;
		this.leafList = leafList;
		this.isLast = isLast;
	}

	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	/**
	 * 获取叶子节点集合
	 * 
	 * @return
	 */
	protected List<E> getLeafList() {
		return leafList;
	}

	/**
	 * 将叶子放入叶子节点集合
	 * 
	 * @param e
	 */
	public void setLeft2list(E e) {
		leafList.add(e);
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	/**
	 * 生成root分支节点的下一层节点
	 * 
	 * @param root
	 *            --
	 * @return
	 */
	protected abstract T createNextNode(BaseNode<T, E> root);

	/**
	 * 判断当前节点是否包含该分支
	 * 
	 * @param branchName
	 * @return
	 */
	public boolean containsBranch(String branchName) {
		return this.nodeBranchMap.containsKey(branchName);
	}

	/**
	 * 判断当前节点是否没有改分支节点
	 * 
	 * @param branchName
	 * @return
	 */
	public boolean notContainsBranch(String branchName) {
		return !containsBranch(branchName);
	}

	/**
	 * 获取分支节点
	 * 
	 * @param branchName
	 * @return
	 */
	public T getBranchNode(String branchName) {
		// 如果不包含该分支则创建一个分支
		if (notContainsBranch(branchName)) {
			T t = createNextNode(this);
			nodeBranchMap.put(branchName, t);
		}
		return nodeBranchMap.get(branchName);
	}

}
