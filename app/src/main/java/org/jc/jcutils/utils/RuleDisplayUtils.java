package org.jc.jcutils.utils;

import org.greenrobot.eventbus.EventBus;
import org.jc.jcutils.javabean.Rule65_3Result;
import org.jc.jcutils.javabean.Rule65_4Result;
import org.jc.jcutils.javabean.Rule65_5Result;

/**
 * 协议处理区域
 * 数据源的前俩位为 协议位
 * Created by a3552 on 2017/4/19.
 */

public class RuleDisplayUtils {

    /**
     * 处理接收信息
     * @param ip 发送者 ip
     * @param port 发送者 port
     * @param data 发送信息体
     */
    public static void display(byte[] ip, int port, byte[] data){
        if (isRule(data, Rule65_3Result.rule)){
            Rule65_3Result.run();
        }else if (isRule(data, Rule65_4Result.rule)){
            Rule65_4Result.run();
        }else if (isRule(data, Rule65_5Result.rule)){
            Rule65_5Result.run();
        }
    }

    /**
     * 协议头判断 协议号判断
     * @param src 数据源
     * @param rule 规则
     * @return 结果
     */
    public static boolean isRule(byte[] src, byte[] rule){
        if (src != null && src.length >= 2){
            return (src[0]==rule[0] && src[1]==rule[1]);
        }else {
            return false;
        }
    }
}
