package putImage;

import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


import com.member.model.MemberService;
import com.member.model.MemberVO;

public class PutImages {
	public static void main(String args[]) {

		File file;
		BufferedInputStream bis = null;
		try {

			MemberService memSvc = new MemberService();
			List<MemberVO> memList = memSvc.getAll();

			
			for (int i = 1; i <= memList.size(); i++) {
				file = new File("WebContent/images/Teacher/Teacher" + i + ".jpg");
				
				bis = new BufferedInputStream(new FileInputStream(file));
				
				byte[] b = new byte[(int)bis.available()];
				bis.read(b);
				
				MemberVO memberVO = memList.get(i - 1);
				memSvc.updateMember(memberVO.getMemId(), memberVO.getMemSkill(), memberVO.getMemWantSkill(),
						memberVO.getMemPair(), memberVO.getMemIdCard(), memberVO.getMemPsw(), b,
						memberVO.getMemEmail(), memberVO.getMemAdd(), memberVO.getMemText(), memberVO.getMemBank(),
						memberVO.getMemBalance(), memberVO.getMemBlock(), memberVO.getMemStatus());

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
