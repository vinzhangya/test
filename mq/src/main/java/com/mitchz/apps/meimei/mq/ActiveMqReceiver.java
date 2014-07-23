package com.mitchz.apps.meimei.mq;

import com.mitchz.apps.meimei.mongodb.MongodbManager;
import com.mitchz.apps.meimei.parser.Page.MeiMeiPage;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.io.File;
import java.util.Arrays;

/**
 * Created by zhangya on 2014/7/21.
 */
@Component
@Qualifier("activeMqReceive")
public class ActiveMqReceiver
{

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMqReceiver.class);
	@Autowired
	@Qualifier("jmsTemplate")
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("queueDestination")
	private Queue queueDestination;
	@Autowired
	@Qualifier("topicDestination")
	private Topic topicDestination;
	@Autowired
	@Qualifier("mongodbManager")
	private MongodbManager mongodbManager;

	@PostConstruct
	public void receive()
	{
		LOGGER.debug("Receiving ");
		for (int i = 0; i < 10; i++)
		{
			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while (true)
					{
						Message message = jmsTemplate.receive(queueDestination);
						if (message != null)
						{
							try
							{
								LOGGER.debug(
										"Receive message " + ((ObjectMessage) message)
												.getObject());
								MeiMeiPage meiMeiPage = (MeiMeiPage) ((ObjectMessage) message)
										.getObject();
								if (StringUtils.isBlank(meiMeiPage.getUrl()))
								{
									continue;
								}
								DB db = mongodbManager.getDB("meimei");
								DBCollection table = db.getCollection("page");
								File dir = new File(
										"E:\\crawler\\heritrix_test\\jobs\\meimei\\20140711183517\\mirror"
												+ meiMeiPage.getPath());
								if (null == dir || !dir.isDirectory() || null == dir
										.list() || dir.list().length == 0)
								{
									continue;
								}
								BasicDBObjectBuilder pageM = BasicDBObjectBuilder.start()
										.push(
												meiMeiPage.getUrlMd5())
										.append("title", meiMeiPage
												.getTitle())
										.append("url", meiMeiPage.getUrl()).append(
												"path", meiMeiPage.getPath())
										.append("childs",
												Arrays.asList(dir.list()));
								table.insert(pageM.get());
							}
							catch (JMSException e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							break;
						}
					}
				}
			});
			thread.start();
		}
	}
}
