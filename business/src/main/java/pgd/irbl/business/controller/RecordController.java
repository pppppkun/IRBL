package pgd.irbl.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.service.RecordService;
import pgd.irbl.business.utils.JwtUtil;
import pgd.irbl.business.VO.QueryRecordVO;
import pgd.irbl.business.VO.ResponseVO;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseVO getUserAllRecord(HttpServletRequest request){
        String token = request.getHeader("irbl-token");
        Long userId = JwtUtil.verifyTokenAndGetUserId(token);
        return recordService.getUserAllRecord(userId);
    }

    @PostMapping("/queryRecord")
    public ResponseVO queryRecord(@RequestBody QueryRecordVO queryRecordVO){
        Object o = recordService.getQueryRecordById(queryRecordVO);
        if(o==null) return ResponseVO.buildFailure("_id不存在");
        else return ResponseVO.buildSuccess(o);
    }

}
