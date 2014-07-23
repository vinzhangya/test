package com.mitchz.apps.meimei.parser.Page;

 import com.mitchz.apps.meimei.common.parser.annotation.ExceptionType;
 import com.mitchz.apps.meimei.common.parser.annotation.Expression;
 import org.junit.Before;
import org.junit.Test;

 import java.lang.reflect.Field;

 import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MeiMeiPageTest
{
	MeiMeiPage meiMeiPage;
	@Before
	public void setUp() throws Exception
	{
		meiMeiPage = new MeiMeiPage();
	}

	@Test
	public void testGetCrawlFile() throws Exception
	{
		assertNotNull(meiMeiPage.getCrawlFile());
		assertEquals("^[a-zA-Z0-9\\$_]*?[^-\\d]?\\.{1}html{1}$",meiMeiPage.getCrawlFile().value());
	}

	@Test
	public void testGetFields() throws Exception
	{
		assertNotNull(meiMeiPage.getFields());
		assertEquals(meiMeiPage.getFields().size(), 2);
		Field title = meiMeiPage.getFields().toArray(new Field[meiMeiPage.getFields().size()])[0];
		assertEquals(title.getName(),"title");
		assertEquals(title.getAnnotation(
				Expression.class).value(),"#b-show .showHeader .title");
		assertEquals(title.getAnnotation(
				Expression.class).type(), ExceptionType.JSOUP);
	}
}