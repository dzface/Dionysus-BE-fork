package kh.Dionysus.Dao;

import kh.Dionysus.Dto.AlcoholTotalDto;
import kh.Dionysus.Utills.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchDao {
    private Connection conn = null;
    private PreparedStatement pStmt = null;
    private ResultSet rs = null;

    public List<AlcoholTotalDto> alcoholSearch(String category,String keyword) throws SQLException {
        List<AlcoholTotalDto> search = new ArrayList<>();
        try{
        conn = Common.getConnection();
        if (category.equals("all")) {
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
                    "WHERE a.ALCOHOL_NAME LIKE ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, "%" + keyword + "%");
        } else {
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
                    "WHERE a.CATEGORY = ? AND a.ALCOHOL_NAME LIKE ?";

            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, category);
            pStmt.setString(2, "%" + keyword + "%");
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
                search.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return search;
    }
}

