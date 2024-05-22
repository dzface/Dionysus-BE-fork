package kh.Dionysus.Dao;

import kh.Dionysus.Dto.MypageDto;
import kh.Dionysus.Dto.ReviewDto;
import kh.Dionysus.Utills.Common;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 리뷰 조회
public class ReviewDao {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    public List<MypageDto> reviewSelect(String id) throws SQLException {
        List<MypageDto> list = new ArrayList<>();
        String sql = "SELECT  M.USER_ID, R.ALCOHOL_NAME, R.REVIEW " +
                "FROM MEMBER_TB M " +
                "JOIN REVIEW_TB R ON M.USER_ID = R.USER_ID " +
                "LEFT JOIN ALCOHOL_TB A ON A.ALCOHOL_NAME = R.ALCOHOL_NAME " +
                "WHERE M.USER_ID = ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                MypageDto dto = new MypageDto();
                dto.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                dto.setReview(rs.getString("REVIEW"));
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

    public List<MypageDto> reviewAlcoholSelect(List<MypageDto> dto) throws SQLException {
        List<MypageDto> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            for (int i = 0; i < dto.size(); i++) {
                String sql = "SELECT M.USER_ID, a.COM, a.ALCOHOL_NAME, a.COUNTRY_OF_ORIGIN, a.ABV, a.VOLUME, a.PRICE, " +
                        "r.REVIEW, s.SCORE, j.JJIM " +
                        "FROM ALCOHOL_TB a " +
                        "JOIN REVIEW_TB r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME " +
                        "LEFT JOIN SCORE_TB s ON a.ALCOHOL_NAME = s.ALCOHOL_NAME " +
                        "LEFT JOIN JJIM_TB j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME " +
                        "LEFT JOIN MEMBER_TB m ON m.USER_ID = j.USER_ID " +
                        "WHERE a.ALCOHOL_NAME = ?";
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
                    dto1.setJjim(rs.getBoolean( "JJIM"));
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
    public List<ReviewDto> reviewSelect2() throws SQLException {
        List<ReviewDto> list = new ArrayList<>();
        String sql = "WITH RankedReviews AS (\n" +
                "    SELECT\n" +
                "        USER_ID,\n" +
                "        ALCOHOL_NAME,\n" +
                "        REVIEW,\n" +
                "        ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                "    FROM\n" +
                "        REVIEW_TB\n" +
                ")\n" +
                "SELECT\n" +
                "    USER_ID,\n" +
                "    ALCOHOL_NAME,\n" +
                "    REVIEW\n" +
                "FROM\n" +
                "    RankedReviews\n" +
                "WHERE\n" +
                "    rn = 1;";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                ReviewDto vo = new ReviewDto();
                vo.setUser_id(rs.getString("USER_ID"));
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                vo.setReview(rs.getString("REVIEW"));
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
    public boolean reviewInsert(ReviewDto dto) throws SQLException {
        String sql = "INSERT INTO REVIEW_TB (USER_ID, ALCOHOL_NAME, REVIEW) VALUES (?, ?, ?)";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, dto.getUser_id());
            pStmt.setString(2, dto.getAlcohol_name());
            pStmt.setString(3, dto.getReview());
            if(pStmt.executeUpdate() > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reviewUpdate(ReviewDto dto) throws SQLException {
        String sql = "UPDATE REVIEW_TB SET REVIEW = ? WHERE USER_ID = ? AND ALCOHOL_NAME = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, dto.getReview());
            pStmt.setString(2, dto.getUser_id());
            pStmt.setString(3, dto.getAlcohol_name());
            if(pStmt.executeUpdate() > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reviewDelete(String user_id, String alcohol_name) throws SQLException {
        String sql = "DELETE FROM REVIEW_TB WHERE USER_ID = ? AND ALCOHOL_NAME = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, user_id);
            pStmt.setString(2, alcohol_name);
            if(pStmt.executeUpdate() > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
