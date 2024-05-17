package kh.Dionysus.Dao;


import kh.Dionysus.Dto.MemberDto;
import kh.Dionysus.Utills.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    // 회원 가입 여부 확인
    public boolean regMemberCheck(String id) {
        boolean isNotReg = false;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM MEMBER_TB WHERE USER_ID = " + "'" + id +"'";
            rs = stmt.executeQuery(sql);
            if(rs.next()) isNotReg = false;
            else isNotReg = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return isNotReg; // 가입 되어 있으면 false, 가입이 안되어 있으면 true
    }
    // 로그인 체크
    public boolean loginCheck(String id, String pw) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement(); // Statement 객체 얻기
            String sql = "SELECT * FROM MEMBER_TB WHERE USER_ID = " + "'" + id + "'";
            rs = stmt.executeQuery(sql);

            while(rs.next()) { // 읽은 데이타가 있으면 true
                String sqlId = rs.getString("ID"); // 쿼리문 수행 결과에서 ID값을 가져 옴
                String sqlPw = rs.getString("PW");
                System.out.println("ID : " + sqlId);
                System.out.println("PWD : " + sqlPw);
                if(id.equals(sqlId) && pw.equals(sqlPw)) {
                    Common.close(rs);
                    Common.close(stmt);
                    Common.close(conn);
                    return true;
                }
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 회원정보 조회
    public List<MemberDto> memberSelect(String getName) {
        List<MemberDto> list = new ArrayList<>();
        String sql = null;
        System.out.println("NAME : " + getName);
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            if(getName.equals("ALL")) sql = "SELECT * FROM MEMBER_TB";
            else sql = "SELECT * FROM MEMBER_TB WHERE USER_NAME = " + "'" + getName + "'";
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String id = rs.getString("USER_ID");
                String pw = rs.getString("USER_PW");
                String name = rs.getString("USER_NAME");
                String jumin = rs.getString("USER_JUMIN");
                String nick = rs.getString("USER_NICK");
                String phone = rs.getString("USER_PHONE");
                String address = rs.getString("USER_ADDRESS");
                MemberDto dto = new MemberDto();
                dto.setUser_id(id);
                dto.setUser_pw(pw);
                dto.setUser_name(name);
                dto.setUser_jumin(jumin);
                dto.setUser_nick(nick);
                dto.setUser_phone(phone);
                dto.setUser_address(address);
                list.add(dto);
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // 로그인 단계
    // 1. 프론트로부터 받은 아이디 비번을 DB 정보와 조회
    // 2. 있는 것으로 확인 되면 아이디 비번을 다시 반환해주기
    public List<MemberDto> regMemberCheck(String id, String pw) {
        List<MemberDto> loginResult = new ArrayList<>();
        boolean isNotReg = false;
        String user_id = null;
        String user_pw = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT USER_ID, USER_PW FROM MEMBER_TB WHERE USER_ID = " + "'" + id +"' AND USER_PW = "+ "'"+pw+"'";
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                user_id = rs.getString("USER_ID");
                user_pw = rs.getString("USER_PW");
                MemberDto dto = new MemberDto();
                dto.setUser_id(id);
                dto.setUser_pw(pw);
                loginResult.add(dto);
                isNotReg = false;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return loginResult;
    }
    //아이디 찾기
    public String findIDMethod(String getName, String getJumin) {
        String sql = "SELECT USER_ID FROM MEMBER_TB WHERE USER_NAME = ? AND USER_JUMIN=?";
        String user_id = null;
        System.out.println("NAME : " + getName);
        System.out.println("JUMIN : " + getJumin);
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, getName);
            pStmt.setString(2, getJumin);
            rs = pStmt.executeQuery();
            while(rs.next()) {
                user_id = rs.getString("USER_ID");
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return user_id;
    }
    // 비밀번호 찾기
    public String findPWMethod(String getId, String getName, String getJumin) {
        System.out.println("ID : "+ getId);
        System.out.println("NAME : " + getName);
        System.out.println("JUMIN : " + getJumin);
        String user_pw = null;
        String sql = "SELECT USER_PW FROM MEMBER_TB WHERE USER_ID = ? AND USER_NAME = ? AND USER_JUMIN=?";
        try {
            conn = Common.getConnection();

            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, getId);
            pStmt.setString(2, getName);
            pStmt.setString(3, getJumin);
            rs = pStmt.executeQuery();
            while(rs.next()){
                user_pw = rs.getString("USER_PW");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return user_pw;
    }
    // 회원 가입
    public boolean userRegister(MemberDto user) {
        int result = 0;
        String sql = "INSERT INTO MEMBER_TB(USER_ID, USER_PW, USER_NAME, USER_JUMIN, USER_NICK, USER_PHONE, USER_ADDRESS) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, user.getUser_id());
            pStmt.setString(2, user.getUser_pw());
            pStmt.setString(3, user.getUser_name());
            pStmt.setString(4, user.getUser_jumin());
            pStmt.setString(5, user.getUser_nick());
            pStmt.setString(6, user.getUser_phone());
            pStmt.setString(7, user.getUser_address());
            result = pStmt.executeUpdate();
            System.out.println("회원 가입 DB 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);

        if(result == 1) return true;
        else return false;
    }

    public boolean userDeleteMethod(String id) {
        int result = 0;
        String sql = "DELETE FROM MEMBER_TB WHERE USER_ID = ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            result = pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }
}