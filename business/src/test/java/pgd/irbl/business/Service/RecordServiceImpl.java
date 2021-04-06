package pgd.irbl.business.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.PO.QueryRecord;
import pgd.irbl.business.VO.QueryRecordVO;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.enums.QueryRecordState;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-06 10:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecordServiceImpl {

    @Autowired
    RecordService recordService;
    @Autowired
    MongoTemplate mongoTemplate;
    InsertOneResult oneResult;
    InsertOneResult twoResult;

    @Before
    public void setUp(){
        mongoTemplate.dropCollection("queryRecord");
        mongoTemplate.createCollection("queryRecord");
        QueryRecord queryRecord = new QueryRecord();
        queryRecord.setQueryRecordState(QueryRecordState.querying);
        queryRecord.setGitUrl("xxx.xxx.xxx.xxx");
        queryRecord.setUserId(2L);
        queryRecord.setFileScoreList(null);
        queryRecord.setRepoCommitId("zxcvbnmasdfgh");
        queryRecord.setQueryTime(new Timestamp(System.currentTimeMillis()));
        QueryRecord queryRecord1 = new QueryRecord();
        BeanUtils.copyProperties(queryRecord, queryRecord1);
        queryRecord1.setQueryTime(new Timestamp(System.currentTimeMillis()));
        queryRecord1.setQueryRecordState(QueryRecordState.complete);
        oneResult = mongoTemplate.getCollection("queryRecord").insertOne(Document.parse(JSONObject.toJSONString(queryRecord)));
        twoResult = mongoTemplate.getCollection("queryRecord").insertOne(Document.parse(JSONObject.toJSONString(queryRecord1)));
    }

    @Test
    public void Test1(){
        ResponseVO responseVO = recordService.getUserAllRecord(2L);
        Assert.assertTrue(responseVO.getSuccess());
        Assert.assertEquals(2, ((List)responseVO.getContent()).size());
    }


    @Test
    public void Test2(){
        QueryRecordVO queryRecordVO = new QueryRecordVO();
        queryRecordVO.setRecordId(oneResult.getInsertedId().asObjectId().getValue().toString());
        QueryRecord record = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertEquals("zxcvbnmasdfgh", record.getRepoCommitId());

    }

    @Test
    public void Test3(){
        QueryRecordVO queryRecordVO = new QueryRecordVO();
        queryRecordVO.setRecordId("alksdjk");
        QueryRecord record = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertNull(record);
    }


}
