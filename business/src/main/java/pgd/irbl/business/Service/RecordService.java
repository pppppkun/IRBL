package pgd.irbl.business.Service;

import pgd.irbl.business.PO.QueryRecord;
import pgd.irbl.business.VO.GetUserAllRecordVO;
import pgd.irbl.business.VO.QueryRecordVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:45
 */
public interface RecordService {

    ResponseVO getUserAllRecord(GetUserAllRecordVO getUserAllRecordVO);
    QueryRecord getQueryRecordById(QueryRecordVO queryRecordVO);

}
