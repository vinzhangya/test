package com.mitchz.apps.meimei.parser.processor;

import com.mitchz.apps.meimei.common.parser.processor.HtmlProcessor;
import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhangya on 2014/7/15.
 */
public class MeimeiProcessor extends HtmlProcessor<MeiMeiPage>
{
	/**
	 * 初始化函数
	 *
	 * @param htmlPath html文件Path
	 */
	public MeimeiProcessor(String path,
			String htmlPath) throws IllegalAccessException, InvocationTargetException,
			InstantiationException
	{
		super(new MeiMeiPage(),path, htmlPath);
	}
}
