package kh.Dionysus.Dao;


import kh.Dionysus.Dto.MemberDto;
import kh.Dionysus.Utills.Common;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MemberUpdateDao {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pSmt = null;

    // 회원 정보 조회
    public MemberDto memberSelect(String userid) {
        MemberDto dto = new MemberDto();
        String sql = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            sql = "SELECT * FROM MEMBER_TB WHERE USER_ID = " + "'" + userid + "'";
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String id = rs.getString("USER_ID");
                String pwd = rs.getString("USER_PW");
                String name = rs.getString("USER_NAME");
                String jumin = rs.getString("USER_JUMIN");
                String nick = rs.getString("USER_NICK");
                String phone = rs.getString("USER_PHONE");
                String addr = rs.getString("USER_ADDRESS");

                dto.setUser_id(id);
                dto.setUser_pw(pwd);
                dto.setUser_name(name);
                dto.setUser_jumin(jumin);
                dto.setUser_nick(nick);
                dto.setUser_phone(phone);
                dto.setUser_address(addr);

            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    // 회원 정보 수정
    public Boolean memberUpdate(MemberDto dto) {
        int result = 0;
        String sql = "UPDATE MEMBER_TB SET user_pw = ?,user_name=?," +
                "user_nick=?,user_phone=?,user_address=? WHERE user_id=?";
        try {
            conn = Common.getConnection();
            pSmt = conn.prepareStatement(sql);
            pSmt.setString(1, dto.getUser_pw());
            pSmt.setString(2, dto.getUser_name());
            pSmt.setString(3, dto.getUser_nick());
            pSmt.setString(4, dto.getUser_phone());
            pSmt.setString(5, dto.getUser_address());
            pSmt.setString(6, dto.getUser_id());
            result = pSmt.executeUpdate();
            System.out.println(result);
            pSmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(pSmt);
            Common.close(conn);
        }
        if(result == 1) return true;
        else return false;
    }



}
