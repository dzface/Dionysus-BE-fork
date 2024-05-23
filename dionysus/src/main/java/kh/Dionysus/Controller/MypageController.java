package kh.Dionysus.Controller;
import kh.Dionysus.Dao.JjimDao;
import kh.Dionysus.Dao.MemberDelDao;
import kh.Dionysus.Dao.MemberUpdateDao;
//import kh.Dionysus.Dao.ReviewDao;
import kh.Dionysus.Dao.ReviewDao;
import kh.Dionysus.Dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mypage")
public class MypageController {
    MemberUpdateDao dao = new MemberUpdateDao();

    @PostMapping("/memberselect")
    public ResponseEntity<UserDto> memberselect(@RequestBody Map<String,String> id) {
        System.out.println(id.get("user_id"));
        String getId = id.get("user_id");
        UserDto dto = dao.memberSelect(getId);
        System.out.println(getId);
        System.out.println(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    // 회원 정보 수정
    @PostMapping("/memberupdate")
    public ResponseEntity<Boolean> memberUpdate(@RequestBody UserDto Dto) {
        Boolean isTrue= dao.memberUpdate(Dto);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }


    // 회원 탈퇴 전 이름과 주민번호 체크 true 값이면 정보가 있는 것
    @PostMapping("/memcheck")
    public ResponseEntity<Boolean> memcheck(@RequestBody UserDto Dto) throws SQLException {
        MemberDelDao dao = new MemberDelDao();
        boolean isTrue = dao.MemberCheck(Dto);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }
    // 회원 탈퇴
    @PostMapping("/memberdel")
    public ResponseEntity<Boolean> memberdel(@RequestBody UserDto Dto) {
        String user_name = Dto.getUser_name();
        String user_jumin = Dto.getUser_jumin();
        MemberDelDao dao = new MemberDelDao();
        boolean isTrue = dao.memberDelete(user_name, user_jumin);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }


    // 찜
    @PostMapping("/jjimalcohol")
    public ResponseEntity<List<MypageJjimDto>> jjimalcohol(@RequestBody MypageJjimDto Dto) throws SQLException {
        JjimDao dao = new JjimDao();
        List<MypageJjimDto> jjimList = dao.jjimSelect(Dto.getUser_id());
        List<MypageJjimDto> jjimAlcoholList = dao.jjimAlcoholSelect(jjimList);
        return new ResponseEntity<>(jjimAlcoholList, HttpStatus.OK);
    }
    // 찜 목록의 별점
    @PostMapping("/mypagescore")
    public ResponseEntity<List<ScoreDto>> mypagescore(@RequestBody Map<String, String> alcoholName) {
        JjimDao dao = new JjimDao();
        ScoreDto dto = new ScoreDto();
        String getAlcoholName = alcoholName.get("alcohol_name");
        dto.setAlcohol_name(getAlcoholName);
        List<ScoreDto> scoreList = dao.scoreSelect(getAlcoholName);
        return new ResponseEntity<>(scoreList, HttpStatus.OK);
    }
    // 찜 목록의 술 리뷰
    @PostMapping("/jjimalcoholreview")
    public ResponseEntity<List<ReviewDto>> jjimalcoholreview(@RequestBody Map<String, String> alcoholName) {
        JjimDao dao = new JjimDao();
        ReviewDto dto = new ReviewDto();
        String getAlcoholName = alcoholName.get("alcohol_name");
        dto.setAlcohol_name(getAlcoholName);
        List<ReviewDto> reveiwList = dao.reviewSelect(getAlcoholName);
        return new ResponseEntity<>(reveiwList, HttpStatus.OK);
    }




}





