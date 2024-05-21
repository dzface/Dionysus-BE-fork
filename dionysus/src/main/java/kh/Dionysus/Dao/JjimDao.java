package kh.Dionysus.Dao;

import kh.Dionysus.Dto.JjimDto;
import kh.Dionysus.Dto.MypageDto;
import kh.Dionysus.Utills.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JjimDao {
    private Connection conn = null;
    private PreparedStatement pStmt = null;
    private ResultSet rs = null;

    public void toggleJjim(String userId, String alcoholName, boolean jjim) throws SQLException {
        String sql = "INSERT INTO JJIM_TB (USER_ID, ALCOHOL_NAME, JJIM) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE JJIM = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userId);
            pStmt.setString(2, alcoholName);
            pStmt.setBoolean(3, jjim);
            pStmt.setBoolean(4, jjim);
            pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(conn);
            Common.close(pStmt);
        }
    }
    public List<MypageDto> jjimSelect(String id) throws SQLException {
        List<MypageDto> list = new ArrayList<>();
        String sql = "SELECT  M.USER_ID, j.ALCOHOL_NAME, j.JJIM " +
                "FROM MEMBER_TB M " +
                "JOIN JJIM_TB j ON M.USER_ID = j.USER_ID " +
                "LEFT JOIN Alcohol_TB a ON a.ALCOHOL_NAME = j.ALCOHOL_NAME " +
                "WHERE M.USER_ID = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                MypageDto dto = new MypageDto();
                dto.setUser_id(rs.getString("USER_ID"));
                dto.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                dto.setJjim(rs.getBoolean("JJIM"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return list;
    }

    public List<MypageDto> jjimAlcoholSelect(List<MypageDto> dto) throws SQLException {
        List<MypageDto> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            for (int i = 0; i < dto.size(); i++) {
                String sql = "SELECT M.USER_ID, A.COM, A.ALCOHOL_NAME, A.COUNTRY_OF_ORIGIN, A.ABV, A.VOLUME, A.PRICE, " +
                        "R.REVIEW, S.SCORE, J.JJIM " +
                        "FROM MEMBER_TB M " +
                        "JOIN JJIM_TB J ON M.USER_ID = J.USER_ID " +
                        "LEFT JOIN ALCOHOL_TB A ON A.ALCOHOL_NAME = J.ALCOHOL_NAME " +
                        "LEFT JOIN REVIEW_TB R ON A.ALCOHOL_NAME = R.ALCOHOL_NAME " +
                        "LEFT JOIN SCORE_TB S ON J.ALCOHOL_NAME = S.ALCOHOL_NAME " +
                        "WHERE A.ALCOHOL_NAME = ?";

                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, dto.get(i).getAlcohol_name());
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    MypageDto dto1 = new MypageDto();
                    dto1.setUser_id(rs.getString("USER_ID"));
                    dto1.setCom(rs.getString("COM"));
                    dto1.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                    dto1.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                    dto1.setAbv(rs.getInt("ABV"));
                    dto1.setVolume(rs.getInt("VOLUME"));
                    dto1.setPrice(rs.getInt("PRICE"));
                    dto1.setReview(rs.getString("REVIEW"));
                    dto1.setScore(rs.getString("SCORE"));
                    dto1.setJjim(rs.getBoolean("JJIM"));
                    list.add(dto1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return list;
    }
    public List<JjimDto> jjimSelect2(String user_id) throws SQLException {
        List<JjimDto> list = new ArrayList<>();
        String sql = "SELECT * FROM JJIM_TB WHERE USER_ID = ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, user_id);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                JjimDto vo = new JjimDto();
                vo.setUser_id(rs.getString("USER_ID"));
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                vo.setJjim(rs.getBoolean("JJIM"));
                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(pStmt);
        Common.close(conn);
        return list;
    }

    public boolean jjimUpdate(JjimDto dto) throws SQLException {
        String sql = "UPDATE JJIM_TB SET JJIM = ? WHERE USER_ID = ? AND ALCOHOL_NAME = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setBoolean(1, dto.isJjim());
            pStmt.setString(2, dto.getUser_id());
            pStmt.setString(3, dto.getAlcohol_name());
            if(pStmt.executeUpdate() > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
