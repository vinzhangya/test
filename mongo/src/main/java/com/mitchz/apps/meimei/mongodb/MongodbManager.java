package com.mitchz.apps.meimei.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;

/**
 * Created by zhangya on 2014/7/22.
 */
public class MongodbManager
{
	private String host;
	private int port;
	private static MongoClient mongo;

	/**
	 * 有参构造器
	 * @param host
	 * @param port
	 */
	private MongodbManager(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	/**
	 * 无参构造器
	 */
	private MongodbManager()
	{

	}

	@PostConstruct
	private void init() throws UnknownHostException
	{
		 mongo = new MongoClient(new ServerAddress(host,port),new MongoClientOptions.Builder().build());
	}

	/**
	 * 获取数据库
	 * @param name 数据库名称
	 */
	public DB getDB(String name)
	{
		return mongo.getDB(name);
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public MongoClient getMongo()
	{
		return mongo;
	}
}
