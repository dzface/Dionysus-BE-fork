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

@CrossOrigin(origins = "http://gool-man.kro.kr:3000")
@RestController
@RequestMapping("/mypage")
public class MypageController {
    MemberUpdateDao dao = new MemberUpdateDao();

    @PostMapping("/memberselect")
    public ResponseEntity<UserDto> memberselect(@RequestBody Map<String, String> id) {
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
        Boolean isTrue = dao.memberUpdate(Dto);
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
    @PostMapping("/jjimalcohollist")
    public ResponseEntity<List<AlcoholTotalDto>> jjimalcohol(@RequestBody Map<String,String> user_id) throws SQLException {
        JjimDao dao = new JjimDao();
        String get_id = user_id.get("user_id");
        List<AlcoholTotalDto> jjimList = dao.jjimSelect(get_id);
        return new ResponseEntity<>(jjimList, HttpStatus.OK);
    }

    // 찜 목록의 술 리뷰
    @PostMapping("/alcoholreviewlist")
    public ResponseEntity<List<AlcoholTotalDto>> alcoholreviewlist(@RequestBody Map<String,String> user_id) throws SQLException{
        JjimDao dao = new JjimDao();
        String get_id = user_id.get("user_id");
        List<AlcoholTotalDto> reviewList = dao.reviewSelect(get_id);
        return new ResponseEntity<>(reviewList, HttpStatus.OK);
    }

}







