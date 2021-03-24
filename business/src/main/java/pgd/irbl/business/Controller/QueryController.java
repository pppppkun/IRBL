package pgd.irbl.business.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 17:38
 */
@RestController
@RequestMapping("/query")
public class QueryController {

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @PostMapping("/uploadRegister")
    public ResponseVO uploadRegister(@RequestParam("bugReport") MultipartFile bugReport, @RequestParam("commitID") String commitID ){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/uploadUnRegister")
    public ResponseVO uploadUnRegister(@RequestParam("bugReport") MultipartFile bugReport, @RequestParam("sourceCode") MultipartFile sourceCode){
        return ResponseVO.buildSuccess();
    }
}
