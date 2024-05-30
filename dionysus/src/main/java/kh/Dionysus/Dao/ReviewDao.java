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
    public List<ReviewDto> reviewSelect(String ALCOHOL_NAME) throws SQLException {
        List<ReviewDto> list = new ArrayList<>();
        String sql = "SELECT REVIEW_TB.USER_ID, REVIEW_TB.ALCOHOL_NAME, REVIEW_TB.REVIEW, MEMBER_TB.USER_NICK\n" +
                "FROM REVIEW_TB\n" +
                "JOIN MEMBER_TB ON REVIEW_TB.USER_ID = MEMBER_TB.USER_ID\n" +
                "WHERE REVIEW_TB.ALCOHOL_NAME = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, ALCOHOL_NAME);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                ReviewDto dto = new ReviewDto();
                dto.setUser_id(rs.getString("USER_ID"));
                dto.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                dto.setReview(rs.getString("REVIEW"));
                dto.setUser_nick(rs.getString("USER_NICK"));
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
}

