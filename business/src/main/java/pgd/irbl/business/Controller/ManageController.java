package pgd.irbl.business.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import pgd.irbl.business.VO.DeleteRepoVO;
import pgd.irbl.business.VO.ModifyRepoVO;
import pgd.irbl.business.VO.RegisterRepoVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 20:47
 */
@RestController
@RequestMapping("/manage")
public class ManageController {

    @PostMapping("/register")
    @ApiOperation(value = "simple message resource")
    public ResponseVO registerRepo(@RequestBody RegisterRepoVO registerRepoVO){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/show")
    public ResponseVO showAllRepo(){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/delete")
    public ResponseVO deleteRepo(@RequestBody DeleteRepoVO repoVO){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/modify")
    public ResponseVO modifyRepo(@RequestBody ModifyRepoVO modifyRepoVO){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/webhooks")
    public ResponseVO webhooks(@)
}
