package pgd.irbl.business.VO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 20:48
 */
@Slf4j
@Data
public class ResponseVO {

    /**
     * 调用是否成功
     */
    private Boolean success;


    /**
     * 内容
     */
    private Object content;

    public static ResponseVO buildSuccess(){
        ResponseVO response=new ResponseVO();
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildSuccess(Object content){
        ResponseVO response=new ResponseVO();
        response.setContent(content);
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildFailure(Object content){
        ResponseVO response=new ResponseVO();
        response.setSuccess(false);
        response.setContent(content);
//        System.out.println(message);
        log.info(content.toString());
        return response;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}