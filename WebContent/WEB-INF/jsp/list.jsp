<%@page import="mybatis.vo.BbsVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%--
1) http://tomcat.apache.org/ 로 접속

   왼쪽 메뉴들 중 Taglibs라는 항목 선택!

    화면 가운데 쯤 [Download]선택!

2) 화면 상단 [Apache Taglibs Downloads] 파트에서
   [Apache Standard Taglib]라는 부분의 링크를 선택한다.
   그리고 버전을 [Standard 1.1]의 [download]를 클릭한다.

3) 나타나는 화면에서 [binaries/] 선택! 그리고 나타나는
   목록에서 밑에서 6번째인
   [jakarta-taglibs-standard-1.1.2.zip]을 받아 압축해제한다.

4) 압축해제한 폴더에 들어가 보면 lib폴더가 있고
   그 폴더 안에 있는 standart.jar와 jstl.jar파일이 필요한
   라이브러리다. 이것을 
   해당 {프로젝트}/WebContent/WEB-INF/lib에 
   복사해 넣는다.

 --%>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style type="text/css">
	#bbs table {
	    width:580px;
	    margin-left:10px;
	    border:1px solid black;
	    border-collapse:collapse;
	    font-size:14px;
	    
	}
	
	#bbs table caption {
	    font-size:20px;
	    font-weight:bold;
	    margin-bottom:10px;
	}
	
	#bbs table th,#bbs table td {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	.no {width:15%}
	.subject {width:30%}
	.writer {width:20%}
	.reg {width:20%}
	.hit {width:15%}
	.title{background:lightsteelblue}
	
	.odd {background:silver}
	
	/* paging */
	
	table tfoot ol.paging {
	    list-style:none;
	}
	
	table tfoot ol.paging li {
	    float:left;
	    margin-right:8px;
	}
	
	table tfoot ol.paging li a {
	    display:block;
	    padding:3px 7px;
	    border:1px solid #00B3DC;
	    color:#2f313e;
	    font-weight:bold;
	}
	
	table tfoot ol.paging li a:hover {
	    background:#00B3DC;
	    color:white;
	    font-weight:bold;
	}
	
	.disable {
	    padding:3px 7px;
	    border:1px solid silver;
	    color:silver;
	}
	
	.now {
	   padding:3px 7px;
	    border:1px solid #ff4aa5;
	    background:#ff4aa5;
	    color:white;
	    font-weight:bold;
	}
		
</style>
</head>
<body>
	<div id="bbs">
		<table summary="게시판 목록">
			<caption>게시판 목록</caption>
			<thead>
				<tr class="title">
					<th class="no">번호</th>
					<th class="subject">제목</th>
					<th class="writer">글쓴이</th>
					<th class="reg">날짜</th>
					<th class="hit">조회수</th>
				</tr>
			</thead>
			
			<tfoot>
                      <tr>
                          <td colspan="4">
                              ${pageCode }
                          </td>
						  <td>
							<input type="button" value="글쓰기"
			onclick="javascript:location.href='write.inc'"/>
						  </td>
                      </tr>
                  </tfoot>
			<tbody>
			<c:forEach var="vo" items="${ar }" varStatus="st">
				<tr>
					<td> <%--순차적인 번호를 만들어서 표현하자! --%>
						${rowTotal-((nowPage-1)*blockList+st.index) }
					</td>
					<td style="text-align: left">
					
						<a href="view.inc?b_idx=${vo.b_idx }&cPage=${nowPage}">
							${vo.subject }
						</a>
					</td>
					<td>${vo.writer }</td>
					<td>
						${vo.write_date }
					</td>
					<td>${vo.hit }</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
	</div>
	
</body>
</html>






