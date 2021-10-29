package spring.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;

@Controller
public class DelControl {

	@Autowired
	private BbsDAO b_dao;
	
	@RequestMapping("/delete.inc")
	public ModelAndView del(String b_idx, String cPage) {
		ModelAndView mv = new ModelAndView();
		
		b_dao.delBbs(b_idx); // status값을 1로 변경!
		
		mv.setViewName("redirect:/list.inc?cPage="+cPage);//cPage가 같이 보내저야 현제 페이지가 보여진다
		
		return mv;
	}
}
