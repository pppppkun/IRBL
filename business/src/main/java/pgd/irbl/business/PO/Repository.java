package pgd.irbl.business.PO;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 14:10
 */
@Data
public class Repository {
    Long Id;
    String gitUrl;
    String description;
    String status;
    Timestamp startTime;
    Timestamp endTime;
}
