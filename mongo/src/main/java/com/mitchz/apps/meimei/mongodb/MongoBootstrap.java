package com.mitchz.apps.meimei.mongodb;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Date;

/**
 * Created by zhangya on 2014/7/22.
 */
@Configuration("com.mitchz.apps.meimei.mongodb")
@EnableAutoConfiguration
@ImportResource("classpath:applicationContext.xml")
public class MongoBootstrap implements CommandLineRunner
{

	@Autowired
	@Qualifier("mongodbManager")
	private MongodbManager mongodbManager;
	@Override
	public void run(String... strings) throws Exception
	{
		try {


			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongodbManager.getDB("meimei");

			/**** Get collection / table from 'testdb' ****/
			// if collection doesn't exists, MongoDB will create it for you
			DBCollection table = db.getCollection("url");

			/**** Insert ****/
			// create a document to store key and value

			BasicDBObject document = new BasicDBObject();
			document.put("name", "mkyong");
			document.put("age", 30);
			document.put("createdDate", new Date());
			table.insert(document);

			/**** Find and display ****/
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("name", "mkyong");

			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}

			/**** Update ****/
			// search document where name="mkyong" and update it with new values
			BasicDBObject query = new BasicDBObject();
			query.put("name", "mkyong");

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("name", "mkyong-updated");

			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);

			table.update(query, updateObj);

			/**** Find and display ****/
			BasicDBObject searchQuery2
					= new BasicDBObject().append("name", "mkyong-updated");

			DBCursor cursor2 = table.find(searchQuery2);

			while (cursor2.hasNext()) {
				System.out.println(cursor2.next());
			}

			/**** Done ****/
			System.out.println("Done");

		}
		catch (MongoException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		SpringApplication.run(MongoBootstrap.class, args).registerShutdownHook();
	}
}
