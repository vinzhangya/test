package com.mitchz.apps.meimei.parser;

import com.mitchz.apps.meimei.parser.processor.MeimeiProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by zhangya on 2014/7/16.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
//@ComponentScan("com.mitchz.apps.meimei.parser")
public class BootStrap implements CommandLineRunner
{
	@Override
	public void run(String... strings) throws Exception
	{
		File dir = new File("E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror\\www.mnsfz.com\\h\\");
		for (String fileName :dir.list())
		{
			MeimeiProcessor meimeiProcessor = new MeimeiProcessor(dir.getPath(),"\\www.mnsfz.com\\h\\"+fileName);
			System.out.println(meimeiProcessor.process());
		}

}

	public static void main(String[] args) throws Exception
	{
		System.setProperty("website.home.path", "E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror\\");
		SpringApplication.run(BootStrap.class, args).registerShutdownHook();
	}

}
