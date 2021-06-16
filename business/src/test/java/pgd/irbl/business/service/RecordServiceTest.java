package pgd.irbl.business.service;

import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;
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
import pgd.irbl.business.po.QueryRecord;
import pgd.irbl.business.vo.FileScore;
import pgd.irbl.business.vo.QueryRecordVO;
import pgd.irbl.business.vo.ResponseVO;
import pgd.irbl.business.enums.QueryRecordState;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-06 10:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecordServiceTest {

    @Autowired
    RecordService recordService;
    @Autowired
    MongoTemplate mongoTemplate;
    InsertOneResult oneResult;
    InsertOneResult twoResult;

    @Before
    public void setUp() {
        mongoTemplate.dropCollection("queryRecord");
        mongoTemplate.createCollection("queryRecord");
        QueryRecord queryRecord = new QueryRecord();
        queryRecord.setQueryRecordState(QueryRecordState.preprocessing);
        queryRecord.setGitUrl("xxx.xxx.xxx.xxx");
        queryRecord.setUserId(2L);
        queryRecord.setFileScoreList(null);
        queryRecord.setRepoCommitId("zxcvbnmasdfgh");
        queryRecord.setQueryTime(new Timestamp(System.currentTimeMillis()));
        QueryRecord queryRecord1 = new QueryRecord();
        BeanUtils.copyProperties(queryRecord, queryRecord1);
        queryRecord1.setQueryTime(new Timestamp(System.currentTimeMillis()));
        queryRecord1.setQueryRecordState(QueryRecordState.complete);
        queryRecord1.setName("123");
        oneResult = mongoTemplate.getCollection("queryRecord").insertOne(Document.parse(JSONObject.toJSONString(queryRecord)));
        twoResult = mongoTemplate.getCollection("queryRecord").insertOne(Document.parse(JSONObject.toJSONString(queryRecord1)));
    }

    @Test
    public void Test1getUserAllRecord() {
        ResponseVO responseVO = recordService.getUserAllRecord(2L);
        Assert.assertTrue(responseVO.getSuccess());
        Assert.assertEquals(2, ((List) responseVO.getContent()).size());
    }


    @Test
    public void Test2queryRecord() {
        QueryRecordVO queryRecordVO = new QueryRecordVO();
        queryRecordVO.setRecordId(oneResult.getInsertedId().asObjectId().getValue().toString());
        QueryRecord record = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertEquals("zxcvbnmasdfgh", record.getRepoCommitId());

    }

    @Test
    public void Test3queryRecord() {
        QueryRecordVO queryRecordVO = new QueryRecordVO();
        queryRecordVO.setRecordId("alksdjk");
        QueryRecord record = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertNull(record);
    }

    @Test
    public void Test4setQueryState() {
        recordService.setQueryRecordQuerying(Objects.requireNonNull(oneResult.getInsertedId()).asObjectId().getValue().toString());
        QueryRecordVO queryRecordVO = new QueryRecordVO();
        queryRecordVO.setRecordId(oneResult.getInsertedId().asObjectId().getValue().toString());
        QueryRecord record = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertEquals(QueryRecordState.querying, record.getQueryRecordState());
        List<FileScore> fileScoreList = new ArrayList<>();
        Faker faker = new Faker(new Locale("en-US"));
        for (int i = 0; i < 10; i++) {
            FileScore fileScore = new FileScore();
            fileScore.setFilePath(faker.file().fileName());
            fileScore.setScore(faker.number().randomDouble(100, 0, 10));
            fileScoreList.add(fileScore);
        }
        int ret = recordService.setQueryRecordComplete(Objects.requireNonNull(oneResult.getInsertedId()).asObjectId().getValue().toString(), fileScoreList);
        Assert.assertEquals(0, ret);
        record = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertEquals(10, record.getFileScoreList().size());
    }

    @Test
    public void Test5insertRecord() {
        String recordId = recordService.insertQueryRecord(123123L, "1", "2", "www.baidu.com.git#12", "/data/source-code/");
        QueryRecordVO queryRecordVO = new QueryRecordVO();
        queryRecordVO.setRecordId(recordId);
        QueryRecord queryRecord = recordService.getQueryRecordById(queryRecordVO);
        Assert.assertNotNull(queryRecord);
        Assert.assertEquals(Long.valueOf(123123L), queryRecord.getUserId());
        Assert.assertEquals("www.baidu.com.git#12", queryRecord.getName());
    }

}
