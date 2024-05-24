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
    //사용함.
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
                    "    ALCOHOL_NAME,\n" +
                    "    COUNTRY_OF_ORIGIN,\n" +
                    "    COM,\n" +
                    "    ABV,\n" +
                    "    VOLUME,\n" +
                    "    PRICE,\n" +
                    "    REVIEW,\n" +
                    "    SCORE,\n" +
                    "    JJIM_USER_ID,\n" +
                    "    JJIM,\n" +
                    "    row_num\n" +
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
                    "    FROM (\n" +
                    "        SELECT *\n" +
                    "        FROM ALCOHOL_TB\n" +
                    "        WHERE REPLACE(LOWER(ALCOHOL_NAME), ' ', '') LIKE REPLACE(LOWER(REPLACE(?, ' ', '')), ' ', '')\n" +
                    "    ) a\n" +
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
                    ")\n" +
                    "WHERE ROWNUM <= 10";
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
                    "    ALCOHOL_NAME,\n" +
                    "    COUNTRY_OF_ORIGIN,\n" +
                    "    COM,\n" +
                    "    ABV,\n" +
                    "    VOLUME,\n" +
                    "    PRICE,\n" +
                    "    REVIEW,\n" +
                    "    SCORE,\n" +
                    "    JJIM_USER_ID,\n" +
                    "    JJIM\n" +
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
                    "        ROW_NUMBER() OVER (PARTITION BY a.ALCOHOL_NAME ORDER BY j.USER_ID) AS row_num\n" +
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
                    "            JJIM\n" +
                    "        FROM\n" +
                    "            JJIM_TB\n" +
                    "    ) j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME\n" +
                    "    LEFT JOIN RankedReviews r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME AND r.rn = 1\n" +
                    "    WHERE a.CATEGORY = ? \n" +
                    "    AND REPLACE(LOWER(a.ALCOHOL_NAME), ' ', '') LIKE REPLACE(LOWER(REPLACE('%' || ? || '%', ' ', '')), ' ', '')\n" +
                    ")\n" +
                    "WHERE row_num = 1";

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
                vo.setScore(rs.getFloat("SCORE"));
                vo.setJjim_user_id(rs.getString("JJIM_USER_ID"));
                vo.setJjim(rs.getBoolean("JJIM"));
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

