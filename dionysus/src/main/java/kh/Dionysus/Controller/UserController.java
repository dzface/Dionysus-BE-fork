package kh.Dionysus.Controller;

import kh.Dionysus.Dao.UserDAO;
import kh.Dionysus.Dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@Slf4j //롬복내장 디버깅 어노테이션
public class UserController {
    @GetMapping("/search-user")
    public ResponseEntity<List<UserDto>> memberList(@RequestParam String name) {
        System.out.println("NAME : " + name);
        UserDAO dao = new UserDAO();
        List<UserDto> list = dao.memberSelect(name);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // POST : 로그인
    @PostMapping("/login")
    public ResponseEntity<List<UserDto>> memberLogin(@RequestBody Map<String, String> loginData) {
        // 디버깅: loginData에 올바른 키가 있는지 확인
        if (!loginData.containsKey("USER_ID") || !loginData.containsKey("USER_PW")) {
            System.err.println("Request data does not contain USER_ID or USER_PW.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = loginData.get("USER_ID");
        String pw = loginData.get("USER_PW");
        System.out.println("INPUT_ID : " + id);
        System.out.println("INPUT_PW : " + pw);
        UserDAO dao = new UserDAO();
        List<UserDto> result = dao.loginUserCheck(id, pw);

        // 디버깅: 결과 확인
        if (result == null || result.isEmpty()) {
            System.err.println("Login result is Nodata or NULL.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // GET : 가입 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> memberCheck(@RequestParam String id) {
        System.out.println("회원 가입 여부 확인 ID : " + id);
        UserDAO dao = new UserDAO();
        boolean isTrue = dao.regMemberCheck(id);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }
    // GET : 이름 주민번호 받아서 아이디 조회
    @GetMapping("/findid")
    public ResponseEntity<String> memberList(@RequestParam String name, @RequestParam String jumin) {
        System.out.println("NAME : " + name);
        System.out.println("JUMIN : " + jumin);
        UserDAO dao = new UserDAO();
        String id = dao.findIDMethod(name, jumin);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
    // GET : 아이디 이름 주민번호 받아서 비밀번호
    @GetMapping("/findpw")
    public ResponseEntity<String> memberList(@RequestParam String id, @RequestParam String name, @RequestParam String jumin) {
        System.out.println("ID : " + id);
        System.out.println("NAME : " + name);
        System.out.println("JUMIN : " + jumin);
        UserDAO dao = new UserDAO();
        String pw= dao.findPWMethod(id, name, jumin);
        return new ResponseEntity<>(pw, HttpStatus.OK);
    }
    // POST : 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> userRegister(@RequestBody UserDto GenerateUser) {

        UserDAO dao = new UserDAO();
        boolean isTrue = dao.userRegister(GenerateUser);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 회원 탈퇴
    @PostMapping("/delete-user")
    public ResponseEntity<Boolean> userDeleteMethod(@RequestBody Map<String, String> delUser) {
        String getId = delUser.get("id");
        UserDAO dao = new UserDAO();
        boolean isTrue = dao.userDeleteMethod(getId);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

}
