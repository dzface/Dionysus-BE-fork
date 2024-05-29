package kh.Dionysus.Dao;


import kh.Dionysus.Dto.UserDto;
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
        String sql = "SELECT * FROM MEMBER_TB WHERE USER_ID = ?";
        try (Connection conn = Common.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    isNotReg = false; // 가입되어 있음
                } else {
                    isNotReg = true;  // 가입되어 있지 않음
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNotReg; // 가입 되어 있으면 false, 가입이 안되어 있으면 true
    }
    // 주민번호유효성 확인
    public boolean juminCheck(String USER_JUMIN) {
        boolean isNotReg = false;
        String sql = "SELECT * FROM MEMBER_TB WHERE USER_JUMIN = ?";
        try (Connection conn = Common.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, USER_JUMIN);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    isNotReg = false; // 가입되어 있음
                    System.out.println("주민등록번호가 이미 존재합니다.");
                } else {
                    isNotReg = true;  // 가입되어 있지 않음
                    System.out.println("주민등록번호가 존재하지 않습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                String sqlId = rs.getString("USER_ID"); // 쿼리문 수행 결과에서 ID값을 가져 옴
                String sqlPw = rs.getString("USER_PW");
                String sqlName = rs.getString("USER_USER_NAME");
                System.out.println("ID : " + sqlId);
                System.out.println("PD : " + sqlPw);
                if(id.equals(sqlId) && pw.equals(sqlPw)) {
                    Common.close(rs);
                    Common.close(stmt);
                    Common.close(conn);
                    return true;
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        }
        return false;
    }

    // 회원정보 조회
    public List<UserDto> memberSelect(String getName) {
        List<UserDto> list = new ArrayList<>();
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
                UserDto dto = new UserDto();
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
        }finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        };
        return list;
    }
    // 로그인 단계
    // 1. 프론트로부터 받은 아이디 비번을 DB 정보와 조회
    // 2. 있는 것으로 확인 되면 아이디 비번을 다시 반환해주기
    public List<UserDto> loginUserCheck(String USER_ID, String USER_PW) {
        List<UserDto> loginResult = new ArrayList<>();
        boolean isNotReg = false;
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM MEMBER_TB WHERE USER_ID = ? AND USER_PW = ? "; // 모든 회원정보 조회
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, USER_ID);
            pStmt.setString(2, USER_PW);
            rs = pStmt.executeQuery();
            if(rs.next()) {
                String user_id = rs.getString("USER_ID");
                String user_pw = rs.getString("USER_PW");
                String user_name = rs.getString("USER_NAME");
                String user_jumin = rs.getString("USER_JUMIN");
                String user_nick = rs.getString("USER_NICK");
                String user_phone = rs.getString("USER_PHONE");
                String user_address = rs.getString("USER_ADDRESS");
                UserDto dto = new UserDto();
                dto.setUser_id(user_id);
                dto.setUser_pw(user_pw);
                dto.setUser_name(user_name);
                dto.setUser_jumin(user_jumin);
                dto.setUser_nick(user_nick);
                dto.setUser_phone(user_phone);
                dto.setUser_address(user_address);
                loginResult.add(dto);
                isNotReg = false;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return loginResult;
    }
    //아이디 찾기
    public String findIDMethod(String getUser_name, String getUser_jumin) {
        String sql = "SELECT USER_ID FROM MEMBER_TB WHERE USER_NAME = ? AND USER_JUMIN=?";
        String user_id = null;
        System.out.println("NAME : " + getUser_name);
        System.out.println("JUMIN : " + getUser_jumin);
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, getUser_name);
            pStmt.setString(2, getUser_jumin);
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
    public boolean userRegister(UserDto user) {
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