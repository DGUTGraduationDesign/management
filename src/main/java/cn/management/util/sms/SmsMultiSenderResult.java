package cn.management.util.sms;

import java.util.ArrayList;

/**
 * 短信群发结果数据模型，来自腾讯云短信接口示例
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class SmsMultiSenderResult {
    
    public class Detail {
        
        public int result;
        
        public String errMsg = "";
        
        public String phoneNumber = "";
        
        public String nationCode = "";
        
        public String sid = "";
        
        public int fee;

        public String toString() {
            if (0 == result) {
                return String.format(
                    "Detail result %d\nerrMsg %s\nphoneNumber %s\nnationCode %s\nsid %s\nfee %d",
                    result, errMsg, phoneNumber, nationCode, sid, fee);
            } else {
                return String.format(
                    "result %d\nerrMsg %s\nphoneNumber %s\nnationCode %s",
                    result, errMsg, phoneNumber, nationCode);
            }
        }
    }

    public int result;
    
    public String errMsg = "";
    
    public String ext = "";
    
    public ArrayList<Detail> details;

    public String toString() {
        return String.format(
            "SmsMultiSenderResult\nresult %d\nerrMsg %s\next %s\ndetails %s",
            result, errMsg, ext, details);
    }
    
}
