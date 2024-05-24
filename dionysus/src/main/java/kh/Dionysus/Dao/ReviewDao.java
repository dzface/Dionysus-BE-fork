package kh.Dionysus.Dao;

import kh.Dionysus.Dto.*;
import kh.Dionysus.Utills.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;
    MypageReviewDto dto = new MypageReviewDto();
    public List<MypageReviewDto> reviewSelect(String id) throws SQLException {
        List<MypageReviewDto> list = new ArrayList<>();
        String sql = "SELECT USER_ID, ALCOHOL_NAME, REVIEW FROM REVIEW_TB WHERE USER_ID = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                MypageReviewDto dto = new MypageReviewDto();
                dto.setUser_id(rs.getString("USER_ID"));
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

    public List<MypageReviewDto> reviewAlcoholSelect(List<MypageReviewDto> dto) throws SQLException {
        List<MypageReviewDto> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            for (int i = 0; i < dto.size(); i++) {
                String sql = "SELECT M.USER_ID, A.COM, A.ALCOHOL_NAME, A.COUNTRY_OF_ORIGIN, A.ABV, A.VOLUME, A.PRICE, " +
                        "R.REVIEW " +
                        "FROM MEMBER_TB M " +
                        "JOIN REVIEW_TB R ON M.USER_ID = R.USER_ID " +
                        "LEFT JOIN ALCOHOL_TB A ON A.ALCOHOL_NAME = R.ALCOHOL_NAME " +
                        "WHERE R.ALCOHOL_NAME = ? AND R.USER_ID = ?";



                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, dto.get(i).getAlcohol_name());
                pStmt.setString(2, dto.get(i).getUser_id());
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    MypageReviewDto dto1 = new MypageReviewDto();
                    dto1.setUser_id(rs.getString("USER_ID"));
                    dto1.setCom(rs.getString("COM"));
                    dto1.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                    dto1.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                    dto1.setAbv(rs.getInt("ABV"));
                    dto1.setVolume(rs.getInt("VOLUME"));
                    dto1.setPrice(rs.getInt("PRICE"));
                    dto1.setReview(rs.getString("REVIEW"));
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



    public List<ScoreDto> scoreSelect(String alcohol_name) {
        List<ScoreDto> list = new ArrayList<>();
        String sql = "SELECT alcohol_name, ROUND(AVG(score), 1) AS average_score " +
                "FROM SCORE_TB " +
                "WHERE ALCOHOL_NAME = ? " +
                "GROUP BY alcohol_name";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, alcohol_name);
            rs = pStmt.executeQuery();

            while (rs.next()) {
                ScoreDto dto = new ScoreDto();
                dto.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                dto.setScore(rs.getInt("average_score"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return list;
    }
    public boolean reviewInsert(ReviewDto dto) throws SQLException {
        try {
            conn = Common.getConnection();

            String sql = "INSERT INTO REVIEW_TB(USER_ID, ALCOHOL_NAME,REVIEW) VALUES(?,?,?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, dto.getUser_id());
            pStmt.setString(2, dto.getAlcohol_name());
            pStmt.setString(3, dto.getReview());
            pStmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(pStmt);
            Common.close(conn);
        }
        return false;
    }
//    public List<ReviewDto> reviewSelect(String alcohol_name) {
//        List<ReviewDto> list = new ArrayList<>();
//        String sql = "SELECT USER_ID, ALCOHOL_NAME, REVIEW " +
//                "FROM REVIEW_TB " +
//                "WHERE ALCOHOL_NAME = ? ";
//        try {
//            conn = Common.getConnection();
//            pStmt = conn.prepareStatement(sql);
//            pStmt.setString(1, alcohol_name);
//            rs = pStmt.executeQuery();
//
//            while (rs.next()) {
//                ReviewDto dto = new ReviewDto();
//                dto.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
//                dto.setReview(rs.getString("REVIEW"));
//                list.add(dto);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            Common.close(rs);
//            Common.close(pStmt);
//            Common.close(conn);
//        }
//        return list;
//    }


}

