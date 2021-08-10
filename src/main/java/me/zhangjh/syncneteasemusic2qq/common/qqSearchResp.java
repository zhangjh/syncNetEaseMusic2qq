package me.zhangjh.syncneteasemusic2qq.common;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Data
public class qqSearchResp {
    @Data
    static class SongContent {
        private String url;
    }

    @Data
    static class Song {
        private List<SongContent> list;
    }

    @Data
    static class SearchData {
        private String keyword;

        private Song song;
    }

    @Data
    static class SearchDataRes {
        private SearchData data;
    }

    private SearchDataRes response;
}
