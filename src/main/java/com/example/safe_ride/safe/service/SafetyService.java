package com.example.safe_ride.safe.service;

import com.example.safe_ride.safe.SafetyRepository;
import com.example.safe_ride.safe.entity.SafetyDirection;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SafetyService {
    private final SafetyRepository safetyRepository;

    private static final String BASE_URL = "https://apis.data.go.kr/B552061/frequentzoneBicycle/getRestFrequentzoneBicycle";
    @Value("${public.api.key}")
    private String SERVICE_KEY;

    public SafetyService(SafetyRepository safetyRepository) {
        this.safetyRepository = safetyRepository;
    }

    // 자전거 사고다발지역
    public String buildApi(String siDo, String guGun) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);

        urlBuilder.append("?ServiceKey=").append(SERVICE_KEY);
        urlBuilder.append("&searchYearCd=").append(URLEncoder.encode("2022", "UTF-8"));
        urlBuilder.append("&siDo=").append(siDo);   // 폼에서 사용자가 입력 하는 것으로 변경
        urlBuilder.append("&guGun=").append(guGun); // 폼에서 사용자가 입력 하는 것으로 변경
//        siDo = "41"; // (11: 서울) - 폼에서 받을 사용자의 위치 정보로 변경
//        guGun = "150"; // (680: 강남구) - 폼에서 받을 사용자의 위치 정보로 변경
        urlBuilder.append("&type=").append(URLEncoder.encode("json", "UTF-8")); // 데이터 형식 (json)
        urlBuilder.append("&numOfRows=").append(URLEncoder.encode("10", "UTF-8")); // 한페이지당 데이터 수
        urlBuilder.append("&pageNo=").append(URLEncoder.encode("1", "UTF-8")); // page Num
        log.info("url확인: {}", urlBuilder);
        return urlBuilder.toString();
    }

    // http 응답받아 문자열로 변환
    // API에서 데이터를 가져오는 메소드
    public List<String> fetchDataFromApi(String siDo, String guGun) {
        try {
            String urlStr = buildApi(siDo, guGun);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 응답 문자열 반환
                return ParsingDate(response.toString());
            } else {
                System.out.println("GET 요청에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 자전거 사고다발지역 데이터 파싱
    public List<String> ParsingDate(String jsonResponse) throws JSONException {
        // JSON 응답 문자열 파싱
        JSONObject jsonObject = new JSONObject(jsonResponse);
        // 사고 정보 추출
        JSONArray items = jsonObject.getJSONObject("items").getJSONArray("item");

        List<String> results = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String bjdCd = item.getString("bjd_cd");  // 법정동 코드
            String spotNm = item.getString("spot_nm"); // 사고가 발생한 시도 및 시군구 명칭
            Integer occrrncCnt = item.getInt("occrrnc_cnt"); // 발생 건수
            Integer casltCnt = item.getInt("caslt_cnt"); // 사상자 수
            Integer dthDnvCnt = item.getInt("dth_dnv_cnt"); // 사망자 수
            Integer seDnvCnt = item.getInt("se_dnv_cnt"); // 중상자 수
            Integer slDnvCnt = item.getInt("sl_dnv_cnt"); // 경상자 수
            String geomJson = item.getString("geom_json"); // 사고 지점 지리적 위치 데이터(List형태)
            String loCrd = item.getString("lo_crd"); // 경도(lnt)
            String laCrd = item.getString("la_crd"); // 위도(lat)

            String result = String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                    bjdCd, spotNm, occrrncCnt, casltCnt, dthDnvCnt, seDnvCnt, slDnvCnt, geomJson, loCrd, laCrd);

            results.add(result);
        }

        return results;
    }

    // 법정동 코드만 추출하여 리스트로 저장
    public List<String> getBjDongCodes(String jsonResponse) throws JSONException {
        // 기존 ParsingDate 메서드를 호출하여 전체 데이터를 파싱
        List<String> allData = ParsingDate(jsonResponse);

        List<String> bjdCodes = new ArrayList<>();
        for (String data : allData) {
            // 데이터 문자열을 쉼표(,)로 분리
            String[] splitData = data.split(", ");
            // splitData[0]은 법정동 코드(bjdCd)를 포함하고 있음
            String bjdCd = splitData[0];
            // 법정동 코드만 별도의 리스트에 추가
            bjdCodes.add(bjdCd);
        }

        return bjdCodes;
    }

    // 리스트로 저장된 법정동 코드를 사용자위치의 법정동 코드와 하나씩 비교하여 일치하면 사고 다발 위치를 지도에 표시


}