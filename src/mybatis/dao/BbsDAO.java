package mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mybatis.vo.BbsVO;
import mybatis.vo.CommVO;

@Component
public class BbsDAO {

	@Autowired
	private SqlSessionTemplate ss;
	
	//총 게시물 수 - 총페이지 값을 구할 수 있다.
	public int getTotalCount(String bname) {
		
		return ss.selectOne("bbs.totalCount", bname);
	}
	
	//목록
	public BbsVO[] getList(int begin, int end, String bname) {
		BbsVO[] ar = null;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("begin", String.valueOf(begin));
		map.put("end", String.valueOf(end));
		map.put("bname", bname);
		
		List<BbsVO> list = ss.selectList("bbs.bbsList", map);
		if(list != null && list.size() > 0 && !list.isEmpty()) {
			ar = new BbsVO[list.size()];
			list.toArray(ar);
		}
		
		return ar;
	}
	
	public int add(BbsVO vo) {
				
		return ss.insert("bbs.add", vo);
	}
	
	public BbsVO getBbs(String b_idx) {
		
		return ss.selectOne("bbs.getBbs", b_idx);
	}
	
	public int addAns(CommVO vo) {
		
		return ss.insert("bbs.addAns", vo);
		
	}
	
	public int delBbs(String b_idx) {
		
		return ss.update("bbs.delBbs", b_idx);
	}
	
	public int updateHit(String b_idx) {
		
		return ss.update("bbs.hit", b_idx);
	}
	
	public int editBbs(BbsVO vo) {
		
		return ss.update("bbs.edit", vo);
	}
}






