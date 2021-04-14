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
}
