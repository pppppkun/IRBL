package pgd.irbl.business.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:29
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @PostMapping("/getUserAllRecord")
    public ResponseVO getUserAllRecord(){
        return null;
    }

    @PostMapping("/queryRecord")
    public ResponseVO queryRecord(){
        return null;
    }

}
