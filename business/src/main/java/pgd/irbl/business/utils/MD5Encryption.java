package pgd.irbl.business.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:35
 */
@Slf4j
public class MD5Encryption {

    /**
     *  使用 MD5算法 计算敏感数据摘要，并用 base64 编码返回
     *
     *  base64只有64个字符，因此只需要6个二进制位来表示 实现:每3个字节为一组凑4个base64字符。
     *  多余一个字节补4个0bit位(共12位)，凑成2个base64字符；多余两个字节补2个bit位(共18位)，凑成3个base64字符。
     *  为了知道添加了bit位,(便于解码)，一个=表示添加了2个bit位，两个=表示添加了4个bit位
     *  MD5 得到 128位 前120位 20个base64字符 多余8位一字节 补4个0bit位 用两个=表示
     *  123456 -> 4QrcOUm6Wau+VuBX8g+IPg==
     *  最终结果24位
     *
     * @param s 待计算的数据
     * @return MD5信息摘要
     */
    public static String encrypt(String s){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = s.getBytes();
            byte[] output = md.digest(input);
            return Base64.encodeBase64String(output); // 6位编码
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return s;
        }
    }
}
