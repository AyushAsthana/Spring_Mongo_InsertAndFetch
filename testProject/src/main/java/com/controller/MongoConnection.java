package com.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

	public static MongoClient mongoClient = new MongoClient("localhost",27017);

     // Access database named 'test'
    public static MongoDatabase database = mongoClient.getDatabase("myNewDatabase");
	
}
