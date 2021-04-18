package pgd.irbl.business.service;

import pgd.irbl.business.po.QueryRecord;
import pgd.irbl.business.vo.FileScore;
import pgd.irbl.business.vo.QueryRecordVO;
import pgd.irbl.business.vo.ResponseVO;

import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:45
 */
public interface RecordService {

    ResponseVO getUserAllRecord(Long userId);
    QueryRecord getQueryRecordById(QueryRecordVO queryRecordVO);
    String insertQueryRecord(Long userId);
    int setQueryRecordQuerying(String recordId);
    int setQueryRecordFail(String recordId);
    int setQueryRecordComplete(String recordId, List<FileScore> fileScoreList);


}
