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
            String sql = "SELECT a.ALCOHOL_NAME, a.COUNTRY_OF_ORIGIN, a.COM, a.ABV, a.VOLUME, a.PRICE "+
                    "FROM ALCOHOL_TB a " +
                    "WHERE a.ALCOHOL_NAME LIKE ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, "%" + keyword + "%");
        } else {
            String sql = "SELECT a.ALCOHOL_NAME, a.COUNTRY_OF_ORIGIN, a.COM, a.ABV, a.VOLUME, a.PRICE "+
                    "FROM ALCOHOL_TB a " +
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

