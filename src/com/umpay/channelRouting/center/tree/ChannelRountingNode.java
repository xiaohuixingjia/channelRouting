package com.umpay.channelRouting.center.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umpay.channelRouting.bean.SortDsBean;
import com.umpay.channelRouting.po.ChannelRoutingPO;
import com.umpay.channelRouting.util.ClassUtil;

/**
 * 通道路由树节点 （构造函数以私有化，调用createRootNode可以创造根节点）
 * 
 * @author xuxiaojia
 */
public class ChannelRountingNode extends BaseNode<ChannelRountingNode, SortDsBean> {

	/**
	 * 节点生成顺序
	 */
	public static final String[] CREATE_NODES_REGULAR = { "productSuitesCode", "merCode", "operatorType" };
	/**
	 * 默认的商户编码分支节点名称
	 */
	public static final String DEFALUT_BRANCH_NAME_4_MER_CODE = "*";
	/**
	 * 默认的运营商类型分支节点名称
	 */
	public static final String DEFALUT_BRANCH_NAME_4_OPERATOR_TYPE = "0";
	/**
	 * 默认的分支名称 map结合
	 */
	public static final Map<String, String> DEFAULT_BRANCH_NAME_MAP = new HashMap<String, String>();

	static {
		DEFAULT_BRANCH_NAME_MAP.put("merCode", DEFALUT_BRANCH_NAME_4_MER_CODE);
	}

	/**
	 * @param deep
	 *            -- 该节点的深度
	 */
	private ChannelRountingNode(int deep) {
		super(deep, new HashMap<String, ChannelRountingNode>(), new ArrayList<SortDsBean>(), BaseNode.IS_NOT_LAST);
	}

	@Override
	protected ChannelRountingNode createNextNode(BaseNode<ChannelRountingNode, SortDsBean> root) {
		return new ChannelRountingNode(root.getDeep() + 1);
	}

	/**
	 * 生成根节点对象
	 * 
	 * @return
	 */
	public static ChannelRountingNode createRootNode() {
		return new ChannelRountingNode(BaseNode.ROOT_DEEP);
	}

	/**
	 * 生成树
	 * 
	 * @param chrList
	 *            -- 通道路由信息集合
	 * @param rootNode
	 *            -- 根节点
	 */
	public void createTree(List<ChannelRoutingPO> chrList) {
		for (ChannelRoutingPO channelRoutingPO : chrList) {
			createNodeBranch2rootNode(channelRoutingPO, CREATE_NODES_REGULAR, this);
		}
	}

	/**
	 * 递归生成叶子分支
	 * 
	 * @param channelRoutingPO
	 * @param createNodesRegular
	 * @param rootNode
	 */
	private void createNodeBranch2rootNode(ChannelRoutingPO channelRoutingPO, String[] createNodesRegular,
			ChannelRountingNode rootNode) {
		// 字段名
		String fieldName = createNodesRegular[rootNode.getDeep() - 1];
		// 获取分支名称
		String branchName = getBranchName(channelRoutingPO, fieldName).trim();
		// 获取分支节点
		ChannelRountingNode branchNode = rootNode.getBranchNode(branchName);
		// 如果是末尾分支则放入叶子并停止递归
		if (isLastNode(createNodesRegular, rootNode)) {
			createLeafAndSetIt(channelRoutingPO, branchNode);
		} else {
			// 递归创建新的分支
			createNodeBranch2rootNode(channelRoutingPO, createNodesRegular, branchNode);
		}
	}

	/**
	 * 根据字段名从通道路由信息对象中获取分支名称
	 * 
	 * @param channelRoutingPO
	 *            -- 通道路由信息对象
	 * @param fieldName
	 *            -- 字段名
	 * @return
	 */
	private String getBranchName(ChannelRoutingPO channelRoutingPO, String fieldName) {
		// 获取分支名称
		Object fieldValue = ClassUtil.getFieldValue(fieldName, channelRoutingPO);
		String branchName = fieldValue != null ? fieldValue.toString() : DEFALUT_BRANCH_NAME_4_MER_CODE;
		return branchName;
	}

	/**
	 * 根据通道信息对象获取符合该通道信息的叶子集合
	 * 
	 * @param channelRouting
	 *            --通道信息对象
	 * @return
	 */
	public List<SortDsBean> getLeafListBychr(ChannelRoutingPO channelRouting) {
		return getLeafListBychr(channelRouting, CREATE_NODES_REGULAR, this);
	}

	/**
	 * 根据通道信息对象获取符合该通道信息的叶子集合
	 * 
	 * @param channelRouting
	 *            --通道信息对象
	 * @return
	 */
	public List<SortDsBean> getLeafListBychr(ChannelRoutingPO channelRouting, String[] createNodesRegular,
			ChannelRountingNode rootNode) {
		// 字段名
		String fieldName = createNodesRegular[rootNode.getDeep() - 1];
		// 获取分支名称
		String branchName = getBranchName(channelRouting, fieldName);
		// 如果该分支名称不存在与树中，则用默认的分支名称
		if (rootNode.notContainsBranch(branchName)) {
			branchName = DEFAULT_BRANCH_NAME_MAP.get(fieldName);
		}
		// 获取分支节点
		ChannelRountingNode branchNode = rootNode.getBranchNode(branchName);
		// 如果是末尾分则取出其中的叶子集合并返回
		if (isLastNode(createNodesRegular, rootNode)) {
			return branchNode.getLeafList();
		} else {
			// 在下一个分支中查找
			return getLeafListBychr(channelRouting, createNodesRegular, branchNode);
		}
	}

	/**
	 * 判断当前节点是否需要生成叶子节点
	 * 
	 * @param createNodesRegular
	 * @param baseNode
	 * @return
	 */
	public boolean isLastNode(String[] createNodesRegular, ChannelRountingNode baseNode) {
		return baseNode.getDeep() == createNodesRegular.length;
	}

	/**
	 * 生成叶子
	 * 
	 * @param channelRoutingPO
	 *            -- 通道路由信息
	 * @param branchName
	 *            -- 分支名称
	 * @param branchNode
	 *            -- 分支节点
	 */
	private void createLeafAndSetIt(ChannelRoutingPO channelRoutingPO, ChannelRountingNode branchNode) {
		// 该节点为末尾节点，没有枝叶
		branchNode.setLast(BaseNode.IS_LAST);
		SortDsBean sortDsBean = new SortDsBean(channelRoutingPO.getSort(), channelRoutingPO.getDsId().toString(),
				channelRoutingPO.getWeight(), ChannelRoutingPO.CHANNEL_CAN_USE_NEXT == channelRoutingPO.getUseNext());
		branchNode.setLeft2list(sortDsBean);
	}

	public static void main(String[] args) {
		ChannelRoutingPO po1 = new ChannelRoutingPO();
		ChannelRoutingPO po2 = new ChannelRoutingPO();
		ChannelRoutingPO po3 = new ChannelRoutingPO();
		ChannelRoutingPO po4 = new ChannelRoutingPO();
		po1.setDsId("1");
		po1.setProductSuitesCode("chanR_001");
		po1.setOperatorType(1);
		po1.setSort(5);
		po2.setDsId("2");
		po2.setProductSuitesCode("chanR_001");
		po2.setOperatorType(2);
		po2.setSort(4);
		po3.setDsId("3");
		po3.setProductSuitesCode("chanR_001");
		po3.setOperatorType(3);
		po3.setSort(3);
		po4.setDsId("2");
		po4.setProductSuitesCode("chanR_001");
		po4.setOperatorType(1);
		po4.setSort(2);
		List<ChannelRoutingPO> list = new ArrayList<ChannelRoutingPO>();
		list.add(po1);
		list.add(po2);
		list.add(po3);
		list.add(po4);
		ChannelRountingNode node = new ChannelRountingNode(1);
		node.createTree(list);
		List<SortDsBean> leafListBychr = node.getLeafListBychr(po1);
		System.out.println(leafListBychr);
	}
}
