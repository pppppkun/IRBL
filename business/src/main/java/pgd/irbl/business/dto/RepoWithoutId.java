package pgd.irbl.business.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 23:36
 */
@Data
public class RepoWithoutId {
    String gitUrl;
    String description;
    String status;
    Timestamp startTime;
    Timestamp endTime;
}
