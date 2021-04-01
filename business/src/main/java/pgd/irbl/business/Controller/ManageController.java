package pgd.irbl.business.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pgd.irbl.business.PO.Repository;
import pgd.irbl.business.Service.ManageService;
import pgd.irbl.business.VO.DeleteRepoVO;
import pgd.irbl.business.VO.ModifyRepoVO;
import pgd.irbl.business.VO.RegisterRepoVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 20:47
 */
@RestController
@RequestMapping("/manageRepo")
public class ManageController {

    ManageService manageService;
    @Autowired
    public void setManageService(ManageService manageService) {
        this.manageService = manageService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册代码仓库")
    public ResponseVO registerRepo(@RequestBody RegisterRepoVO registerRepoVO){
        return manageService.registerRepo(registerRepoVO);
    }

    @PostMapping("/show")
    @ApiOperation(value = "展示所有代码仓库", response = Repository.class, notes = "这个方法的返回值是List Repository")
    public ResponseVO showAllRepo(){
        Object o = manageService.getAllRepo();
        if(o==null) return ResponseVO.buildFailure("查询代码仓库失败");
        else return ResponseVO.buildSuccess(o);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除指定的代码仓库")
    public ResponseVO deleteRepo(@RequestBody DeleteRepoVO repoVO){
        return manageService.deleteRepo(repoVO);
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改代码仓库")
    public ResponseVO modifyRepo(@RequestBody ModifyRepoVO modifyRepoVO){
        return manageService.modifyRepo(modifyRepoVO);
    }

    @PostMapping("/webhooks")
    @ApiOperation(value = "webhooks，这个接口暂时用不到")
    public ResponseVO webhooks(@RequestBody Object o){
        return ResponseVO.buildSuccess();
    }
}
