import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了 注意Mongo已经实现了连接池，并且是线程安全的。 设计为单例模式， 因
 * MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可， Mongo有个内置的连接池（2.10.0版以后 默认为100个）
 *
 * @Author: Liudongyang
 * @Description:
 * @Date: Created in 13:50 2018/7/11
 * @Modified By:
 * @Version: 1.0.0
 */
public enum MongoDBUtils {

  /**
   * 定义一个枚举的元素，它代表此类的一个实例
   */
  INSTANCE;

  private static String userName;
  private static char[] pwd;
  private static String dbName;
  private static String replicaSet;

  private MongoClient mongoClient;

  private MongoDBUtils() {
    System.out.println("执行构造函数！");
  }

  static {
    System.out.println("===============MongoDBUtil初始化========================");
    userName = "ldyldy";
    char[] chars = "class1013".toCharArray();
    pwd = chars;
    dbName = "test";
    replicaSet = "localhost:10080";
    List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
    List<ServerAddress> seeds = replicaSet(replicaSet);

    MongoCredential mc = MongoCredential.createCredential(userName, dbName, pwd);
    credentialsList.add(mc);

    Builder builder = new MongoClientOptions.Builder();
    builder.connectionsPerHost(300);// 连接池设置为300个连接,默认为100  与目标数据库能够建立的最大connection数量
    builder.connectTimeout(15000);// 连接超时，推荐>3000毫秒
    builder.maxWaitTime(5000); //最大等待连接的线程阻塞时间
    builder.socketTimeout(0);// 套接字超时时间，0无限制
    builder.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。

    MongoClientOptions options = builder.build();
    INSTANCE.mongoClient = new MongoClient(seeds, credentialsList, options);
//    INSTANCE.mongoClient = new MongoClient("localhost",10080);
  }

  /**
   * 副本集
   */
  public static List<ServerAddress> replicaSet(String serverAddress) {
    List<ServerAddress> setList = new ArrayList<ServerAddress>();
    String[] serverAddres = serverAddress.split(",");
    for (int i = 0; i < serverAddres.length; i++) {
      String[] server = serverAddres[i].split(":");
      setList.add(new ServerAddress(server[0], Integer.parseInt(server[1])));
    }

    return setList;
  }

  /**
   * 获取DB实例
   */
  public MongoDatabase getDB() {
    MongoDatabase database = mongoClient.getDatabase(dbName);
    return database;
  }

  /**
   * 获取collection对象 - 指定Collection
   */
  public MongoCollection<Document> getCollection(String collName) {
    if (null == collName || "".equals(collName)) {
      return null;
    }
    MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
    return collection;
  }

  /**
   * 查询DB下的所有Collection名称
   */
  public List<String> getAllCollections() {
    MongoIterable<String> colls = getDB().listCollectionNames();
    List<String> list = new ArrayList<String>();
    for (String s : colls) {
      list.add(s);
    }
    return list;
  }

  /**
   * 关闭Mongodb
   */
  public void close() {
    if (mongoClient != null) {
      mongoClient.close();
      mongoClient = null;
    }
  }

  /**
   * 查找对象 - 根据主键_id
   */
  public Document findById(MongoCollection<Document> coll, String id) {
    ObjectId _idobj = null;
    try {
      _idobj = new ObjectId(id);
    } catch (Exception e) {
      return null;
    }
    Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
    return myDoc;
  }

  /**
   * 统计数
   */
  public int getCount(MongoCollection<Document> coll) {
    int count = (int) coll.count();
    return count;
  }

  /**
   * 条件查询
   */
  public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
    return coll.find(filter).iterator();
  }

  /**
   * 分页查询
   */
  public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
    Bson orderBy = new BasicDBObject("_id", 1);
    return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
  }

  /**
   * 通过ID删除
   */
  public int deleteById(MongoCollection<Document> coll, String id) {
    int count = 0;
    ObjectId _id = null;
    try {
      _id = new ObjectId(id);
    } catch (Exception e) {
      return 0;
    }
    Bson filter = Filters.eq("_id", _id);
    DeleteResult deleteResult = coll.deleteOne(filter);
    count = (int) deleteResult.getDeletedCount();
    return count;
  }

  /**
   * FIXME
   */
  public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
    ObjectId _idobj = null;
    try {
      _idobj = new ObjectId(id);
    } catch (Exception e) {
      return null;
    }
    Bson filter = Filters.eq("_id", _idobj);
    // coll.replaceOne(filter, newdoc); // 完全替代
    coll.updateOne(filter, new Document("$set", newdoc));
    return newdoc;
  }

  //--------------------------------------------------------------------------------------------------------------------
  public boolean isExits(String dbName, String collectionName, Map<String, Object> filterMap) {
    if (filterMap != null) {
      FindIterable<Document> docs = mongoClient.getDatabase(dbName).getCollection(collectionName).find(new Document(filterMap));

      Document doc = docs.first();
      if (doc != null) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }


  public boolean insert(String dbName, String collectionName, Map<String, Object> insertMap) {
    if (insertMap != null) {
      mongoClient.getDatabase(dbName).getCollection(collectionName).insertOne(new Document(insertMap));
      return true;
    }
    return false;
  }

  public boolean deleteById(String dbName, String collectionName, String _id) {
    ObjectId objectId = new ObjectId(_id);
    Bson filter = Filters.eq("_id", objectId);

    DeleteResult deleteResult = getDatabase(dbName).getCollection(collectionName).deleteOne(filter);
    long deletedCount = deleteResult.getDeletedCount();

    return deletedCount > 0 ? true : false;
  }


  public boolean delete(String dbName, String collectionName, Map<String, Object> map) {
    if (map != null) {
      DeleteResult result = mongoClient.getDatabase(dbName).getCollection(collectionName).deleteMany(new Document(map));
      long deletedCount = result.getDeletedCount();
      return deletedCount > 0 ? true : false;
    }
    return false;
  }


  public boolean updateOne(String dbName, String collectionName, Map<String, Object> filterMap, Map<String, Object> updateMap) {
    if (filterMap != null && filterMap.size() > 0 && updateMap != null) {
      UpdateResult result = mongoClient.getDatabase(dbName).getCollection(collectionName).updateOne(new Document(filterMap), new Document("$set", new Document(updateMap)));
      long modifiedCount = result.getModifiedCount();
      return modifiedCount > 0 ? true : false;
    }

    return false;
  }

  public boolean updateById(String dbName, String collectionName, String _id, Document updateDoc) {
    ObjectId objectId = new ObjectId(_id);
    Bson filter = Filters.eq("_id", objectId);

    UpdateResult result = getDatabase(dbName).getCollection(collectionName).updateOne(filter, new Document("$set", updateDoc));
    long modifiedCount = result.getModifiedCount();

    return modifiedCount > 0 ? true : false;
  }

  public List<Document> find(String dbName, String collectionName, Bson filter) {
    final List<Document> resultList = new ArrayList<Document>();
    if (filter != null) {
      FindIterable<Document> docs = mongoClient.getDatabase(dbName).getCollection(collectionName).find(filter);
      docs.forEach(new Block<Document>() {

        public void apply(Document document) {
          resultList.add(document);
        }
      });
    }

    return resultList;
  }

  public Document findById(String dbName, String collectionName, String _id) {
    ObjectId objectId = new ObjectId(_id);

    Document doc = getDatabase(dbName).getCollection(collectionName).find(Filters.eq("_id", objectId)).first();
    return doc;
  }


  /**
   * 分页查询
   *
   * @param pageIndex 从1开始
   */
  public List<Document> findByPage(String dbName, String collectionName, Bson filter, int pageIndex, int pageSize) {
    Bson orderBy = new BasicDBObject("_id", 1);

    final List<Document> resultList = new ArrayList<Document>();
    FindIterable<Document> docs = getDatabase(dbName).getCollection(collectionName).find(filter).sort(orderBy).skip((pageIndex - 1) * pageSize).limit(pageSize);
    docs.forEach(new Block<Document>() {
      public void apply(Document document) {
        resultList.add(document);
      }
    });

    return resultList;
  }

  public MongoCollection getCollection(String dbName, String collectionName) {
    return mongoClient.getDatabase(dbName).getCollection(collectionName);
  }


  public MongoDatabase getDatabase(String dbName) {
    return mongoClient.getDatabase(dbName);
  }

  public long getCount(String dbName, String collectionName) {
    return getDatabase(dbName).getCollection(collectionName).count();
  }

  /**
   * 查询dbName下的所有表名
   */
  public List<String> getAllCollections(String dbName) {
    MongoIterable<String> cols = getDatabase(dbName).listCollectionNames();
    List<String> _list = new ArrayList<String>();
    for (String s : cols) {
      _list.add(s);
    }
    return _list;
  }

  /**
   * 获取所有数据库名称列表
   */
  public MongoIterable<String> getAllDatabaseName() {
    MongoIterable<String> s = mongoClient.listDatabaseNames();
    return s;
  }

  /**
   * 删除一个数据库
   */
  public void dropDatabase(String dbName) {
    getDatabase(dbName).drop();
  }

  /**
   * 删除collection
   */
  public void dropCollection(String dbName, String collectionName) {
    getDatabase(dbName).getCollection(collectionName).drop();
  }

  /*
   * 测试
   db.createUser(
      {
        user: "ldyldy",
            pwd: "class1013",
          roles: [
        { role: "readWriteAnyDatabase", db: "admin" }
       ]
      }
    )
   */
  public static void main(String[] args) {
    MongoCollection<Document> obj1 = MongoDBUtils.INSTANCE.getCollection("test");
    MongoClient mongoClient1 = MongoDBUtils.INSTANCE.mongoClient;
    MongoCollection<Document> obj2 = MongoDBUtils.INSTANCE.getCollection("test");
    MongoClient mongoClient2 = MongoDBUtils.INSTANCE.mongoClient;
    //输出结果：obj1==obj2?true
    int count1 = MongoDBUtils.INSTANCE.getCount(obj1);
    int count2 = MongoDBUtils.INSTANCE.getCount(obj2);
    System.out.println("o1n:" + count1);
    System.out.println("o2n:" + count2);
    System.out.println(MongoDBUtils.INSTANCE.updateById(obj1, "5b45b832f16aa4581afd15df", new Document().append("name",
        "小王")));
    System.out.println(mongoClient2);

    MongoDBUtils instance = MongoDBUtils.INSTANCE;

    Bson filter = Filters.regex("name", "小*");
    MongoCursor<Document> byPage = instance.findByPage(obj1, filter, 1,
        10);

    while (byPage.hasNext()) {
      Document next = byPage.next();
      System.out.println(next);
    }

    System.out.println("mongoClient1==mongoClient2?" + (mongoClient1 == mongoClient2));//单利
    System.out.println("obj1==obj2?" + (obj1 == obj2));//两次查询结果
  }
}
