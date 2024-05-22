package kh.Dionysus.Dao;

import kh.Dionysus.Dto.AlcoholTotalDto;
import kh.Dionysus.Utills.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlcoholDao {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;
    public List<AlcoholTotalDto> alcoholSelect2(String category,String sortBy) throws SQLException {
        List<AlcoholTotalDto> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            if (category.equals("all")) {
                String basesql = "WITH RankedReviews AS (\n" +
                        "    SELECT\n" +
                        "        USER_ID,\n" +
                        "        ALCOHOL_NAME,\n" +
                        "        REVIEW,\n" +
                        "        ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                        "    FROM\n" +
                        "        REVIEW_TB\n" +
                        ")\n" +
                        "SELECT\n" +
                        "    a.ALCOHOL_NAME,\n" +
                        "    a.COUNTRY_OF_ORIGIN,\n" +
                        "    a.COM,\n" +
                        "    a.ABV,\n" +
                        "    a.VOLUME,\n" +
                        "    a.PRICE,\n" +
                        "    r.REVIEW\n" +
                        "FROM\n" +
                        "    ALCOHOL_TB a\n" +
                        "LEFT JOIN RankedReviews r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME AND r.rn = 1\n";
                String orderByClause = "";
                if (sortBy != null && !sortBy.isEmpty()) {
                    switch (sortBy) {
                        case "price":
                            orderByClause = " ORDER BY a.PRICE";
                            break;
                        case "abv":
                            orderByClause = " ORDER BY a.ABV";
                            break;
                        case "volume":
                            orderByClause = " ORDER BY a.VOLUME";
                            break;
                        default:
                            orderByClause = "";
                    }
                }
                String sql = basesql + orderByClause;
                pStmt = conn.prepareStatement(sql);
            } else {
                String basesql = "WITH RankedReviews AS (\n" +
                        "    SELECT\n" +
                        "        USER_ID,\n" +
                        "        ALCOHOL_NAME,\n" +
                        "        REVIEW,\n" +
                        "        ROW_NUMBER() OVER (PARTITION BY ALCOHOL_NAME ORDER BY USER_ID) AS rn\n" +
                        "    FROM\n" +
                        "        REVIEW_TB\n" +
                        ")\n" +
                        "SELECT\n" +
                        "    a.ALCOHOL_NAME,\n" +
                        "    a.COUNTRY_OF_ORIGIN,\n" +
                        "    a.COM,\n" +
                        "    a.ABV,\n" +
                        "    a.VOLUME,\n" +
                        "    a.PRICE,\n" +
                        "    r.REVIEW\n" +
                        "FROM\n" +
                        "    ALCOHOL_TB a\n" +
                        "LEFT JOIN RankedReviews r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME AND r.rn = 1\n" +
                        "WHERE\n" +
                        "    a.CATEGORY = ?";
                String orderByClause = "";
                if (sortBy != null && !sortBy.isEmpty()) {
                    switch (sortBy) {
                        case "price":
                            orderByClause = " ORDER BY a.PRICE";
                            break;
                        case "abv":
                            orderByClause = " ORDER BY a.ABV";
                            break;
                        case "volume":
                            orderByClause = " ORDER BY a.VOLUME";
                            break;
                        default:
                            orderByClause = "";
                    }
                }
                String sql = basesql + orderByClause;
                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, category);
            }
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
    public List<AlcoholTotalDto> alcoholSelect(String category) throws SQLException {
        List<AlcoholTotalDto> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            if (category.equals("all")) {
                String sql = "SELECT a.ALCOHOL_NAME, a.COUNTRY_OF_ORIGIN, a.COM, a.ABV, a.VOLUME, a.PRICE, " +
                        "j.JJIM, r.REVIEW, s.SCORE " +
                        "FROM ALCOHOL_TB a " +
                        "LEFT JOIN REVIEW_TB r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME " +
                        "LEFT JOIN SCORE_TB s ON a.ALCOHOL_NAME = s.ALCOHOL_NAME " +
                        "LEFT JOIN JJIM_TB j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME ";
            } else {
                String sql = "SELECT a.ALCOHOL_NAME, a.COUNTRY_OF_ORIGIN, a.COM, a.ABV, a.VOLUME, a.PRICE, " +
                        "j.JJIM, r.REVIEW, s.SCORE " +
                        "FROM ALCOHOL_TB a " +
                        "LEFT JOIN REVIEW_TB r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME " +
                        "LEFT JOIN SCORE_TB s ON a.ALCOHOL_NAME = s.ALCOHOL_NAME " +
                        "LEFT JOIN JJIM_TB j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME " +
                        "WHERE a.CATEGORY = ?";
                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, category);
            }
            rs = pStmt.executeQuery();
            while (rs.next()) {
                AlcoholTotalDto vo = new AlcoholTotalDto();
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                vo.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                vo.setCom(rs.getString("COM"));
                vo.setAbv(rs.getInt("ABV"));
                vo.setVolume(rs.getInt("VOLUME"));
                vo.setPrice(rs.getInt("PRICE"));
                vo.setJjim(rs.getBoolean("JJIM"));
                vo.setReview(rs.getString("REVIEW"));
                vo.setScore(rs.getInt("SCORE"));
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
}

