package pgd.irbl.business.service;


import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.VO.ResponseVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;


/**
 * @Author: qin
 * @CreateTime: 2021-04-11 16:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QueryService queryService;

    private final String ROOT_PATH  = "E:\\code-test\\";

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

    @Test
    public void queryNotRegisterTest1(){
        ResponseVO responseVO = queryService.queryNotRegister(null, null);
        Assert.assertFalse(responseVO.getSuccess());
    }
    @Test
    public void queryNotRegisterTest2(){
        MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "filename.txt", "text/plain", "hello1".getBytes(StandardCharsets.UTF_8));

//        ResponseVO responseVO = queryService.queryNotRegister(mockMultipartFile, mockMultipartFile);
//        ArrayList<FileScore> content = (ArrayList<FileScore>) responseVO.getContent();
//        System.out.println(content.get(0).getFilepath());
//        System.out.println(content.get(0).getScore());

//        Assert.assertTrue(responseVO.getSuccess());
    }

    @Test
    public void queryNotRegisterTest3() throws IOException {
        String codePath = "C:\\Users\\10444\\Desktop\\se3\\data\\source-code\\";
        String zipName = "C:\\Users\\10444\\Desktop\\se3\\data\\test.zip";
        File file = new File(zipName);
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("filename.txt", "filename.txt", "text/plain", "report test query not register".getBytes(StandardCharsets.UTF_8));

        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        ResponseVO responseVO = queryService.queryNotRegister(mockMultipartFile, multipartFile);
        Assert.assertTrue(responseVO.getSuccess());

    }

}
