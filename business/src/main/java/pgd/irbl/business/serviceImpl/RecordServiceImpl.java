package pgd.irbl.business.serviceImpl;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pgd.irbl.business.DTO.RecordWithTime;
import pgd.irbl.business.PO.QueryRecord;
import pgd.irbl.business.VO.FileScore;
import pgd.irbl.business.enums.QueryRecordState;
import pgd.irbl.business.service.RecordService;
import pgd.irbl.business.VO.QueryRecordVO;
import pgd.irbl.business.VO.ResponseVO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:46
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseVO getUserAllRecord(Long userId) {
        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
        List<RecordWithTime> result = new ArrayList<>();
        queryRecord.find(eq("userId", userId)).projection(Projections.include("_id", "queryTime")).forEach(document -> result.add(new RecordWithTime(document)));
        return ResponseVO.buildSuccess(result);
    }

    @Override
    public QueryRecord getQueryRecordById(QueryRecordVO queryRecordVO) {
        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
        Document document = null;
        try{
            document = queryRecord.find(eq("_id", new ObjectId(queryRecordVO.getRecordId()))).first();
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
            return null;
        }
        if(document == null) return null;
        return new QueryRecord(document);
    }

    @Override
    public String insertQueryRecord(Long userId) {
        QueryRecord queryRecord = new QueryRecord();
        queryRecord.setUserId(userId);
        queryRecord.setQueryTime(new Timestamp(System.currentTimeMillis()));
        queryRecord.setRepoCommitId("未设置commitId");
        queryRecord.setGitUrl("未设置gitUrl");
        queryRecord.setQueryRecordState(QueryRecordState.preprocessing);
        InsertOneResult oneResult =  mongoTemplate.getCollection("queryRecord").insertOne(Document.parse(JSONObject.toJSONString(queryRecord)));
        return oneResult.getInsertedId().asObjectId().getValue().toString();
    }

    @Override
    public int setQueryRecordQuerying(String recordId) {
        return changeQueryRecordState(recordId, QueryRecordState.preprocessing, QueryRecordState.querying);
    }

    @Override
    public int setQueryRecordFail(String recordId) {
        return changeQueryRecordState(recordId, null, QueryRecordState.fail);
    }

    @Override
    public int setQueryRecordComplete(String recordId, List<FileScore> fileScoreList) {
        int ret = changeQueryRecordState(recordId, QueryRecordState.querying, QueryRecordState.complete);
        if(ret == 0)
        {
            MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
            List<Document> documents = new ArrayList<>();
            for(FileScore fileScore : fileScoreList) documents.add(Document.parse(JSONObject.toJSONString(fileScore)));
            queryRecord.updateOne(eq("_id", new ObjectId(recordId)), set("fileScoreList", documents));
            return 0;
        }
        else return ret;
    }

    // 0表示成功，-1表示不存在这个数据，或者你要修改的那一项是不存在的
    public int changeQueryRecordState(String recordId, QueryRecordState from, QueryRecordState to)
    {
        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
        Document document = queryRecord.find(eq("_id", new ObjectId(recordId))).first();
        if(document == null) return -1;
        if(document.get("queryRecordState") == null) return -1;
        if(from == null || QueryRecordState.valueOf(document.getString("queryRecordState")).equals(from))
        {
            queryRecord.updateOne(eq("_id", new ObjectId(recordId)), set("queryRecordState", to.getValue()));
            return 0;
        }
        return 1;
    }

}
