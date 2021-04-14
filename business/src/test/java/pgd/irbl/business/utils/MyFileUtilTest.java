package pgd.irbl.business.utils;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * @author qin
 * @description
 * @date 2021-04-06
 */
public class MyFileUtilTest {

    private final String ROOT_PATH  = "E:\\code-test\\";
    @Test
    public void saveFileTest() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "filename.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));
        String fileFullPath = MyFileUtil.saveFile(ROOT_PATH , mockMultipartFile, "hello.txt");
        assertEquals(fileFullPath, ROOT_PATH+"hello.txt");

    }
}