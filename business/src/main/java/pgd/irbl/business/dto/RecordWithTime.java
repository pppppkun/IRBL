package pgd.irbl.business.dto;

import lombok.Data;
import org.bson.Document;

import java.sql.Timestamp;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-06 10:42
 */
@Data
public class RecordWithTime {
    String recordId;
    Timestamp queryTime;

    public RecordWithTime() {
    }

    public RecordWithTime(Document document) {
        recordId = document.getObjectId("_id").toString();
        queryTime = new Timestamp(((Number) document.get("queryTime")).longValue());
    }
}
