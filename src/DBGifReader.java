

import java.io.*;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.tomcat.util.codec.binary.Base64;

import sun.misc.BASE64Encoder;

@WebServlet("/DBGifReader")
public class DBGifReader extends HttpServlet {
	
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String url = "jdbc:oracle:thin:@localhost:49161:XE";
	String userid = "WESHARE";
	String passwd = "123456";
	private static final String GET_ONE_BY_MEMID = "SELECT memImage FROM member WHERE memId=?";

	Connection con;
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req,res);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		res.setContentType("image/gif");
		
		if("get_member_pic".equals(action)) {
			
			PrintWriter out = res.getWriter();
			
			String base64 = null;
			byte[] bPic = null;
			InputStream is = null;
			OutputStream os = null;
			try {
				
				
			
				pstmt = con.prepareStatement(GET_ONE_BY_MEMID);
				pstmt.setString(1,req.getParameter("memId"));
				
				rs = pstmt.executeQuery();
		
				if (rs.next()) {
					bPic = rs.getBytes("memImage");
				} else {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				
			} catch (Exception e) {
				System.out.println(e);
			} finally {	
				try {
					rs.close();
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(bPic.length);
			base64 = Base64.encodeBase64String(bPic);
			out.print(base64);

						
		}
		
		
		
		
//		ServletOutputStream out = res.getOutputStream();
//
//		try {
//			pstmt = con.prepareStatement(INSERT_STMT);
//			pstmt.setString(1,req.getParameter("memId"));
//			rs = pstmt.executeQuery();
//	
//
//			if (rs.next()) {
//				BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream("memImage"));
//				byte[] buf = new byte[4 * 1024]; // 4K buffer
//				int len;
//				while ((len = in.read(buf)) != -1) {
//					out.write(buf, 0, len);
//				}
//				in.close();
//			} else {
//				res.sendError(HttpServletResponse.SC_NOT_FOUND);
//			}
//			rs.close();
//			pstmt.close();
//		} catch (Exception e) {
//			System.out.println(e);
//		}
	}

	public void init() throws ServletException {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Couldn't load JdbcOdbcDriver");
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't get db connection");
		}
	}

	public void destroy() {
		try {
			if (con != null) con.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}