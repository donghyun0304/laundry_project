package aug.laundry.service;

import aug.laundry.dao.subscribe.SubscribeDao;
import aug.laundry.dto.SubscriptionPayDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeServiceImpl_ksh implements SubscribeService_ksh{
    private final SubscribeDao subscribeDao;
    @Transactional
    @Override
    public int saveSubscribe(SubscriptionPayDto subData) {
        // 결제정보 DB저장
        int res = subscribeDao.insertJoinSubscribe(subData);
        res += subscribeDao.updateMemberSubscribe(subData.getSelectMonth(), subData.getMemberId());

        return res;
    }

    @Override
    public String getAccessToken() {
        try {
            // access_token 요청해서 받아오기
            String requestUrl = "https://api.iamport.kr/users/getToken";
            String jsonBody = "{\"imp_key\": \"imp_key\", \"imp_secret\": \"imp_secret\"}";
            StringEntity json = new StringEntity(jsonBody, "UTF-8");
            json.setContentType("application/json");

            CloseableHttpClient httpClient = HttpClients.createDefault(); // http 요청하기위한 객체 생성
            HttpPost postRequest = new HttpPost(requestUrl);
            postRequest.setEntity(json);

            // 요청 후 데이터 받기
            HttpResponse response = httpClient.execute(postRequest);
            HttpEntity getEntity = response.getEntity();

            // 응답데이터를 String으로 변환
            String responseEntity = EntityUtils.toString(getEntity);

            // responseEntity데이터를 JsonObject객체로 파싱
            JsonObject jsonObject = new Gson().fromJson(responseEntity, JsonObject.class);

            // accessToken추출
            String accessToken = jsonObject.get("response").getAsJsonObject().get("access_token").getAsString();
            httpClient.close();

            return accessToken;

        } catch (IOException e) {
            throw new RuntimeException("Unable to get access token",e);
        }
    }
}
