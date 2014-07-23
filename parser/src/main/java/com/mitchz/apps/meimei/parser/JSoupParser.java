package com.mitchz.apps.meimei.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by zhangya on 2014/7/14.
 */
public class JSoupParser
{

	public static void main(String[] args) throws IOException
	{
		Document doc = Jsoup.parse(new File(
						"E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror\\www.mnsfz.com\\h\\meihuo\\PHPddHmPbiJPaiJiC.html"),
				"utf-8");
		System.out.println(
				doc.select("#b-show .box_inner script").html());
		String temp = doc.select("#b-show .box_inner script").html();
		String url = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
		url = url.replace("/big/", "/pic/");
		url = url.substring(url.indexOf("//") + 1);
		url = url.substring(0, url.lastIndexOf("/") + 1);
		System.out
				.println(url);
		String title = doc.select("#b-show .showHeader .title").text();
		System.out.println(title);
		String path = "E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror\\";
		System.out.println(path + url);
		File dir = new File(path + url);
		for (File file : dir.listFiles())
		{
			System.out.println(file.getName());
		}
		File dirHtml = new File(
				"E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror\\www.mnsfz.com\\h\\meihuo");
		FilenameFilter filenameFilter = new FilenameFilter()
		{

			@Override
			public boolean accept(File dir, String name)
			{
				Pattern pattern = Pattern
						.compile("^[a-zA-Z0-9\\$_]*?[^-\\d]?\\.{1}html{1}$");
				System.out.println(name);
				System.out.println(pattern.matcher(name).matches());
				return pattern.matcher(name).matches();
			}
		};
		for (File file : dirHtml.listFiles(filenameFilter))
		{
			System.out.println(file.getName());
		}
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\$_]*?[^-\\d]?\\.{1}html{1}$");
		System.out.println(pattern.matcher("09321$123511-1.html").matches());
	}
}
