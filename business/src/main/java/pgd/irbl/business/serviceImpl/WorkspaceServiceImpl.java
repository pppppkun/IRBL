package pgd.irbl.business.serviceImpl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pgd.irbl.business.dao.RepoMapper;
import pgd.irbl.business.dto.RecordWithTime;
import pgd.irbl.business.po.QueryRecord;
import pgd.irbl.business.service.WorkspaceService;
import pgd.irbl.business.vo.ResponseVO;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-17 13:25
 */
@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    RepoMapper repoMapper;

    @Autowired
    MongoTemplate mongoTemplate;


    @Value("${repo_direction}")
    private String REPO_DIRECTION;

    @Override
    public String getWorkspacePath(String id, int flag) {
        return flag == 1? getRecordWorkspacePath(id) : getRepoWorkspacePath(Long.valueOf(id));
    }

    private String getRepoWorkspacePath(Long repositoryId){
        String gitUrl = repoMapper.findGitUrlByRepoId(repositoryId);
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        log.info(gitUrl);
        return repoName + gitUrl.hashCode();
    }

    private String getRecordWorkspacePath(String recordId){
        MongoCollection<Document> queryRecord = mongoTemplate.getCollection("queryRecord");
        Document document = null;
        try{
            document = queryRecord.find(eq("_id", new ObjectId(recordId))).first();
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
            return null;
        }
        if(document == null) return null;
        log.info(recordId);
        log.info(document.toString());
        return document.getString("path");
    }
}
