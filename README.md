Read-Nextday

## 프로젝트 설명

### 기술스택
 <a href="https://spring.io/">
  <img src="https://img.shields.io/badge/Spring-Framework-success?logo=spring" alt="Spring">
</a>
<a href="https://spring.io/projects/spring-security">
  <img src="https://img.shields.io/badge/Spring%20Security-Security-blue?logo=spring-security" alt="Spring Security">
</a>
<a href="https://spring.io/projects/spring-boot">
  <img src="https://img.shields.io/badge/Spring%20Boot-Boot-blue?logo=spring-boot" alt="Spring Boot">
</a>
<a href="https://www.mysql.com/">
  <img src="https://img.shields.io/badge/MySQL-Database-blue?logo=mysql" alt="MySQL">
</a>
<a href="https://redis.io/">
  <img src="https://img.shields.io/badge/Redis-Cache-red?logo=redis" alt="Redis">
</a>
<a href="https://slack.com/">
  <img src="https://img.shields.io/badge/Slack-Chat-4A154B?logo=slack" alt="Slack">
</a>

### :calendar: 기간
2023-10-07~

### :couple: 팀 구성
- Backend : 고예진

### 서비스 설명
블로그 링크, 문서들을 업로드하고 알람 기능을 통해 읽어야 하는 문서들을 다시 리마인드 시켜주는 웹 서비스를 제공한다. 

1. 블로그 관리: 사용자는 자신의 블로그 게시물을 업로드하고 보관할 수 있으며, 카테고리 구분을 통해 쉽게 찾고 관리할 수 있다.

2. 문서 저장: PDF 파일을 저장하고, PDF의 텍스트를 추출하여 게시물을 작성할 수 있다. 

3. 알림 설정을 통한 리마인드 : 사용자가 읽어야할 게시물을 알람을 통해 제공하고, 한번도 방문하지 않는 게시물에 대한 알람을 제공한다.

### 기능 설명
- 로그인 : Access과 Refresh 토큰 사용한다.
- 카테고리 CRUD : 게시글을 그룹화하여 관리한다.
- 게시글 CRUD : 게시글은 url 또는 PDF 파일로 저장한다. PDF파일의 경우, 글자를 추출하여 추출된 글자를 저장한다.
- 북마크 CRUD :  자신의 게시글을 북마크하여, 한곳에서 볼 수 있다.
- 해시태그 CRUD : 게시글에 해시태그를 달 수 있으며, 검색을 통해 게시글을 찾을 수 있다.
- 알림 
  - 슬랙 게시글 알람(유저용) : 게시글에 대한 알림을 설정하여 슬랙에서 알림을 표시해준다.
  - 슬랙 알람(개발자용) : 게시글 생성 및 에러에 대한 내용을 슬랙으로 알려준다.
 
### 주 구현 기능 설명
https://dandy-gasoline-075.notion.site/03afa50bf78d4fd9ac11dea04c23d695?pvs=4
 
### ERD
<img width="1252" alt="image" src="https://github.com/YEJINGO/Read-Nextday/assets/114003526/689e8b99-49ec-4739-aa46-8eba00e7f222">
