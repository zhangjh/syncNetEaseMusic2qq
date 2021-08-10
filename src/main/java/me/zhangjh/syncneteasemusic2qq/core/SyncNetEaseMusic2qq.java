package me.zhangjh.syncneteasemusic2qq.core;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import me.zhangjh.syncneteasemusic2qq.common.AccountPlayList;
import me.zhangjh.syncneteasemusic2qq.common.NetEaseLoginResponse;
import me.zhangjh.syncneteasemusic2qq.common.PlayListDetail;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangjh
 * @date 2021/8/10
 */
@Component
public class SyncNetEaseMusic2qq {

    @Value("${netease.account.phone}")
    private String netEaseAccountPhone;

    @Value("${netease.account.password}")
    private String netEaseAccountPassword;

    @Value("${qq.cookies}")
    private String qqCookies;

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    private static final CloseableHttpClient CLIENT = HttpClients.createDefault();

    /** 网易云音乐api部署环境 */
    private static final String URL_PRE = "http://zhangjh.me:3001";
    private static final String LOGIN_URL_PRE = URL_PRE + "/login/cellphone";
    private static final String PLAYLIST_URL_PRE = URL_PRE + "/user/playlist";
    public static final String PLAYLIST_DETAIL_URL_PRE = URL_PRE + "/playlist/detail";

    private static final boolean SET_COOKIE_FLAG = false;

    @PostConstruct
    @SneakyThrows
    public void sync() {
        String md5Hex = DigestUtils.md5Hex(netEaseAccountPassword);

        NetEaseLoginResponse loginResponse = doLogin();
        Long userId = loginResponse.getAccount().getId();
        String cookie = loginResponse.getCookie();
        RequestConfig config = RequestConfig.custom().setCookieSpec(URLEncoder.encode(cookie, "utf-8")).build();

        // 获取用户歌单
        Long playListId = getUserPlayList(userId);

        // 获取歌单详情
        List<String> playlistDetail = getPlaylistDetail(playListId, config);

        // 收藏qq音乐
        favorite(playlistDetail);
    }

    @SneakyThrows
    private NetEaseLoginResponse doLogin() {
        HttpGet httpGet = new HttpGet(LOGIN_URL_PRE + "?phone=" + netEaseAccountPhone + "&password=" + netEaseAccountPassword);
        CloseableHttpResponse response = CLIENT.execute(httpGet);
        Validate.isTrue(response.getStatusLine().getStatusCode() == 200, "登录失败:" + response.getStatusLine().getReasonPhrase());

        HttpEntity entity = response.getEntity();
        return JSONObject.parseObject(EntityUtils.toString(entity), NetEaseLoginResponse.class);
    }

    @SneakyThrows
    private Long getUserPlayList(Long userId) {
        HttpGet httpGet = new HttpGet(PLAYLIST_URL_PRE + "?uid=" + userId + "&limit=1");
        CloseableHttpResponse playlistResponse = CLIENT.execute(httpGet);
        Validate.isTrue(playlistResponse.getStatusLine().getStatusCode() == 200,
                "获取用户歌单失败：" + playlistResponse.getStatusLine().getReasonPhrase());
        AccountPlayList accountPlayList = JSONObject.parseObject(EntityUtils.toString(playlistResponse.getEntity()), AccountPlayList.class);
        List<AccountPlayList.PlayList> playList = accountPlayList.getPlayList();
        Validate.isTrue(!playList.isEmpty(), "用户没有歌单");
        return playList.get(0).getId();
    }

    private List<String> getPlaylistDetail(Long playListId, RequestConfig config) throws Exception {
        List<String> songList = new ArrayList<>();
        HttpGet httpGet = new HttpGet(PLAYLIST_DETAIL_URL_PRE + "?id=" + playListId + "&cookie=" + config.getCookieSpec());
        CloseableHttpResponse playListDetailResp = CLIENT.execute(httpGet);
        Validate.isTrue(playListDetailResp.getStatusLine().getStatusCode() == 200,
                "获取歌单详情失败：" + playListDetailResp.getStatusLine().getReasonPhrase());
        PlayListDetail playListDetail = JSONObject.parseObject(EntityUtils.toString(playListDetailResp.getEntity()), PlayListDetail.class);
        List<PlayListDetail.PlayTrack> playTrackList = playListDetail.getPlaylist();
        Validate.isTrue(!playTrackList.isEmpty(), "歌单没有歌曲");
        List<PlayListDetail.Track> tracks = playTrackList.get(0).getTracks();
        for (PlayListDetail.Track track : tracks) {
            String name = track.getName();
            songList.add(name);
        }
        return songList;
    }

    @SneakyThrows
    private void favorite(List<String> songList) {
        // 需要首先安装chromedriver，下载后解压即可
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://y.qq.com/n/ryqq/search");
        // 种cookies免登
        if(!SET_COOKIE_FLAG) {
            String[] cookies = qqCookies.split(";");
            for (String cookie : cookies) {
                String[] split = cookie.split("=");
                if(split.length != 2) {
                    continue;
                }
                String key = split[0];
                String value = split[1];
                // 注意不能有空格
                driver.manage().addCookie(new Cookie(key.trim(), value.trim(), ".qq.com", "/", new Date(System.currentTimeMillis() + 3600 * 1000)));
            }
        }
            for (String song : songList) {
                String url = "https://y.qq.com/n/ryqq/search?w=" + URLEncoder.encode(song, "UTF-8") +  "&t=song";
                driver.get(url);

                Actions actions = new Actions(driver);
                actions.sendKeys(Keys.DOWN).sendKeys(Keys.DOWN).perform();
                actions.sendKeys(Keys.DOWN).sendKeys(Keys.DOWN).perform();
                Thread.sleep(2000);

                // 选择器
                String firstSongSelector = "#app div.mod_songlist > ul.songlist__list > li:nth-child(1) div.songlist__time";
                driver.findElement(By.cssSelector(firstSongSelector)).click();

                Thread.sleep(2000);
                String addBtnSelector = "#app div.mod_songlist > ul.songlist__list > li:nth-child(1) div.songlist__songname a.list_menu__item.list_menu__add";
                driver.findElement(By.cssSelector(addBtnSelector)).click();
                Thread.sleep(2000);

                String add2FavoriteSelector = "#fav_pop ul > li > a.operate_menu__link";
                driver.findElement(By.cssSelector(add2FavoriteSelector)).click();
            }
        } finally {
            driver.quit();
        }
    }
}
