- 프로젝트 기간: 2024.04.11 ~ 2024.05.03
- Team: Riders 🚴🏻

---


### 🎖️  팀원 소개
<br>
<div align="center"> 
<table>
    <tr>
        <td align="center">
            <b>김소은</b>
        </td>
        <td align="center">
            <b>박주윤</b>
        </td>
        <td align="center">
            <b>나유현</b>
        </td>
        <td align="center">
            <b>이서주</b>
        </td>
        <td align="center">
            <b>이영규</b>
        </td>
        <td align="center">
            <b>김규석</b>
        </td>
    </tr>
  <tr>
        <td align="center">
            <b>팀장</b>
            <br>
            - 안전한 자전거 길 안내 서비스 구현 <br>
            - MySQL 서버 구축 <br>
            - AWS 배포
        </td>
        <td align="center">
            <b>팀원</b>
            <br>
            - 커뮤니티 기능 구현 <br>
            - 라이딩 매칭 기능 구현
        </td>
        <td align="center">
            <b>팀원</b>
            <br>
            - 라이딩 기록 기능 구현 <br>
            - 커뮤니티 기능 구현 <br>
            - AWS 배포  
        </td>
        <td align="center">
           <b>팀원</b>
            <br>
            - 공영자전거 현환 기능 구현 <br>
            - 메인페이지 제작 <br>
            - 템플릿 수정 / 적용
        </td>
        <td align="center">
            <b>팀원</b>
            <br>
            - 회원가입 / 로그인 기능 구현 <br>
            - 사용자 계정 관리 기능 구현
        </td>
        <td align="center">
           <b>팀원</b>
            <br>
            - 실시간 날씨 기능 구현
        </td>
    </tr>
</table>
</div>
<br>

---

### ✨ 프로젝트 소개


- **세이프 라이드 (Safe-Ride)**
  - 안전한 라이딩을 위한 커뮤니티 서비스
- 지역별 공영 자전거 이용 정보와 안전한 라이딩을 위한 다양한 서비스를 제공하는 자전거 커뮤니티 및 건강 관리 플랫폼


### 👩🏻‍💻 기술 스택 

#### &nbsp;　[ DB ]
&nbsp;&nbsp;&nbsp; <img src="https://img.shields.io/badge/SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/AWS-569A31?style=flat-square&logo=amazon s3&logoColor=white"/>


#### &nbsp;　[ Backend ]

&nbsp;&nbsp;&nbsp; <img src="https://img.shields.io/badge/Java 17-FF160B?style=flat-square&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white&color=darkgreen"/>
<img src="https://img.shields.io/badge/JPA-088142?style=flat-square&logo=jpa&logoColor=white"/>
<img src="https://img.shields.io/badge/Querydsl-black?style=flat-square&logo=querydsl&logoColor=white"/>


#### &nbsp;　[ Frontend ]

&nbsp;&nbsp;&nbsp;&nbsp; <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white">
<img src="https://img.shields.io/badge/CSS-1572B6?style=flat&logo=CSS3&logoColor=white&color=darkblue">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=black">
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white"/>
<img src="https://img.shields.io/badge/BootStrap-7952B3?style=flat-square&logo=bootstrap&logoColor=white"/>
<img src="https://img.shields.io/badge/jQuery-0769AD?style=flat-square&logo=jquery&logoColor=white"/>


#### &nbsp;　[ Etc... ]

&nbsp;&nbsp;&nbsp;&nbsp; <img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white"/>
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/>






### 📝 프로젝트 설계 및 기획

<details>
<summary><strong>ERD</strong></summary>

![ERD](https://github.com/soeunc/safe_ride/blob/main/Weekly_Log/assets/ERD%2B.png)


</details>
<br>

<details>
<summary><strong>화면 기획</strong></summary>

![event](https://github.com/soeunc/safe_ride/blob/main/Weekly_Log/assets/%ED%99%94%EB%A9%B4%EA%B8%B0%ED%9A%8D.png)

</details>

<br>

<details>
<summary><strong>구조도</strong></summary>

![event](https://github.com/soeunc/safe_ride/blob/main/Weekly_Log/assets/%EA%B5%AC%EC%A1%B0%EB%8F%84.png)

</details>


---

### 🌼 기능 설명

#### 👤 사용자 계정 관리
- 회원가입 및 로그인
- 사용자 정보 수정, 삭제
- 마이페이지 '오늘의 기록' 입력 
- 그래프를 활용한 라이딩 기록 시각화

#### 🌤 날씨 정보 안내
- 공공 데이터 기상청 Open API를 사용
- 한국환경공단 에어코리아 Open API 사용
- UI를 통한 날씨 및 미세먼지 정보 제공

#### ️🚲 공영 자전거 사용 정보 안내
- 주소 / 현재 위치 기반 검색
- 주소 팝업 Open API, 공공데이터 Open API 사용
- 공영 대여소 위치 및 상세 정보 제공
- 대여소 별 자전거 보유 수량 정보 제공

#### 🚩 안전한 자전거 길 안내
- 공공데이터 Open API 사용
- 사용자 탐색 위치 검색
- 근처 위험 구역(자전거, 스쿨존 사고다발지역) 정보 제공

#### 🗣️ 자전거 이용자 커뮤니티
- 게시판을 통한 커뮤니티 공간 제공
- 지역별 검색 필터 기능 제공
- 게시글 내부 댓글 기능 제공

#### ️👊 라이딩 매칭
- 지역 라이더 매칭 신청
- 매칭 신청글 작성
- 해당 매칭글에 매칭 신청
- 매칭글 작성자는 신청자 목록을 확인 후 수락 및 거절 가능
- 라이딩 종료 후 매칭 매너 리뷰 작성 

#### 📝 라이딩 기록
- 목적지 목록 및 지도 제공
- 실시간 사용자 자전거 이용 기록 수집
- 당일 라이딩 기록에 수집 정보 누적
