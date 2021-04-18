package pgd.irbl.business.vo;

import lombok.Data;
import org.bson.Document;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-26 23:10
 */
@Data
public class FileScore {
    Double score;
    // this field contain name
    String filePath;

    public FileScore(){};
    public FileScore(Document document) {
        this.score = document.getDouble("score");
        this.filePath = document.getString("filePath");
    }

}
