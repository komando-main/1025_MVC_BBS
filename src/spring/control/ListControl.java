package spring.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.Controller;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.Paging;

@EnableWebMvc
public class ListControl implements Controller{

	@Autowired
	private BbsDAO b_dao;
	
	//페이징 기법을 위한 상수들
	public final int BLOCK_LIST = 7;//한 페이지당 보여질 게시물의 수
	
	public final int BLOCK_PAGE = 5;//한 블럭당 보여질 페이지 수
	
	int nowPage; //현재 페이지 값
	int rowTotal; //전체 게시물의 수
	String pageCode; // 페이징 처리된 HTML코드가 저장될 곳!
	
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		//사용자가 브라우저에서 list.inc라고 요청했을 때 수행하는 곳!
		
		// 반환객체 생성
		ModelAndView mv = new ModelAndView();
		
		//현재 페이지 값 파라미터로 받기
		String c_page = request.getParameter("cPage");
		if(c_page == null)
			nowPage = 1;
		else
			nowPage = Integer.parseInt(c_page);
		
		//게시판 종류를 구별하기 위해 이것 또한 파라미터로 
		String bname = request.getParameter("bname");
		if(bname == null)
			bname = "BBS"; //일반게시판
		
		// 총 게시물의 수를 얻어낸다.
		rowTotal = b_dao.getTotalCount(bname);
		
		//페이징 처리를 위한 객체 생성(Paging)
		Paging page = new Paging(nowPage, rowTotal, BLOCK_LIST, BLOCK_PAGE);
		
		pageCode = page.getSb().toString();//페이징 HTML코드 가져오기
		
		//JSP에서 표현할 게시물들의 목록을 받아내기 위해 begin과
		//end값이 필요하다. 이들은 모두 page에 있다.
		int begin = page.getBegin();
		int end = page.getEnd();
		
		BbsVO[] ar = b_dao.getList(begin, end, bname);
		
		//JSP에서 사용해야 할 값들을 모두 ModelAndView에 저장하자!
		mv.addObject("ar", ar);
		mv.addObject("nowPage", nowPage);
		mv.addObject("rowTotal", rowTotal);
		mv.addObject("blockList", BLOCK_LIST);
		mv.addObject("pageCode", pageCode);
		
		mv.setViewName("list"); // WEB-INF/jsp/list.jsp를 의미함!
		return mv;
	}

}
