package pgd.irbl.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.service.WorkspaceService;


import javax.servlet.http.HttpServletResponse;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-17 10:53
 */
@RestController
@Slf4j
public class WorkspaceController {

    WorkspaceService workspaceService;

    @Autowired
    public void setWorkspaceService( WorkspaceService workspaceService ){
        this.workspaceService = workspaceService;
    }

    @GetMapping("/workspace")
    public void smsJumpUrl(HttpServletResponse response, @RequestParam(required = false, defaultValue = "null") String recordId, @RequestParam(required = false, defaultValue = "null") String repositoryId) {
        String path = "";
        if (recordId.equals("null")) path = workspaceService.getWorkspacePath(repositoryId, 0);
        else path = workspaceService.getWorkspacePath(recordId, 1);
        log.info(path);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        String workspaceUrl = "http://116.85.66.200:9001/?folder=/root/project/";
        response.setHeader("Location", workspaceUrl + path);
    }

}
