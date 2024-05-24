package kh.Dionysus.Dao;

import kh.Dionysus.Dto.AlcoholTotalDto;
import kh.Dionysus.Utills.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PopularListDao {
    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;
    public List<AlcoholTotalDto> popularSelect(String tag) throws SQLException {
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
                "        a.TAG,\n" +
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
                "            JJIM\n" +
                "        FROM\n" +
                "            JJIM_TB\n" +
                "    ) j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME\n" +
                "    LEFT JOIN RankedReviews r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME AND r.rn = 1\n" +
                ") WHERE TAG LIKE ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, "%" + tag + "%");
            rs = pStmt.executeQuery();
            while (rs.next()) {
                AlcoholTotalDto vo = new AlcoholTotalDto();
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                vo.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                vo.setCom(rs.getString("COM"));
                vo.setAbv(rs.getInt("ABV"));
                vo.setVolume(rs.getInt("VOLUME"));
                vo.setPrice(rs.getInt("PRICE"));
                vo.setTag(rs.getString("TAG"));
                vo.setJjim(rs.getBoolean("JJIM"));
                vo.setReview(rs.getString("REVIEW"));
                vo.setScore(rs.getFloat("SCORE"));
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

