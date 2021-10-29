package spring.control;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import mybatis.vo.CommVO;
import spring.util.FileRenameUtil;
import spring.vo.ImgVO;

@Controller
public class WriteControl {
	
	private String img_path = "/editor_img";//저장할 경로를 문자로 지정하여 저장 한다
	private String bbs_upload = "/bbs_upload";
	
	@Autowired
	private ServletContext application;//실재 경로를 지정하기위해 필요하다
	
	@Autowired
	private HttpServletRequest request;//저장이 완료 되었을때 실재 저장된 경로를 반환하기위해 필요 하다
	
	@Autowired
	private BbsDAO b_dao;//컨트롤러가 필요로 하기 때문에 컨트롤러보다 먼저 생성되어 있어야 한다 applicationContext.xml
	
	@RequestMapping("/write.inc")
	public String write() {
		return "write"; // WEB-INF/jsp/write.jsp
	}
	
	@RequestMapping(value="/saveImg.inc", method=RequestMethod.POST)
	@ResponseBody//비동기식 통신
	public Map<String, String> saveImg(ImgVO vo){//반환형이 맵인겨우 비동기통신 (제이슨)
		//반환객체 생성
		Map<String, String> map = new HashMap<String, String>();
		
		//넘어온 이미지 파일이 있는지 확인
		MultipartFile f = vo.getS_file();
		String fname = null; // 반환할 때 필요함!
		
		if(f.getSize() > 0) {
			//이미지 파일이 있는 경우
			String realPath = application.getRealPath(img_path);
			
			fname = f.getOriginalFilename(); //f.getName();// s_file
			
			//첨부파일이 앞서 저장된 파일명과 동일할 경우 파일명 뒤에 숫자를
			//붙여서 같은 이름을 피한다.
			fname = FileRenameUtil.checkSameFileName(fname, realPath);
			
			try {
				f.transferTo(new File(realPath, fname));//지정된 위치(realPath)에
														//파일 올린다.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//파일이 업로드가 되었으므로 이제 정확한 경로를 반환해야 한다.(JSON)
		String c_path = request.getContextPath();
		
		map.put("url", c_path+img_path);//  1025_MVC_Bbs/editor_img
		map.put("fname", fname);// flag.png
		
		return map;
	}
	
	@RequestMapping(value="/write.inc", method=RequestMethod.POST)
	public ModelAndView write(BbsVO vo)throws Exception{
		
		// 첨부된 파일을 vo로부터 얻어낸다.
		MultipartFile mf = vo.getFile();
		
		if(mf.getSize() > 0) {
			//절대경로 얻기
			String realPath = application.getRealPath(bbs_upload);
			
			//파일명 얻기
			String fname = mf.getOriginalFilename();
			
			//동일한 파일명이 있다면 fname변경!
			fname = FileRenameUtil.checkSameFileName(fname, realPath);
			
			mf.transferTo(new File(realPath, fname));
			
			//첨부된 파일명을 DB에 저장하기 위해 vo안에 있는 file_name에 저장
			vo.setFile_name(fname);
			vo.setOri_name(fname);
		}
			
		vo.setBname("BBS");
		vo.setIp(request.getRemoteAddr());//접속자 IP저장
		
		b_dao.add(vo);// DB에 저장!!!!
		
		//반환을 위한 객체 생성
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/list.inc");// list.inc로 Redirect한다.
		
		return mv;
	}
	
	@RequestMapping("/ans_write.inc")
	public ModelAndView ans_write(CommVO cvo, String cPage) {
		ModelAndView mv = new ModelAndView();
		
		//댓글정보가 모두 cvo에 저장되어 넘어왔다.
		b_dao.addAns(cvo); //댓글 저장
		
		mv.setViewName("redirect:/view.inc?b_idx="+cvo.getB_idx()+"&cPage="+cPage);
		
		return mv;
		
	}
}







