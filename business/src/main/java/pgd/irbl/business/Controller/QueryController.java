package pgd.irbl.business.Controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.PO.Repository;
import pgd.irbl.business.VO.FileScore;
import pgd.irbl.business.VO.ResponseVO;

import java.io.File;
import java.sql.Date;
import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 17:38
 */
@RestController
@RequestMapping("/queryDefects")
public class QueryController {


    @PostMapping("/uploadRegister")
    @ApiOperation(value = "上传的报告属于已注册的项目", response = FileScore.class, notes = "这个方法的返回值是List queryRecord")
    public ResponseVO uploadRegister(@ApiParam(value = "这个参数是MultipartFile类型") @RequestParam("bugReport") MultipartFile bugReport, @RequestParam("commitID") String commitID ){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/uploadUnRegister")
    @ApiOperation(value = "上传的报告属于未注册的项目", response = FileScore.class, notes = "这个方法的返回值是List queroRecord")
    public ResponseVO uploadUnRegister(@ApiParam(value = "这个参数是MultipartFile类型") @RequestParam("bugReport") MultipartFile bugReport, @RequestParam("sourceCode") MultipartFile sourceCode){
        return ResponseVO.buildSuccess();
    }
}
