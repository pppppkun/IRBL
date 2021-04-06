package pgd.irbl.business.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import jdk.nashorn.internal.parser.JSONParser;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.PO.QueryRecord;
import pgd.irbl.business.enums.QueryRecordState;

import java.sql.Timestamp;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 16:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void test(){
//        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
//        QueryRecord queryRecord1 = new QueryRecord();
//        queryRecord1.setQueryRecordState(QueryRecordState.querying);
//        queryRecord1.setQueryTime(new Timestamp(System.currentTimeMillis()));
//        queryRecord1.setFileScoreList(null);
//        queryRecord1.setRepoCommitId("askldjaklsj`123");
//        queryRecord1.setGitUrl("TESTMONGODB");
//        System.out.println(JSONObject.toJSONString(queryRecord1));
//        Document document = Document.parse(JSONObject.toJSONString(queryRecord1));
//        InsertOneResult insertOneResult = queryRecord.insertOne(document);
//
//        ObjectId objectId = insertOneResult.getInsertedId().asObjectId().getValue();
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", objectId);
//        Document document1 = queryRecord.find(query).first();
//
//        System.out.println(document1);

    }


}
