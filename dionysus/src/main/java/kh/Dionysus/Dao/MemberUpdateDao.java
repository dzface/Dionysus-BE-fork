package kh.Dionysus.Dao;


import kh.Dionysus.Dto.UserDto;
import kh.Dionysus.Utills.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberUpdateDao {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pSmt = null;

    // 회원 정보 조회
    public UserDto memberSelect(String user_id) {
        UserDto dto = new UserDto();
        String sql = "SELECT * FROM MEMBER_TB WHERE USER_ID = ? ";
        try {
            conn = Common.getConnection();
            pSmt = conn.prepareStatement(sql);
            pSmt.setString(1, user_id);
            rs = pSmt.executeQuery();
            while (rs.next()) {
                dto.setUser_id(rs.getString("USER_ID"));
                dto.setUser_pw(rs.getString("USER_PW"));
                dto.setUser_name(rs.getString("USER_NAME"));
                dto.setUser_jumin(rs.getString("USER_JUMIN"));
                dto.setUser_nick(rs.getString("USER_NICK"));
                dto.setUser_phone(rs.getString("USER_PHONE"));
                dto.setUser_address(rs.getString("USER_ADDRESS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pSmt);
            Common.close(conn);
        }
        return dto;
    }

    // 회원 정보 수정
    public Boolean memberUpdate(UserDto dto) {
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
