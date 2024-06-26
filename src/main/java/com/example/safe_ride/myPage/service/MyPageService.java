package com.example.safe_ride.myPage.service;

import com.example.safe_ride.member.dto.BadgeDto;
import com.example.safe_ride.member.entity.Badge;
import com.example.safe_ride.member.entity.Grade;
import com.example.safe_ride.member.entity.Member;
import com.example.safe_ride.member.repo.BadgeRepo;
import com.example.safe_ride.member.repo.MemberRepo;
import com.example.safe_ride.myPage.dto.MyPageDto;
import com.example.safe_ride.myPage.dto.WeeklyRecordDto;
import com.example.safe_ride.myPage.entity.MyPage;
import com.example.safe_ride.myPage.repo.MyPageRepo;
import com.example.safe_ride.myPage.repo.MyPageRepoDsl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepo myPageRepo;
    private final MyPageRepoDsl myPageRepoDsl;
    private final BadgeRepo badgeRepo;
    private final MemberRepo memberRepo;

    //라이딩 정보 가져오기
    @Transactional
    public MyPageDto readRidingRecord(Long memberId){
        MyPage myPage;
        MyPageDto myPageDto = new MyPageDto();
        //memberId로 조회 시 데이터가 하나라도 있는가
        boolean isExist = myPageRepo.existsByMemberId(memberId);

        //이전 정보가 존재한다면
        if (isExist) {
            //오늘자 데이터가 있는지 확인
            Optional<MyPage> optionalMyPage =
                    myPageRepo.findByMemberIdAndCreateDateAfter(
                        memberId, LocalDate.now().atStartOfDay()
                    );
            if (optionalMyPage.isPresent()) {
                myPage = optionalMyPage.get();
                myPageDto = MyPageDto.fromEntity(myPage);
            }
//            //전체기록합산
//            myPageDto.setTotalRecord(findTotalRecord(memberId));
//            //주간기록합산
//            myPageDto.setWeeklyRecord(findWeeklyRecord(memberId));
//            //주간 개별 기록(날짜, 기록)
//            myPageDto.setWeeklyRecordList(weeklyRecordListTotal(getThisWeekDayList(),findWeeklyRecordList(memberId)));
        } else {
            //이번주 날짜(일~토)
            List<String> thisWeek = getThisWeekDayList();
            List<WeeklyRecordDto> weeklyRecordDtoList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            for (int i = 0; i < thisWeek.size(); i++) {
                WeeklyRecordDto dto = new WeeklyRecordDto();
                String oneDay = thisWeek.get(i) + " 00:00:00.000";
                LocalDateTime localDateTime = LocalDateTime.parse(oneDay, formatter);
                dto.setTodayRecord(0);
                dto.setCreateDate(localDateTime);
                weeklyRecordDtoList.add(dto);
            }
            myPageDto.setWeeklyRecordList(weeklyRecordDtoList);
        }
        //전체기록합산
        myPageDto.setTotalRecord(findTotalRecord(memberId));
        //주간기록합산
        myPageDto.setWeeklyRecord(findWeeklyRecord(memberId));
        //주간 개별 기록(날짜, 기록)
        myPageDto.setWeeklyRecordList(weeklyRecordListTotal(getThisWeekDayList(),findWeeklyRecordList(memberId)));

        return myPageDto;
    }

    //주단위 기록
    public List<String> getThisWeekDayList(){
        List<String> thisWeekDayList = new ArrayList<>();
        //오늘날짜
        LocalDate today = LocalDate.now();
        int day = today.get(ChronoField.DAY_OF_WEEK);
        if (day == 7) day = 0;
        //이번주 시작일자(일요일)
        LocalDate start = today.minusDays(day);
        //이번주 끝일자
        LocalDate end = start.plusDays(6);
        for(int i = 0; i < 7; i++) {
            thisWeekDayList.add(start.plusDays(i).toString());
        }
        return thisWeekDayList;
    }

    //오늘 주행기록 입력
    @Transactional
    public void createToday(Long memberId, int todayRecord){
        Optional<MyPage> optionalMyPage =
                myPageRepo.findByMemberIdAndCreateDateAfter(
                        memberId, LocalDate.now().atStartOfDay()
                );
        //오늘 주행기록이 이미 존재한다면
        if (optionalMyPage.isPresent()){
            MyPage myPage = optionalMyPage.get();
            //더해주기
            myPage.setTodayRecord(myPage.getTodayRecord() + todayRecord);
        } 
        //오늘 주행기록이 없다면
        else {
            Member member = memberRepo.findById(memberId)
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
                    );
            //새로운 주행기록 만들기
            MyPage myPage = MyPage.builder()
                    .member(member)
                    .todayRecord(todayRecord)
                    .createDate(LocalDateTime.now())
                    .build();
            myPageRepo.save(myPage);
        }
    }
    //주간기록합산결과
    public int findWeeklyRecord(Long memberId){
        int weeklyRecord = 0;
        LocalDateTime today = LocalDate.now().atStartOfDay();
        int day = today.get(ChronoField.DAY_OF_WEEK);
        if (day == 7) day = 0;
        //이번주 시작일자(일요일)
        LocalDateTime start = today.minusDays(day);
        //이번주 끝일자
        LocalDateTime end = start.plusDays(6);

        //dsl로 sum해서 가져옴
        try {
            weeklyRecord = myPageRepoDsl.getTotalWeeklyRecord(memberId, start, end);
        } catch (NullPointerException npe){
            log.info("주간 기록 합산 결과 error : {}", npe.getMessage());
            weeklyRecord = 0;
        }

        return weeklyRecord;
        
        
    }
    //주간개별기록
    public List<WeeklyRecordDto> findWeeklyRecordList(Long memberId){
        List<WeeklyRecordDto> weeklyRecordList = new ArrayList<>();
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDateTime.of(today.getYear(),today.getMonth(), today.getDayOfMonth(), 23,59,59);
        int day = today.get(ChronoField.DAY_OF_WEEK);
        if (day == 7) day = 0;
        //이번주 시작일자(일요일 00:00:00)
        LocalDateTime start = today.minusDays(day);
        //이번주 끝일자(토요일 23:59:59)
        LocalDateTime endTemp = todayEnd.minusDays(day);
        LocalDateTime end = endTemp.plusDays(6);
        weeklyRecordList = myPageRepoDsl.getWeeklyRecordList(memberId,start,end);
        return weeklyRecordList;
    }
    //이번주 라이딩 기록 총합버전
    //이번주 날짜와 이번주 기록을 받아 주단위 기록을 생성
    //특정 날짜 기록이 없다면 0으로 변경
    public List<WeeklyRecordDto> weeklyRecordListTotal(
            List<String> thisWeekDayList,
            List<WeeklyRecordDto> weeklyRecordList
    ){
        List<WeeklyRecordDto> weeklyRecordDtoList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTimeFormatter dayEqualCk = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //날짜비교.
        for (int i = 0; i < thisWeekDayList.size(); i++) {
            WeeklyRecordDto dto = new WeeklyRecordDto();
            LocalDateTime dateTime;

            // DB에서 받아온 데이터list가 비어서 더이상 2중for문을 돌지않을때
            // 그냥 빈 값 만들어준다
            if(weeklyRecordList.isEmpty()){
                //해당 날짜와 기록을 dto에 저장
                //parsing을 위한 변경
                String oneDay = thisWeekDayList.get(i) + " 00:00:00.000";
                dateTime = LocalDateTime.parse(oneDay, formatter);
                dto.setCreateDate(dateTime);
                dto.setTodayRecord(0);
            }

            //해당 하는 날짜가 있고 DB에서 받아온 데이터list가 비어있지 않을 때
            for (int j = 0; j < weeklyRecordList.size(); j++) {
                if (
                        thisWeekDayList.get(i).equals(
                                weeklyRecordList.get(j).getCreateDate().format(dayEqualCk)
                        )
                ){
                    //해당 날짜와 기록을 dto에 저장(DB값 그대로)
                    dto.setCreateDate(weeklyRecordList.get(j).getCreateDate());
                    dto.setTodayRecord(weeklyRecordList.get(j).getTodayRecord());
                    //저장 후 list에서 해당 데이터 삭제
                    weeklyRecordList.remove(j);

                    break;
                } else {
                    //해당 날짜와 기록을 dto에 저장
                    //parsing을 위한 변경
                    String oneDay = thisWeekDayList.get(i) + " 00:00:00.000";
                    dateTime = LocalDateTime.parse(oneDay, formatter);
                    dto.setCreateDate(dateTime);
                    dto.setTodayRecord(0);

                    break;
                }
            }
            weeklyRecordDtoList.add(dto);
        }
        return weeklyRecordDtoList;
    }

    //전체기록합산결과
    public int findTotalRecord(Long memberId){
        int totalRecord = 0;
        try {
            totalRecord = myPageRepoDsl.getTotalRecord(memberId);
        } catch (NullPointerException npe){
            log.info("전체 기록 합산 결과 error : {}", npe.getMessage());
        }
        return totalRecord;
    }
    
    //뱃지 가져오기
    @Transactional
    public BadgeDto readBadges(Long memberId, int totalRecord) {
        Optional<Badge> optionalBadge = badgeRepo.findByMemberId(memberId);
        BadgeDto badgeDto = new BadgeDto();
        if (optionalBadge.isPresent()) {
            Badge badge = optionalBadge.get();
            //전체기록을 확인하여 뱃지 grade update(가능하다면)
            upgradeBadgeGrade(badge, totalRecord);
            badgeDto = BadgeDto.fromEntity(badge);
        } else {
            return badgeDto;
        }
        return badgeDto;
    }

    //등급뱃지 업그레이드 가능 여부 체크
    //0~99km : 킥보드
    //100~499km : 자전거
    //500~999km : 스쿠터
    //1000km ~ : 오토바이
    //기존 뱃지 grade와 동일하다면 pass
    @Transactional
    public void upgradeBadgeGrade(
            Badge badge,
            int totalRecord
    ) {
        //전체기록에 따른 뱃지 grade 저장용 변수
        Grade newGrade;
        if (totalRecord >= 1000000) {
            newGrade = Grade.MOTOR_CYCLE;
        } else if (totalRecord >= 500000) {
            newGrade = Grade.SCOOTER;
        } else if (totalRecord >= 100000) {
            newGrade = Grade.BYCICLE;
        } else {
            newGrade = Grade.QUICK_BOARD;
        }
        //현재 DB의 뱃지의 grade와
        // 전체기록에 따른 뱃지의 grade가 다르다면 upgrade
        if (!badge.getGrade().equals(newGrade)) {
            badge.setGrade(newGrade);
        }

    }


}
