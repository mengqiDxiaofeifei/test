package com.zhou.test;


import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBJDBC{
   public static void main( String args[] ){
      try{   
       // 连接到 mongodb 服务
         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
         // 连接到数据库
         MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

          MongoCollection<Document> collection = mongoDatabase.getCollection("stus");

          collection.deleteMany(Filters.eq("genger","女"));

          FindIterable<Document> findIterable = collection.find();
          MongoCursor<Document> mongoCursor = findIterable.iterator();
          while(mongoCursor.hasNext()){
              System.out.println(mongoCursor.next());
          }
      }catch(Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     }
   }
}