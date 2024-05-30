package kh.Dionysus.Dao;

import kh.Dionysus.Dto.AlcoholTotalDto;
import kh.Dionysus.Dto.JjimDto;

import kh.Dionysus.Dto.MypageJjimDto;
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

    public void insertJjim(JjimDto dto) throws SQLException {
        String sql = "INSERT INTO JJIM_TB (USER_ID, ALCOHOL_NAME, JJIM) VALUES (?, ?, ?) ";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, dto.getUser_id());            pStmt.setString(2, dto.getAlcohol_name());
            pStmt.setBoolean(3,true);
            pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(pStmt);
            Common.close(conn);
        }
    }
    public List<AlcoholTotalDto> jjimSelect(String id) throws SQLException {
        List<AlcoholTotalDto> list = new ArrayList<>();
        String sql = "WITH RankedReviews AS (\n" +
                "    SELECT\n" +
                "        USER_ID,\n" +
                "        ALCOHOL_NAME,\n" +
                "        REVIEW,\n" +
                "        ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                "    FROM\n" +
                "        REVIEW_TB\n" +
                ")\n" +
                "SELECT *\n" +
                "FROM (\n" +
                "    SELECT\n" +
                "        a.ALCOHOL_NAME,\n" +
                "        a.COUNTRY_OF_ORIGIN,\n" +
                "        a.COM,\n" +
                "        a.ABV,\n" +
                "        a.VOLUME,\n" +
                "        a.PRICE,\n" +
                "        r.REVIEW,\n" +
                "        s.SCORE,\n" +
                "        j.USER_ID AS JJIM_USER_ID,\n" +
                "        j.JJIM,\n" +
                "        ROW_NUMBER() OVER (ORDER BY COALESCE(s.SCORE, 0) DESC) AS row_num\n" +
                "    FROM\n" +
                "        ALCOHOL_TB a\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            ALCOHOL_NAME,\n" +
                "            ROUND(AVG(SCORE), 1) AS SCORE\n" +
                "        FROM\n" +
                "            SCORE_TB\n" +
                "        GROUP BY\n" +
                "            ALCOHOL_NAME\n" +
                "    ) s ON a.ALCOHOL_NAME = s.ALCOHOL_NAME\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            USER_ID,\n" +
                "            ALCOHOL_NAME,\n" +
                "            JJIM,\n" +
                "            ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                "        FROM\n" +
                "            JJIM_TB\n" +
                "    ) j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME AND j.rn = 1\n" +
                "    LEFT JOIN RankedReviews r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME AND r.rn = 1\n" +
                "    WHERE j.USER_ID = ? )";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                AlcoholTotalDto vo = new AlcoholTotalDto();
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                vo.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                vo.setCom(rs.getString("COM"));
                vo.setAbv(rs.getInt("ABV"));
                vo.setVolume(rs.getInt("VOLUME"));
                vo.setPrice(rs.getInt("PRICE"));
                vo.setReview(rs.getString("REVIEW"));
                vo.setScore(rs.getFloat("SCORE"));
                vo.setJjim_user_id(rs.getString("JJIM_USER_ID"));
                vo.setJjim(rs.getBoolean("JJIM"));
                list.add(vo);
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
    public List<AlcoholTotalDto> reviewSelect(String id) throws SQLException {
        List<AlcoholTotalDto> list = new ArrayList<>();
        String sql = "WITH RankedReviews AS (\n" +
                "    SELECT\n" +
                "        USER_ID,\n" +
                "        ALCOHOL_NAME,\n" +
                "        REVIEW,\n" +
                "        ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                "    FROM\n" +
                "        REVIEW_TB\n" +
                ")\n" +
                "SELECT *\n" +
                "FROM (\n" +
                "    SELECT\n" +
                "        a.ALCOHOL_NAME,\n" +
                "        a.COUNTRY_OF_ORIGIN,\n" +
                "        a.COM,\n" +
                "        a.ABV,\n" +
                "        a.VOLUME,\n" +
                "        a.PRICE,\n" +
                "        r.REVIEW,\n" +
                "        s.SCORE,\n" +
                "        j.USER_ID AS JJIM_USER_ID,\n" +
                "        j.JJIM,\n" +
                "        rr.USER_ID AS REVIEW_USER_ID,\n" +
                "        ROW_NUMBER() OVER (ORDER BY COALESCE(s.SCORE, 0) DESC) AS row_num\n" +
                "    FROM\n" +
                "        ALCOHOL_TB a\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            ALCOHOL_NAME,\n" +
                "            ROUND(AVG(SCORE), 1) AS SCORE\n" +
                "        FROM\n" +
                "            SCORE_TB\n" +
                "        GROUP BY\n" +
                "            ALCOHOL_NAME\n" +
                "    ) s ON a.ALCOHOL_NAME = s.ALCOHOL_NAME\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            USER_ID,\n" +
                "            ALCOHOL_NAME,\n" +
                "            JJIM,\n" +
                "            ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                "        FROM\n" +
                "            JJIM_TB\n" +
                "    ) j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME AND j.rn = 1\n" +
                "    LEFT JOIN RankedReviews r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME AND r.rn = 1\n" +
                "    LEFT JOIN REVIEW_TB rr ON a.ALCOHOL_NAME = rr.ALCOHOL_NAME\n" +
                ")\n" +
                "WHERE REVIEW_USER_ID = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                AlcoholTotalDto vo = new AlcoholTotalDto();
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                vo.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                vo.setCom(rs.getString("COM"));
                vo.setAbv(rs.getInt("ABV"));
                vo.setVolume(rs.getInt("VOLUME"));
                vo.setPrice(rs.getInt("PRICE"));
                vo.setReview(rs.getString("REVIEW"));
                vo.setScore(rs.getFloat("SCORE"));
                vo.setJjim_user_id(rs.getString("REVIEW_USER_ID"));
                vo.setJjim(rs.getBoolean("JJIM"));
                list.add(vo);
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
    public void deleteJjim(JjimDto dto) throws SQLException {
        String sql = "DELETE FROM JJIM_TB WHERE USER_ID = ? AND  ALCOHOL_NAME=?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, dto.getUser_id());
            pStmt.setString(2, dto.getAlcohol_name());
            pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(conn);
            Common.close(pStmt);
        }
    }
}
