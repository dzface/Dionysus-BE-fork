package kh.Dionysus.Dao;

import kh.Dionysus.Dto.ScoreDto;
import kh.Dionysus.Utills.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDao {
    private Connection conn = null;
    private PreparedStatement pStmt = null;


    public boolean scoreInsert(ScoreDto dto) throws SQLException {
        String sql = "INSERT INTO SCORE_TB (USER_ID, ALCOHOL_NAME, SCORE) VALUES (?, ?, ?)";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, dto.getUser_id());
            pStmt.setString(2, dto.getAlcohol_name());
            pStmt.setInt(3, dto.getScore());
            if(pStmt.executeUpdate() > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
