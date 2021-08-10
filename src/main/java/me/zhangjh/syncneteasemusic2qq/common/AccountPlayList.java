package me.zhangjh.syncneteasemusic2qq.common;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Data
public class AccountPlayList {

    @Data
    public static class PlayList {
        private String name;

        private Long id;
    }

    private Integer more;

    private List<PlayList> playList;
}
