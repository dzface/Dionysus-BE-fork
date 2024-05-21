package kh.Dionysus.Dao;

import kh.Dionysus.Dto.ScoreDto;
import kh.Dionysus.Utills.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDao {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    public List<ScoreDto> scoreSelect(String alcohol_name) throws SQLException {
        List<ScoreDto> list = new ArrayList<>();
        String sql = "SELECT * FROM SCORE_TB WHERE ALCOHOL_NAME = ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, alcohol_name);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                ScoreDto vo = new ScoreDto();
                vo.setUser_id(rs.getString("USER_ID"));
                vo.setAlcohol_name(rs.getString("ALCOHOL_NAME"));
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

    public boolean scoreUpdate(ScoreDto dto) throws SQLException {
        String sql = "UPDATE SCORE_TB SET SCORE = ? WHERE USER_ID = ? AND ALCOHOL_NAME = ?";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, dto.getScore());
            pStmt.setString(2, dto.getUser_id());
            pStmt.setString(3, dto.getAlcohol_name());
            if(pStmt.executeUpdate() > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
