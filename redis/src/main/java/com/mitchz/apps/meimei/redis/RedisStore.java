package com.mitchz.apps.meimei.redis;

import com.mitchz.apps.meimei.common.persistent.StoreTemplate;
import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 *
 * Created by zhangya on 2014/7/16.
 */
@Component
@Qualifier("mqStore")
public class RedisStore extends StoreTemplate<MeiMeiPage>
{
	private  static final Logger LOGGER = LoggerFactory.getLogger(RedisStore.class);
	@Autowired
	@Qualifier("jmsTemplate")
	private JmsTemplate jmsTemplate;
	@Override
	public boolean save(MeiMeiPage o)
	{
		LOGGER.debug("Saving meimeipage is {}",o);
		try
		{
			jmsTemplate.convertAndSend("test",o);
			return  true;
		}catch (Exception e)
		{
			LOGGER.error("Send Mq error .{}",e,e);
			return false;
		}


	}

	@Override
	public boolean saveBatch(Iterator<MeiMeiPage> iterator)
	{
		while(iterator.hasNext())
		{
			MeiMeiPage page = iterator.next();
			boolean flag = save(page);
			if (!flag)
			{
				return false;
			}
		}
		return true;
	}
}
