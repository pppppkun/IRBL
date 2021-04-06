package pgd.irbl.business.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.Service.QueryService;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: qin
 * @CreateTime: 2021-04-06 20:20
 */
@Service
public class QueryServiceImpl implements QueryService {

    @Override
    public ResponseVO queryRegister(MultipartFile bugReport, String commitID) {
        //todo
        return null;
    }

    @Override
    public ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode){

        return null;
    }
}
