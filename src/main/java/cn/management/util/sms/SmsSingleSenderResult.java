package cn.management.util.sms;

/**
 * 短信单发结果数据模型，来自腾讯云短信接口示例
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class SmsSingleSenderResult {
    
    public int result;
    
    public String errMsg = "";
    
    public String ext = "";
    
    public String sid = "";
    
    public int fee;

    public String toString() {
        return String.format(
            "SmsSingleSenderResult\nresult %d\nerrMsg %s\next %s\nsid %s\nfee %d",
            result, errMsg, ext, sid, fee);
    }
    
}
