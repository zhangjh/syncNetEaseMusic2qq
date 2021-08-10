package me.zhangjh.syncneteasemusic2qq.common;

import lombok.Data;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Data
public class Account {
    private Long id;

    private String userName;

    private Integer type;

    private Integer status;

    private Integer whitelistAuthority;

    private Long createTime;
}
