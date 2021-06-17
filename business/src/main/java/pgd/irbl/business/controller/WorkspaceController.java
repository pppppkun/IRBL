package pgd.irbl.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.service.WorkspaceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-17 10:53
 */
@RestController
@Slf4j
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    @GetMapping("/workspace")
    public void smsJumpUrl(HttpServletResponse response, @RequestParam(required = false, defaultValue = "null") String recordId, @RequestParam(required = false, defaultValue = "null") String repositoryId) throws Exception {
        int flag = 1;
        if (recordId.equals("null")) flag = 0;
        String path = workspaceService.getWorkspacePath(repositoryId, flag);
        log.info(path);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", "http://116.85.66.200:9001/?folder=/root/project/" + path);
    }

}
