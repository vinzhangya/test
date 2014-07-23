package com.mitchz.apps.meimei.mq;

import com.mitchz.apps.meimei.common.persistent.StoreTemplate;
import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Iterator;

/**
 * Created by zhangya on 2014/7/16.
 */
@Component
@Qualifier("mqStore")
public class ActiveMqStore extends StoreTemplate<MeiMeiPage>
{

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMqStore.class);
	@Autowired
	@Qualifier("jmsTemplate")
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("queueDestination")
	private Queue queueDestination;
	@Autowired
	@Qualifier("topicDestination")
	private Topic topicDestination;

	@Override
	public boolean save(final MeiMeiPage o)
	{
		LOGGER.debug("Saving meimeipage is {}", o);
		try
		{
			jmsTemplate.send(queueDestination, new MessageCreator()
			{

				public Message createMessage(Session session) throws JMSException
				{
					ObjectMessage message = session.createObjectMessage(o);
					return message;
				}
			});
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("Send Mq error .{}", e, e);
			return false;
		}
	}

	@Override
	public boolean saveBatch(Iterator<MeiMeiPage> iterator)
	{
		while (iterator.hasNext())
		{
			MeiMeiPage page = iterator.next();
			LOGGER.debug("Save batch " + page);
			boolean flag = save(page);
			if (!flag)
			{
				return false;
			}
		}
		return true;
	}
}
