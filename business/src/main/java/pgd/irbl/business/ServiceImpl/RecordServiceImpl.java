package pgd.irbl.business.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pgd.irbl.business.PO.QueryRecord;
import pgd.irbl.business.Service.RecordService;
import pgd.irbl.business.VO.GetUserAllRecordVO;
import pgd.irbl.business.VO.QueryRecordVO;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.enums.QueryRecordState;

import java.sql.Timestamp;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:46
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseVO getUserAllRecord(GetUserAllRecordVO getUserAllRecordVO) {
        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
        QueryRecord queryRecord1 = new QueryRecord();
        queryRecord1.setQueryRecordState(QueryRecordState.querying);
        queryRecord1.setQueryTime(new Timestamp(System.currentTimeMillis()));
        queryRecord1.setFileScoreList(null);
        queryRecord1.setRepoCommitId("askldjaklsj`123");
        queryRecord1.setGitUrl("TESTMONGODB");
        System.out.println(JSONObject.toJSONString(queryRecord1));
        Document document = Document.parse(JSONObject.toJSONString(queryRecord1));
        InsertOneResult insertOneResult = queryRecord.insertOne(document);

        ObjectId objectId = insertOneResult.getInsertedId().asObjectId().getValue();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", objectId);
        Document document1 = queryRecord.find(query).first();

        System.out.println(document1);
        return null;
    }

    @Override
    public QueryRecord getQueryRecordById(QueryRecordVO queryRecordVO) {
        return null;
    }
}
