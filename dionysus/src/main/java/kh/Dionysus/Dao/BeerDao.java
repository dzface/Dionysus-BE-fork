package kh.Dionysus.Dao;

import kh.Dionysus.Dto.AlcoholDto;
import kh.Dionysus.Utills.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BeerDao {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    public List<AlcoholDto> beerSelect(String beer){
        List<AlcoholDto> list = new ArrayList<>();
        String sql = "SELECT * FROM ALCOHOL_TB WHERE CATEGORY  = " + "'" + beer + "'";
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String alcohol_name = rs.getString("ALCOHOL_NAME");
                String category = rs.getString("CATEGORY");
                String country_of_origin = rs.getString("COUNTRY_OF_ORIGIN");
                String com = rs.getString("COM");
                int abv = rs.getInt("ABV");
                int volume = rs.getInt("VOLUME");
                int price = rs.getInt("PRICE");
                String tag = rs.getString("TAG");

                AlcoholDto vo = new AlcoholDto();
                vo.setAlcohol_name(alcohol_name);
                vo.setCategory(category);
                vo.setCountry_of_origin(country_of_origin);
                vo.setCom(com);
                vo.setAbv(abv);
                vo.setVolume(volume);
                vo.setPrice(price);
                vo.setTag(tag);
                list.add(vo);
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

