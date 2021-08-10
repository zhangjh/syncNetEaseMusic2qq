package me.zhangjh.syncneteasemusic2qq.common;

import lombok.Data;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Data
public class NetEaseLoginParam {
    private String phone;

    private String countrycode = "86";

    private String password;

    private String rememberLogin = "true";
}
