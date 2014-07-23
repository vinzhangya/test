package com.mitchz.apps.meimei.redis;

import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import com.mitchz.apps.meimei.parser.ParserInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;
import java.util.Iterator;

/**
 * Created by zhangya on 2014/7/16.
 */
@Configuration("applicationContext.xml")
@EnableAutoConfiguration
@ComponentScan
//@ComponentScan("com.mitchz.apps.meimei.parser")
public class RedisBootStrap implements CommandLineRunner
{
	@Autowired
	@Qualifier("jmsTemplate")
	private JmsTemplate jmsTemplate;
	@Override
	public void run(String... strings) throws Exception
	{

		String path = "E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror\\";
		File dir = new File(path+"www.mnsfz.com\\h\\");
		for (String fileName :dir.list())
		{
			ParserInvoker invoker = new ParserInvoker(path,"\\www.mnsfz.com\\h\\"+fileName);
			Iterator<MeiMeiPage> iterator = invoker.iterator();
			while (iterator.hasNext())
			{
				MeiMeiPage meimeiPage = iterator.next();
				jmsTemplate.convertAndSend("meimei",meimeiPage);
			}
		}

	}

	public static void main(String[] args) throws Exception
	{
		SpringApplication.run(RedisBootStrap.class, args).registerShutdownHook();
	}

}
