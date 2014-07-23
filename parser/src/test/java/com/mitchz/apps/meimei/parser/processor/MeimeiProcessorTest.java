package com.mitchz.apps.meimei.parser.processor;

import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MeimeiProcessorTest
{
	private MeimeiProcessor meimeiProcessor;
	@Before
	public void setUp() throws Exception
	{
		File dir = new File(getClass().getResource("").getPath());
		String dirName = dir.getName();
		String  homePath = dir.getParentFile().getAbsolutePath();
		meimeiProcessor = new MeimeiProcessor(homePath,dirName);
	}

	@After
	public void tearDown() throws Exception
	{
		System.clearProperty("website.home.path");
	}

	@Test
	public void testProcess() throws Exception
	{
		List<MeiMeiPage> meiMeiPages =  meimeiProcessor.process();
		assertEquals(meiMeiPages.size(), 1);
		MeiMeiPage page = meiMeiPages.get(0);
		assertEquals(page.getTitle(),"Nono酱迷人妖精");
		assertEquals(page.getUrl(),"http://mhimg1.mnsfz.com/big/meihuo/2014-1-6/1/1.jpg");
		assertEquals(page.getPath(),"/mhimg1.mnsfz.com/pic/meihuo/2014-1-6/1/");
		assertEquals(page.getUrlMd5(), "6788fd6d47a5f3d95e9b9875b1facff3");
	}
}