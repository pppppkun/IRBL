package pgd.irbl.business.service;

import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.vo.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 23:40
 */
public interface QueryService {

    ResponseVO queryRegister(MultipartFile bugReport, String commitId, Long userId);

    ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode, Long userId);

}
