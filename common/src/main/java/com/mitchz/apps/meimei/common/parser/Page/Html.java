package com.mitchz.apps.meimei.common.parser.Page;

import com.mitchz.apps.meimei.common.parser.annotation.CrawlFile;
import com.mitchz.apps.meimei.common.parser.annotation.Expression;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.WeakHashMap;

/**
 * html解析模板类
 * Created by zhangya on 2014/7/15.
 */
public abstract class Html<T extends Html> implements IPage
{

	/**
	 * 缓存crawlFile注解
	 */
	private transient static final WeakHashMap<Class, CrawlFile> CRAWL_FILES;

	/**
	 * 缓存Expression注解标注的Field
	 */
	private transient static final WeakHashMap<Class, HashSet<Field>> FIELDS;

	static
	{
		CRAWL_FILES = new WeakHashMap<Class, CrawlFile>();
		FIELDS = new WeakHashMap<Class, HashSet<Field>>();
	}

	/**
	 * 实现类
	 */
	private Class<T> clazz;

	/**
	 *构造函数，初始化实现类实例annotation标记的域名称
	 * @throws UnsupportedOperationException 在没有设置泛型的时候抛出该异常
	 */
	public Html() throws UnsupportedOperationException
	{
		synchronized (CRAWL_FILES)
		{
			clazz = getExtendsClass();
			if (null == clazz || 0 == clazz.getAnnotations().length)
				throw new UnsupportedOperationException(
						clazz.getName() + " not contains annotation [CrawlFile]");
			if (!CRAWL_FILES.containsKey(clazz))
			{
				CRAWL_FILES.put(clazz, clazz.getAnnotation(CrawlFile.class));
			}
			if (!FIELDS.containsKey(clazz))
			{
				HashSet<Field> fields = new HashSet<Field>();
				for (Field field : clazz.getDeclaredFields())
				{
					if (null != field.getAnnotation(Expression.class))
						fields.add(field);
				}
				if (null == clazz || 0 == clazz.getAnnotations().length)
					throw new UnsupportedOperationException(
							clazz.getName() + " no field contains annotation [Expression]");
				FIELDS.put(clazz, fields);
			}
		}
	}

	/**
	 * 获取注解中的类注解CrawlFile
	 * @see com.mitchz.apps.meimei.common.parser.annotation.CrawlFile
	 * @return
	 */
	public CrawlFile  getCrawlFile()
	{
		return CRAWL_FILES.get(clazz);
	}

	/**
	 * 获取注解中的申明属性
	 * @see com.mitchz.apps.meimei.common.parser.annotation.Expression
	 * @return
	 */
	public HashSet<Field> getFields()
	{
		return FIELDS.get(clazz);
	}

	/**
	 * 获取泛型子类的实体类
	 * @return
	 */
	private Class<T> getExtendsClass()
	{
		/**
		 * 获取泛型子类的实体类
		 */
		ParameterizedType type = getClass().getGenericSuperclass() instanceof ParameterizedType ? (ParameterizedType) getClass()
				.getGenericSuperclass() : null;
		Assert.notNull(type);
		if(null == type)
		{
			return null;
		}
		Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
		Assert.notNull(clazz);
		return clazz;
	}

	/**
	 * 初始化 子类实例
	 * @return 子类实例
	 */
	public T newChildInstance() throws IllegalAccessException, InstantiationException
	{
		Class<T> t = getExtendsClass();
		if(null == t)
		{
			return null;
		}
		return getExtendsClass().newInstance();
	}
}
