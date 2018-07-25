package com.umpay.channelRouting.center;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightConfDSlist {
	/* 同排名的数据源集合 */
	private List<WeightConfigDS> list;
	/* 权重总和 */
	private int sumWeight;

	private boolean haveElem;

	public boolean isHaveElem() {
		return haveElem;
	}

	public void setHaveElem(boolean haveElem) {
		this.haveElem = haveElem;
	}

	public WeightConfDSlist() {
		list = new ArrayList<WeightConfigDS>();
		sumWeight = 0;
		haveElem = true;
	}

	/**
	 * 增加元素
	 * 
	 * @param t
	 */
	public void add(WeightConfigDS t) {
		list.add(t);
		sumWeight += t.getWeight();
	}

	/**
	 * 获取并删除元素
	 * 
	 * @return
	 */
	public WeightConfigDS removeAndGetFromList() {
		if (!haveElem) {
			return null;
		}
		WeightConfigDS retuElem = getFromList();
		list.remove(retuElem);
		sumWeight -= retuElem.getWeight();
		if (list.size() == 0) {
			haveElem = false;
		}
		return retuElem;
	}

	private WeightConfigDS getFromList() {
		// 权重随机
		int random = new Random().nextInt(sumWeight);
		int sum = 0;
		for (WeightConfigDS t : list) {
			sum += t.getWeight();
			if (random < sum) {
				return t;
			}
		}
		return list.get(0);
	}

	public int getSize() {
		return list.size();
	}

	@Override
	public String toString() {
		return "WeightConfDSlist [list=" + list + ", sumWeight=" + sumWeight + ", haveElem=" + haveElem + "]";
	}
	
	
}
