package aug.laundry.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Random;

@Service
public class SendSmsService_kgw {
    private static String projectId = "ncp:sms:kr:313691656496:laundry";
    private static String accessKey = "CpNSnGt9McyS2DoiuZFU";
    private static String secretKey = "hI0EFu7sLiITJBnhgrQnXG3GTro4FctHIIhqeX0g";

    private static String url = "/sms/v2/services/"+projectId+"/messages";
    private static String requestUrl = "https://sens.apigw.ntruss.com"+url;

    private static String timestamp = Long.toString(System.currentTimeMillis());
    private static String method = "POST";


    public String sendMassage(String phonenumber) throws Exception {
        String number = makeNumber();

        JSONObject bodyJson = new JSONObject();
        JSONObject toJson = new JSONObject();
        JSONArray toArr = new JSONArray();
        toJson.put("subject" , "[laundry]");
        toJson.put("content" , "인증번호는 " + number + " 입니다.");
        toJson.put("to" , phonenumber);
        toArr.put(toJson);

        bodyJson.put("type" , "SMS");
        bodyJson.put("contentType" , "COMM");
        bodyJson.put("countryCode" , "82");
        bodyJson.put("from" , "01081208867");
        bodyJson.put("subject" , "인증번호는 " + number + " 입니다.");
        bodyJson.put("content" , number);
        bodyJson.put("messages" , toArr );
        String body = bodyJson.toString();
        String result2 = doPost(requestUrl , body);

        return number;
    }

    // OkHttp 통신
    public static String doPost(String requestURL , String jsonMessage) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("x-ncp-apigw-timestamp", timestamp )
                .addHeader("x-ncp-iam-access-key", accessKey )
                .addHeader("x-ncp-apigw-signature-v2", makeSignature() )
                .url(requestURL)
                .post(RequestBody.create(MediaType.parse("application/json"), jsonMessage))
                .build();
        System.out.println("doPost");

        Response response = client.newCall(request).execute();
        //출력
        String message = response.body().string();
        return message;
    };

    // Signature생성

    public static String makeSignature() throws Exception {

        String space = " ";        // one space
        String newLine = "\n";    // new line

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        String encodeBase64String = null;
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        return encodeBase64String;
    }

    public static String makeNumber(){
        Random random = new Random();      //랜덤 함수 선언
        int createNum = 0;           //1자리 난수
        String ranNum = "";          //1자리 난수 형변환 변수
        int letter    = 6;         //난수 자릿수:6
        String resultNum = "";        //결과 난수

        for (int i=0; i<letter; i++) {
            createNum = random.nextInt(9);      //0부터 9까지 올 수 있는 1자리 난수 생성
            ranNum =  Integer.toString(createNum);  //1자리 난수를 String으로 형변환
            resultNum += ranNum;         //생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
        }

        return resultNum;
    }

}
