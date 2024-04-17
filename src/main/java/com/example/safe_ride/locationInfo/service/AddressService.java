package com.example.safe_ride.locationInfo.service;

import com.example.safe_ride.locationInfo.entity.LocationInfo;
import com.example.safe_ride.locationInfo.repo.LocationInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final LocationInfoRepo locationInfoRepo;

    // 전체 데이터 중에 시도 데이터 불러오기 (폼 제출용)
    public List<String> getSido() {
        return locationInfoRepo.findAll().stream()
                .map(LocationInfo::getSido)
                .distinct()
                .collect(Collectors.toList());
    }
    // 시도 중에 시군구 데이터 불러오기 (폼 제출용)
    public List<String> getSigunguBySido(String sido) {
        return locationInfoRepo.findAllBySido(sido).stream()
                .map(LocationInfo::getSigungu)
                .distinct()
                .collect(Collectors.toList());
    }
    // 시군구 중에 읍면동 데이터 불러오기 (폼 제출용)
    public List<String> getEupmyundongBySigungu(String sigungu) {
        return locationInfoRepo.findAllBySigungu(sigungu).stream()
                .map(LocationInfo::getEupmyundong)
                .distinct()
                .collect(Collectors.toList());
    }

    // 제출된 폼의 지역 코드 추출 (API 인자로 써야함)
    public String getAddressCode(String sido) {
        List<LocationInfo> locations = locationInfoRepo.findBySido(sido);
        if (!locations.isEmpty()) {
            return locations.get(0).getAddressCode();
        }
        return null;  // 결과가 없으면 null 반환
    }

    // 제출된 폼의 지역 코드 추출 (API 인자로 써야함)


    // 제출된 시도, 시군구, 읍면동 데이터로 해당하는 지역의 데이터 표현하기
    // 1. API 데이터 중 소재지지번주소 (lotnoAddr) 와 일치하는 데이터를 추출하는 쿼리 작성
    // 2. 대여소 정보 데이터를 추출
    // 3. 추출된 대여소의 ID(rntstnId)와 일치하는 데이터를 추출하는 쿼리 작성
    // 4. 대여소 자전거 정보 데이터를 추출
}