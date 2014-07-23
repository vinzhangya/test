package com.mitchz.apps.meimei.parser.Page;

import com.mitchz.apps.meimei.common.parser.Page.Html;
import com.mitchz.apps.meimei.common.parser.Page.IPage;
import com.mitchz.apps.meimei.common.parser.annotation.CrawlFile;
import com.mitchz.apps.meimei.common.parser.annotation.ExceptionType;
import com.mitchz.apps.meimei.common.parser.annotation.Expression;
import com.mitchz.apps.meimei.common.parser.annotation.ExpressionParseType;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangya on 2014/7/14.
 */
@CrawlFile("^[a-zA-Z0-9\\$_]*?[^-\\d]?\\.{1}html{1}$")
public class MeiMeiPage extends Html<MeiMeiPage> implements IPage
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MeiMeiPage.class);
	/**
	 * 标题
	 */
	@Expression(value="#b-show .showHeader .title",parseType= ExpressionParseType.Text)
	private String title;
	/**
	 * 链接地址
	 */
	@Expression(value="#b-show .box_inner script",type = ExceptionType.JSOUP)
	private String url;
	/**
	 * 文件路径
	 */
	private String path;
	/**
	 * url使用MD5编码
	 */
	private String urlMd5;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		LOGGER.debug("Parsed url is {}",url);
		if (null == url || "".equals(url))
		{
			url = "";
			return ;
		}

		url = url.substring(url.indexOf("\"") + 1);
		url = url.substring(0,url.indexOf("\""));
		LOGGER.debug("Formated url is {}",url);
		path = url.replace("/big/", "/pic/");
		path = path.substring(path.indexOf("//") + 1);
		path = path.substring(0, path.lastIndexOf("/") + 1);
		LOGGER.debug("Formated path is {}",path);
		urlMd5 = DigestUtils.md5Hex(url);
		LOGGER.debug("Url Md5 result is {}",urlMd5);
		this.url = url;

	}

	public String getUrlMd5()
	{
		return urlMd5;
	}

	public void setUrlMd5(String urlMd5)
	{
		this.urlMd5 = urlMd5;
	}

	@Override
	public String toString()
	{
		return "MeiMeiPage{" +
				"title='" + title + '\'' +
				", url='" + url + '\'' +
				", path='" + path + '\'' +
				", urlMd5='" + urlMd5 + '\'' +
				'}';
	}
}
