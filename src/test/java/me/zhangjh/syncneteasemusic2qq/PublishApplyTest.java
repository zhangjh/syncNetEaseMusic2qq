package me.zhangjh.syncneteasemusic2qq;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author zhangjh
 * @date 2022/5/31
 * 与本项目无关，借助一下webdriver和httpclient的功能
 */
@SpringBootTest
public class PublishApplyTest {

    private static final CloseableHttpClient CLIENT = HttpClients.createDefault();

    private static final String COOKIES = "";

    private Map<String, String> init() {
        Map<String, String> appMaps = new HashMap<>();
        appMaps.put("raycloud_sheji_analytic_prod", "39563");
        appMaps.put("raycloud_sheji_analytic", "39233");

        appMaps.put("raycloud_sheji_schema_test1", "27708");
        appMaps.put("raycloud_sheji_schema_test3", "31053");
        appMaps.put("raycloud_sheji_schema_test5", "31054");
        appMaps.put("raycloud_sheji_schema_gray", "30365");
        appMaps.put("raycloud_sheji_schema_prod", "30388");

        appMaps.put("distribution_test1", "27604");
        appMaps.put("distribution_gray", "30380");
        appMaps.put("distribution_production", "32171");
        appMaps.put("distribution_private", "36531");
        appMaps.put("distribution_private_test2", "39521");

        appMaps.put("distribution_business_test", "35987");
        appMaps.put("distribution_business_test2", "39471");

        appMaps.put("raycloud_sheji_platform_ability_test", "36420");
        appMaps.put("raycloud_sheji_platform_ability_gray", "37242");
        appMaps.put("raycloud_sheji_platform_ability_prod", "37539");
        appMaps.put("raycloud_sheji_platform_ability_private", "39299");

        appMaps.put("raycloud_sheji_common_task_prod", "19146");
        appMaps.put("raycloud_sheji_common_task_gray", "18033");

        appMaps.put("infra_test5", "35593");
        appMaps.put("raycloud_sheji_infra_gray", "28106");
        appMaps.put("raycloud_sheji_infra_prod", "28107");

        appMaps.put("raycloud_sheji_pmdm_test3", "31061");
        appMaps.put("raycloud_sheji_pmdm_test5", "35548");
        appMaps.put("raycloud_sheji_pmdm_gray", "28016");
        appMaps.put("raycloud_sheji_pmdm_prod", "28015");

        appMaps.put("raycloud_sheji_davinci_test", "30306");
        appMaps.put("raycloud_sheji_davinci_test5", "35597");
        appMaps.put("raycloud_sheji_davinci_gray", "30418");
        appMaps.put("raycloud_sheji_davinci_prod", "30417");

        appMaps.put("raycloud_sheji_idpt_test5", "21434");
        appMaps.put("raycloud_sheji_idpt_gray", "18107");

        appMaps.put("raycloud_sheji_idpt_boss_test5", "21440");
        appMaps.put("raycloud_sheji_boss_gray", "18086");
        appMaps.put("raycloud_sheji_boss_production_k8s", "19213");

        appMaps.put("raycloud_sheji_material_test3", "32179");
        appMaps.put("raycloud_sheji_material_test5", "35552");
        appMaps.put("raycloud_sheji_material_gray", "23273");

        appMaps.put("raycloud_sheji_algorithm_gray", "18104");
        appMaps.put("raycloud_sheji_algorithm_production_k8s", "19065");

        return appMaps;
    }

    @SneakyThrows
    @Test
    public void doApplyByHttp() {
        Map<String, String> map = init();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String appId = entry.getValue();
            Map<String, Object> param = new HashMap<>();
            param.put("appId", appId);
            param.put("appType", "1");
            param.put("reason", "1");
            param.put("deployInfo", "开发内容测试");
            param.put("influenceScope", "pim主站和发布");
            param.put("remedialAction", "回滚");
            param.put("proofScheme", "回归测试");
            param.put("singleEffective", "0");
            param.put("appKey", new ArrayList<String>());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.SECOND, -1);
            param.put("period", String.valueOf(calendar.getTimeInMillis()));

            HttpPost httpPost = new HttpPost("https://console.cloud.tmall.com/approval/container/start");
            httpPost.addHeader("accept", "application/json, text/json");
            httpPost.addHeader("content-type", "application/json");
            httpPost.addHeader("cookie", COOKIES);
            httpPost.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            httpPost.addHeader("x-requested-with", "XMLHttpRequest");
            httpPost.addHeader("x-xsrf-token", "38da3d66-3f97-4713-98ce-12e52354468e");

            StringEntity entity = new StringEntity(JSON.toJSONString(param), StandardCharsets.UTF_8);
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = CLIENT.execute(httpPost);
            if(response.getStatusLine().getStatusCode() != 200) {
                System.out.println("申请失败" + param);
            }
            System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));

            Thread.sleep(2000);
        }

        JSONObject cookieJSON = new JSONObject();
        cookieJSON.put("cookie", COOKIES);
        HttpPost httpPost = new HttpPost("https://control.superboss.cc/apply/cookie?cookie=XSRF-TOKEN=38da3d66-3f97-4713-98ce" +
                "-12e52354468e;+cna=ogKDF1dZkBcCAbeGbtOXsdg2;+xlly_s=1;+dingtalk-login=tLdr5NIcXnIiE;+_dd_s=logs=1&id=55e4027f-7dad-43dc-aa66-60a8b186d006&created=1653988660008&expire=1653989712819;+l=eBPtqIsrQqSGO6rMBOfZhurza7799IRAguPzaNbMiOCPO31H5bTFB6X76SYMCnGVh67kJ3rVZW9zBeYBcCXna5XxOp5tPbHmn;+isg=BImJ5lubD62KtM8w2nFARofrmLPj1n0I-C-LWSv-NHCvcqmEcidM2aGstNZEKhVA;+tfstk=c53PButSIyDfb6aBXzaeN_oBvoqRZW3iJtPQqdkEWpth--ZliJBLi6X30l1VKuf..");

        httpPost.setEntity(new StringEntity(JSON.toJSONString(cookieJSON)));
        CloseableHttpResponse response = CLIENT.execute(httpPost);
        if(response.getStatusLine().getStatusCode() != 200) {
            System.out.println("cookie提交失败");
        }
    }
}
