package pgd.irbl.business.serviceImpl;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
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

import java.util.ArrayList;
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
        return null;
    }

    @Override
    public int setQueryRecordQuerying(String recordId) {
        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
        Document document = queryRecord.find(eq("_id", recordId)).first();
        if(document == null) return -1;
        if(document.get("QueryRecordState") == null) return -1;
        if(QueryRecordState.valueOf(document.getString("QueryRecordState")).equals(QueryRecordState.preprocessing))
            document.put("QueryRecordState", QueryRecordState.querying.getValue());
//        queryRecord.updateOne();
        return 0;
    }

    @Override
    public int setQueryRecordFail(String recordId) {
        return 0;
    }

    @Override
    public int setQueryRecordComplete(String recordId, List<FileScore> fileScores) {
        return 0;
    }
}
