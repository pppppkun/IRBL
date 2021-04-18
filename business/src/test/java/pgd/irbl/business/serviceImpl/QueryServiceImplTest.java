package pgd.irbl.business.serviceImpl;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.service.QueryService;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * @author qin
 * @description QueryServiceImplTest
 * @date 2021-04-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryServiceImplTest {

    @Autowired
    QueryService queryService;

    @Test
    public void queryNotRegister() {
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "filename.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));
//        queryService.queryNotRegister(mockMultipartFile, mockMultipartFile, 1L);

    }
}