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
    public List<AlcoholTotalDto> alcoholSelect(String alcohol) throws SQLException {
        List<AlcoholTotalDto> list = new ArrayList<>();
        String sql = "SELECT a.ALCOHOL_NAME, a.CATEGORY, a.COUNTRY_OF_ORIGIN, a.COM, a.ABV, a.VOLUME, a.PRICE, a.TAG, " +
                "j.JJIM, r.REVIEW, s.SCORE " +
                "FROM ALCOHOL_TB a " +
                "LEFT JOIN REVIEW_TB r ON a.ALCOHOL_NAME = r.ALCOHOL_NAME " +
                "LEFT JOIN SCORE_TB s ON a.ALCOHOL_NAME = s.ALCOHOL_NAME " +
                "LEFT JOIN JJIM_TB j ON a.ALCOHOL_NAME = j.ALCOHOL_NAME " +
                "WHERE a.CATEGORY = ?";

            try {
                conn = Common.getConnection();
                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, alcohol);
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    AlcoholTotalDto vo = new AlcoholTotalDto();
                    vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
                    vo.setCategory(rs.getString("CATEGORY"));
                    vo.setCountry_of_origin(rs.getString("COUNTRY_OF_ORIGIN"));
                    vo.setCom(rs.getString("COM"));
                    vo.setAbv(rs.getInt("ABV"));
                    vo.setVolume(rs.getInt("VOLUME"));
                    vo.setPrice(rs.getInt("PRICE"));
                    vo.setTag(rs.getString("TAG"));
                    vo.setJjim(rs.getBoolean("JJIM"));
                    vo.setReview(rs.getString("REVIEW"));
                    vo.setScore(rs.getInt("SCORE"));
                    list.add(vo);
                    }
          } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        Common.close(pStmt);
        return list;
    }
}

