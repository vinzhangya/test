package com.mitchz.apps.meimei.parser;

import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import com.mitchz.apps.meimei.parser.processor.MeimeiProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * Created by zhangya on 2014/7/16.
 */
public class ParserInvoker implements Iterable<MeiMeiPage>
{
	private MeimeiProcessor meimeiProcessor;
	/**
	 *
	 * @param path
	 * @param relativeHtmlPath
	 */
	public ParserInvoker(String path, String relativeHtmlPath)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException
	{
		meimeiProcessor =  new MeimeiProcessor(path,relativeHtmlPath);
	}

	public ParserInvoker(String relativeHtmlPath)
			throws IllegalAccessException, InvocationTargetException,
			InstantiationException
	{
		this(System.getProperty("website.home.path"),relativeHtmlPath);
	}

	@Override
	public Iterator<MeiMeiPage> iterator()
	{
		return meimeiProcessor.iterator();
	}
}
