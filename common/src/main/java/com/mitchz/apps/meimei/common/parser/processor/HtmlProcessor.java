package com.mitchz.apps.meimei.common.parser.processor;

import com.google.common.collect.Lists;
import com.mitchz.apps.meimei.common.parser.Page.Html;
import com.mitchz.apps.meimei.common.parser.annotation.Expression;
import com.mitchz.apps.meimei.common.parser.annotation.ExpressionParseType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * HOME_PATH_NAME 使用 System.getProperty() 获取启动参数中的目录名
 * Created by zhangya on 2014/7/15.
 */
public abstract class HtmlProcessor<T extends Html> implements Processor
{
	private static final Logger LOGGER = LoggerFactory.getLogger(
			HtmlProcessor.class);
	private Html<T> html;
	private String relativeHtmlPath;
	private String path;

	/**
	 * 初始化函数
	 *
	 * @param html     html文件实例
	 * @param path 工程的主目录
	 * @param relativeHtmlPath html文件相对路径
	 */
	protected HtmlProcessor(Html<T> html,String path , String relativeHtmlPath) throws IllegalAccessException,
			InstantiationException, InvocationTargetException
	{
		LOGGER.debug("html instance class [{}],path: {},relativeHtmlPath : {}",
				new Object[] { html.getClass(), path, relativeHtmlPath });
		this.html = html;
		this.path = path;
		this.relativeHtmlPath = relativeHtmlPath;
	}

	/**
	 * 执行Html解析
	 *
	 * @return 解析完成的对象实例
	 */
	public List<T> process() throws InstantiationException, IllegalAccessException,
			InvocationTargetException, IOException
	{
		List<T> list = new ArrayList<T>();
		Iterator<T> iterator = this.iterator();
		while(iterator.hasNext())
		{
			T t = iterator.next();
			list.add(t);
		}
		return list;
	}

	/**
	 * 获取目录下的文件
	 *
	 * @return 文件列表
	 */
	protected List<File> iteratorFiles()
	{
		FilenameFilter filenameFilter = new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				LOGGER.debug("File name filter regex:{},file name is {}",html.getCrawlFile().value(),name);
				Pattern pattern = Pattern
						.compile(html.getCrawlFile().value());
				boolean match = pattern.matcher(name).matches();
				LOGGER.debug("File match status : {}",match);
				return match;
			}
		};
		File dir = new File( path + File.separator + relativeHtmlPath);
		LOGGER.debug("Dir path : {}",path);
		File[] files = dir.listFiles(filenameFilter);
		if (null == files || files.length <= 0)
			return Lists.newArrayList();
		return Lists.newArrayList(files);
	}

	/**
	 * 对文件进行解析，并赋值到相应的域变量内
	 *
	 * @param file     文件
	 * @param fields    需要赋值的字段
	 */
	protected T parse(File file, HashSet<Field> fields)
			throws InstantiationException, IllegalAccessException, IOException,
			InvocationTargetException
	{
		T instance = html.newChildInstance();
		Document doc = Jsoup.parse(file, "utf-8");
		for (Field field : fields)
		{
			Method writeMethod = BeanUtils.getPropertyDescriptor(instance.getClass(), field.getName()).getWriteMethod();
			Elements elements  = doc.select(field.getAnnotation(Expression.class).value());
			String parseValue = field.getAnnotation(Expression.class).parseType().equals(
					ExpressionParseType.Text) ? elements.text():elements.html();
			writeMethod.invoke(instance, parseValue);
		}
		LOGGER.debug("Parsed instance is [{}]",instance);
		return instance;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Itr(iteratorFiles().iterator());
	}

	/**
	 * 迭代器
	 */
	private class Itr implements Iterator<T>
	{
		private Iterator<File> iterator;
		public Itr(Iterator<File> iterator)
		{
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		@Override
		public T next()
		{
			File file = iterator.next();
			try
			{
				return parse(file,html.getFields());
			}
			catch (Exception e)
			{
				LOGGER.error("Parsing instance error.{}",e,e);
			}
			return null;
		}

		@Override
		public void remove()
		{
			iterator.remove();
		}
	}
}
