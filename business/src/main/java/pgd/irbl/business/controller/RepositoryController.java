package pgd.irbl.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pgd.irbl.business.service.ManageService;
import pgd.irbl.business.vo.FilePath;
import pgd.irbl.business.vo.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-12 21:22
 */
@RestController
@RequestMapping("/repository")
public class RepositoryController {

    ManageService manageService;

    @Autowired
    public void setManageService(ManageService manageService){
        this.manageService = manageService;
    }

    @PostMapping("/list")
    public ResponseVO getAllRepo(){
        return ResponseVO.buildSuccess(manageService.getAllSimpleRepo());
    }

    @PostMapping("/{id}/commit/list")
    public ResponseVO getRepoCommit(@PathVariable("id") Long id){
        return manageService.getRepoCommit(id);
    }

    @PostMapping("/commit/{commitId}/file")
    public ResponseVO getFileByCommitId(@RequestBody FilePath filePath, @PathVariable("commitId") String commitId){
        return manageService.getFileByCommit(filePath.getPath(), commitId);
    }

}
