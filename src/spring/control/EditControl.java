package spring.control;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;

@Controller
public class EditControl {

	private String bbs_upload = "/bbs_upload";
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private BbsDAO b_dao;
	
	@Autowired
	private HttpServletRequest request;
	
	
	@RequestMapping(value="/edit.inc", method=RequestMethod.POST)//method=RequestMethod.POST 포스트 방식으로 구분할때
	public ModelAndView edit(BbsVO vo, String cPage) {//인자값으로 클레스를 넣으면 자동으로 @Autowiredrk가 된다!!
		
		ModelAndView mv = new ModelAndView();//if문 밖에 있어야 if문 수행결과 값을 변수에 담을수 있다 (지역 변수 계념 **맴버 변수와 비슷하지만 다르다!!**)
		
		String ctx = request.getContentType();//파일이 첨부 여부를 확인 가능하다 파일이 없을경우 application 가 출력 된다!!
		//System.out.println(ctx); 확인방법
		
		if(ctx.startsWith("application")) {//포스트 방식이며 파일이 첨부되지 않으시 application
			
			//인자로 받은 b_idx로 게시물 검색
			BbsVO bvo = b_dao.getBbs(vo.getB_idx());
			mv.addObject("vo", bvo);
			//mv.addObject("nowPage", cPage);
			
			mv.setViewName("edit");
		}else if(ctx.startsWith("multipart")) {//포스트 방식이며 파일이 첨부 되었을때 multipart
			//파일첨부가 되어서 전달되어 온 경우!!!!!!!!
			MultipartFile mf = vo.getFile();
			if(mf != null && mf.getSize() > 0) {
				//절대경로 얻기
				String realPath = application.getRealPath(bbs_upload);
				
				String fname = mf.getOriginalFilename();
				
				fname = FileRenameUtil.checkSameFileName(fname, realPath);
				
				try {
					mf.transferTo(new File(realPath, fname));//파일 업로드!!!!!
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				vo.setFile_name(fname);
				vo.setOri_name(fname);
			}
			
			vo.setIp(request.getRemoteAddr());
			
			b_dao.editBbs(vo);
			mv.setViewName("redirect:/view.inc?b_idx="+vo.getB_idx()+"&cPage="+cPage);
		}
		
		return mv;//컨트롤+스페이스로 setViewName() 불러올수 있다!! 단 지역변수의 값은 브래이스 밖으로 못나온다
	}
}
