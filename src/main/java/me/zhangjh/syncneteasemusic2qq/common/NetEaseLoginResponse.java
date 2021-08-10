package me.zhangjh.syncneteasemusic2qq.common;

import lombok.Data;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Data
public class NetEaseLoginResponse {
    private Integer loginType;

    private Account account;

    private String token;

    private Profile profile;

    private String cookie;
}
