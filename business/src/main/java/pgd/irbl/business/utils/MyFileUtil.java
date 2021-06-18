package pgd.irbl.business.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author qin
 * @description 文件工具类
 * @createDate 2021-04-06
 * @modifyDate 2021-06-16
 */
@Slf4j
public class MyFileUtil {

    /**
     * 将 multipartFile 源文件 保存为 rootPath + fileName
     *
     * @param rootPath 文件路径前缀
     * @param multipartFile 源文件
     * @param fileName 指定文件名
     * @return 文件名
     * @throws IOException 输入输出异常
     */
    public static String saveFile(String rootPath, MultipartFile multipartFile, String fileName) throws IOException {
        log.info("enter save file. root path: " + rootPath + " filename: " + fileName + " upload filename: " + multipartFile.getOriginalFilename());
        File file = new File(rootPath + fileName);
        log.info("begin to transfer file from multipartFile to " + fileName);
        multipartFile.transferTo(file);
        return fileName;
    }

    /**
     * 将 multipartFile 源文件（.zip文件）保存到 codePath + fileName  并解压
     *
     * @param codePath 文件路径前缀
     * @param multipartFile zip 源文件
     * @param fileName 指定文件名
     * @return 文件名
     * @throws IOException 输入输出异常
     */
    public static String unZipAndSaveDir(String codePath, MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(codePath + fileName);
        multipartFile.transferTo(file);
        unZip(codePath, codePath + fileName);
        return fileName.substring(0, fileName.length() - 4);
    }

    /**
     * 将 zipPath 的 .zip文件 解压到 destDir 目的文件夹
     *
     * @param destDir 目的文件夹
     * @param zipPath zip 文件路径
     * @return 文件名
     * @throws IOException 输入输出异常
     */
    public static void unZip(String destDir, String zipPath) throws IOException {
        File fileZip = new File(zipPath);
        ZipFile zip = new ZipFile(fileZip);
//            ZipFile zip = new ZipFile(file, Charset.forName("GBK"));//解决中文文件夹乱码
        String name = zip.getName().substring(zip.getName().lastIndexOf(File.separatorChar) + 1, zip.getName().lastIndexOf('.'));
        File pathFile = new File(destDir + name);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
//            String outPath = (destDir + name + File.separatorChar + zipEntryName).replaceAll("\\*", "/");
            String outPath = (destDir + name + File.separatorChar + zipEntryName).replaceAll("/", Matcher.quoteReplacement(File.separator));
            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separatorChar)));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            FileOutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        log.info("******************解压完毕********************");
    }

}
