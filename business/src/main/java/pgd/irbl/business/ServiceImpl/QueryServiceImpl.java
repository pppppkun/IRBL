package pgd.irbl.business.ServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.Service.QueryService;
import pgd.irbl.business.Utils.MyFileUtil;
import pgd.irbl.business.VO.ResponseVO;

import java.io.IOException;

/**
 * @Author: qin
 * @CreateTime: 2021-04-06 20:20
 */
@Service
public class QueryServiceImpl implements QueryService {

    @Value("${file.path.code}")
    String codePath;

    @Value("${file.path.report}")
    String reportPath;

    @Override
    public ResponseVO queryRegister(MultipartFile bugReport, String commitID) {
        //todo
        return null;
    }

    @Override
    public ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode) {
        String bugReportFullPath, codeFullPath;
        try {
            bugReportFullPath = MyFileUtil.saveFile(reportPath, bugReport);
            codeFullPath = MyFileUtil.saveFile(codePath, bugReport);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVO.buildFailure(e);
        }
        if(bugReportFullPath==null || codeFullPath==null){
            return ResponseVO.buildFailure(null);
        }
        //todo call gRPC

        return null;
    }
}
