package pgd.irbl.business.Controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.PO.QueryRecord;
import pgd.irbl.business.Service.QueryService;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 17:38
 */
@RestController
@RequestMapping("/queryDefects")
public class QueryController {

    QueryService queryService;

    @Autowired
    public void setQueryService(QueryService queryService){
        this.queryService = queryService;
    }

    @PostMapping("/uploadRegister")
    @ApiOperation(value = "上传的报告属于已注册的项目", response = QueryRecord.class, notes = "这个方法的返回值是 recordId")
    public ResponseVO uploadRegister(@ApiParam(value = "这个参数是MultipartFile类型") @RequestParam("bugReport") MultipartFile bugReport, @RequestParam("commitID") String commitID ){
        // todo
        return queryService.queryRegister(bugReport, commitID);
    }

    @PostMapping("/uploadUnRegister")
    @ApiOperation(value = "上传的报告属于未注册的项目", response = QueryRecord.class, notes = "这个方法的返回值是 recordId")
    public ResponseVO uploadUnRegister(@ApiParam(value = "这个参数是MultipartFile类型") @RequestParam("bugReport") MultipartFile bugReport, @RequestParam("sourceCode") MultipartFile sourceCode){
        return queryService.queryNotRegister(bugReport, sourceCode);
    }
}
