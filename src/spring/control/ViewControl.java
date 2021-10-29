package spring.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

@Controller
public class ViewControl {

	@Autowired
	private BbsDAO b_dao;
	
	@Autowired
	private HttpServletRequest request;//접속자 ip가 필요한경우 있어야 한다
	
	@RequestMapping("/view.inc")
	public ModelAndView view(String b_idx, int cPage) {
		//b_idx를 받아서 보여질 게시물을 검색한다. 그리고
		//검색된 게시물 객체를 저장한 후 view.jsp로 가야 한다.
		ModelAndView mv = new ModelAndView();
		BbsVO vo = b_dao.getBbs(b_idx);
		
		mv.addObject("vo", vo);
		mv.addObject("nowPage", cPage);
		mv.addObject("ip", request.getRemoteAddr());//request.getRemoteAddr() 접속자 ip 가저오기!!
		
		mv.setViewName("view");
		
		return mv;
	}
}










