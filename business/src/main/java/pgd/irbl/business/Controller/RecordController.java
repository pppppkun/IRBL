package pgd.irbl.business.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.Service.RecordService;
import pgd.irbl.business.VO.GetUserAllRecordVO;
import pgd.irbl.business.VO.QueryRecordVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:29
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    RecordService recordService;

    @PostMapping("/getUserAllRecord")
    public ResponseVO getUserAllRecord(@RequestBody GetUserAllRecordVO getUserAllRecordVO){
        return recordService.getUserAllRecord(getUserAllRecordVO);
    }

    @PostMapping("/queryRecord")
    public ResponseVO queryRecord(@RequestBody QueryRecordVO queryRecordVO){
        Object o = recordService.getQueryRecordById(queryRecordVO);
        if(o==null) return ResponseVO.buildFailure("_id不存在");
        else return ResponseVO.buildSuccess(o);
    }

}
