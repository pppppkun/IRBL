package pgd.irbl.business.utils;

import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    @Test
    public void unzipJar() {
        String codePath = "C:\\Users\\10444\\Desktop\\se3\\data\\source-code\\";
        String zipName = "C:\\Users\\10444\\Desktop\\se3\\data\\test.zip";
//        try {
//            MyFileUtil.unZip(codePath ,);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void unZipAndSaveDir() throws IOException {
        String codePath = "C:\\Users\\10444\\Desktop\\se3\\data\\source-code\\";
        String zipName = "C:\\Users\\10444\\Desktop\\se3\\data\\test.zip";
//        File file = new File(zipName);
//        FileInputStream fileInputStream = null;
//        fileInputStream = new FileInputStream(file);
//        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
//                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
//        MyFileUtil.unZipAndSaveDir(codePath, multipartFile);
    }
}