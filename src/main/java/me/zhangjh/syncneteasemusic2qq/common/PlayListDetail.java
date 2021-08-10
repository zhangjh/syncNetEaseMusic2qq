package me.zhangjh.syncneteasemusic2qq.common;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Data
public class PlayListDetail {
    @Data
    public static class Track {
        private String name;
        private Long id;
    }

    @Data
    public static class PlayTrack {
        private List<Track> tracks;
    }

    private List<PlayTrack> playlist;
}
