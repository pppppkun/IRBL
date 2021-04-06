package pgd.irbl.business.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author qin
 * @description  save file
 * @date 2021-04-06
 */

public class MyFileUtil {

//    private static String savePath;

    public static String saveFile(String rootPath, MultipartFile multipartFile) throws IOException {
        return saveFile(rootPath, multipartFile,String.valueOf( System.currentTimeMillis() ) );
    }

    public static String saveFile(String rootPath, MultipartFile multipartFile,  String fileName) throws IOException {
        String fullFileName = rootPath + fileName;
        File file= new File(fullFileName);
        multipartFile.transferTo(file);
        return fullFileName;
    }

//    public static void setSavePath(String savePath) {
//        MyFileUtil.savePath = savePath;
//    }
//
//    public static String getSavePath() {
//        System.out.println(savePath);
//        return savePath;
//    }
}
