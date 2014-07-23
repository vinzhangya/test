package com.mitchz.apps.meimei.common.persistent;

import java.util.Iterator;

/**
 * Created by zhangya on 2014/7/16.
 */
public interface IStoreTemplate<T>
{

	/**
	 * 保存实例
	 * @param t 实例
	 * @return 返回保存状态
	 */
	boolean save(T t);

	/**
	 * 批量保存
	 * @param iterator 实例迭代器
	 * @return 保存状态
	 */
	boolean saveBatch(Iterator<T> iterator);
}
