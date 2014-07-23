package com.mitchz.apps.meimei.mq;

import com.mitchz.apps.meimei.common.persistent.StoreTemplate;
import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import com.mitchz.apps.meimei.parser.ParserInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;

/**
 * Created by zhangya on 2014/7/16.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.mitchz.apps.meimei.mq")
@ImportResource("classpath:applicationContext.xml")
public class MqBootStrap implements CommandLineRunner
{

	@Autowired
	@Qualifier("jmsTemplate")
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("mqStore")
	private StoreTemplate<MeiMeiPage> storeTemplate;

	public static void main(String[] args) throws Exception
	{
		SpringApplication.run(MqBootStrap.class, args).registerShutdownHook();
	}

	@Override
	public void run(String... strings) throws Exception
	{
		String path = "E:\\crawler\\meimei\\20140712092526\\mirror\\";
		File dir = new File(path + "www.mnsfz.com\\h\\");
		for (String fileName : dir.list())
		{
			ParserInvoker invoker = new ParserInvoker(path,
					"\\www.mnsfz.com\\h\\" + fileName);
			storeTemplate.saveBatch(invoker.iterator());
		}
	}
}
