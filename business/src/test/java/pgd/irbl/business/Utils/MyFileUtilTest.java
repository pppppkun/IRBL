package pgd.irbl.business.Utils;

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

    @Test
    public void saveFileTest() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "filename.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));
        String fileFullPath = MyFileUtil.saveFile("E:\\code-test\\", mockMultipartFile, "hello.txt");
        assertEquals(fileFullPath, "E:\\code-test\\hello.txt");

    }
}