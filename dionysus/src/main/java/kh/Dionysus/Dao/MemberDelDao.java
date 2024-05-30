package kh.Dionysus.Dao;

import kh.Dionysus.Dto.UserDto;
import kh.Dionysus.Utills.Common;

import java.sql.*;

public class MemberDelDao {
    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    // 회원 정보 있는지 확인
    public boolean MemberCheck(UserDto Dto) {
        boolean isNotReg = false;
        String sql = "SELECT * FROM MEMBER_TB WHERE USER_NAME = ? AND USER_JUMIN = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, Dto.getUser_name());
            pStmt.setString(2, Dto.getUser_jumin());
            rs = pStmt.executeQuery();
            if(rs.next()) isNotReg = true;
            else isNotReg = false;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
            return isNotReg;
        }
    }
    public boolean memberDelete(String user_name, String user_jumin) {
        int result = 0;
        String sql1 = "DELETE FROM SCORE_TB " +
                "WHERE USER_ID IN (SELECT M.USER_ID FROM MEMBER_TB M WHERE M.USER_NAME = ? AND M.USER_JUMIN = ?)";

        String sql2 = "DELETE FROM JJIM_TB " +
                "WHERE USER_ID IN (SELECT M.USER_ID FROM MEMBER_TB M WHERE M.USER_NAME = ? AND M.USER_JUMIN = ?)";

        String sql3 = "DELETE FROM REVIEW_TB " +
                "WHERE USER_ID IN (SELECT M.USER_ID FROM MEMBER_TB M WHERE M.USER_NAME = ? AND M.USER_JUMIN = ?)";

        String sql4 = "DELETE FROM MEMBER_TB WHERE USER_NAME = ? AND USER_JUMIN = ?";

        try {
            conn = Common.getConnection();

            // 첫 번째 DELETE 쿼리 실행
            pStmt = conn.prepareStatement(sql1);
            pStmt.setString(1, user_name);
            pStmt.setString(2,user_jumin);

            result += pStmt.executeUpdate();
            pStmt.close();

            // 두 번째 DELETE 쿼리 실행
            pStmt = conn.prepareStatement(sql2);
            pStmt.setString(1, user_name);
            pStmt.setString(2,user_jumin);
            result += pStmt.executeUpdate();
            pStmt.close();

            // 세 번째 DELETE 쿼리 실행
            pStmt = conn.prepareStatement(sql3);
            pStmt.setString(1, user_name);
            pStmt.setString(2,user_jumin);
            result += pStmt.executeUpdate();
            pStmt.close();

            // 네 번째 DELETE 쿼리 실행
            pStmt = conn.prepareStatement(sql4);
            pStmt.setString(1, user_name);
            pStmt.setString(2,user_jumin);
            result += pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(pStmt);
            Common.close(conn);
        }

        return result == 4; // 모든 DELETE 쿼리가 성공했을 경우 true 반환
    }
}
