package pgd.irbl.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.service.ManageService;
import pgd.irbl.business.vo.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-12 21:22
 */
@RestController
public class RepositoryController {

    @Autowired
    ManageService manageService;

    @PostMapping("/repository/list")
    public ResponseVO getAllRepo(){
        return ResponseVO.buildSuccess(manageService.getAllSimpleRepo());
    }

    @PostMapping("/repositoryu/{id}/commit/list")
    public ResponseVO getRepoCommit(@PathVariable("id") Long id){
        return manageService.getRepoCommit(id);
    }

}
