package com.ohgiraffers.service;

import com.ohgiraffers.dao.UserDao;
import com.ohgiraffers.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    //private static final Logger log = LoggerFactory.getLogger(UserService.class); //로그 남기는 라이브러리
    private final UserDao userDao;
    private final Connection conn;

    /**
     * 📌 생성자 주입 (의존성 주입)
     */
    public UserService(Connection conn) {
        this.conn = conn;
        this.userDao = new UserDao(conn);
    }

    /*------------------------------------------------------------------------------------*/
    //전체유저 조회 메서드
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /*------------------------------------------------------------------------------------*/
    //특정 유저 조회 메서드(이메일로 조회)
    public User getUserByEmail(String email) throws SQLException {
        User user = userDao.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다.");
        }
        return user;
    }

    /*------------------------------------------------------------------------------------*/
    //사용자 등록   (이메일 중복 체크 후 추가)

    //이메일 유효성 검사
    public boolean notEmail(String email) {
        //정규표현식 (영어대,소문자 숫자 0-9 _.-) @ (영어대,소문자 숫자 0-9 _.-) . (영어 2글자 이상)
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        //이메일이 null 이 아니고 그리고(and연산자) 정규표현식과 일치하면 true 반환
        return email != null && email.matches(emailRegex);
    }
    //.matches() = 문자열이 특정 패턴에 맞는지 체크해서 true나 false를 반환하는 메서드


    //이메일 중복 확인
    public boolean emailCheck(String email) throws SQLException {
        List<User> u = userDao.getAllUsers(); // u 변수에 모든유저정보를 담아줌
        for (User existUser : u) {
            if (existUser.getEmail().equals(email))
            // u에 있는 유저정보를 하나씩 꺼내서 existUser 와 비교
            {
                return true; // 중복된 값이 있으면 true 반환
            }
        }
        return false; // 중복 없으면 false 반환
    }


    // 사용자 등록
    public boolean addUser(User user) throws SQLException {

        // 이메일 유효성 검사
        if (!notEmail(user.getEmail())) { //
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }  // ! NOT연산자  참 -> 거짓 , 거짓 -> 참
        //즉 입력한 이메일이 이메일형식이 아니면 익셉션을 발생

        // 이메일 중복 체크
        if (emailCheck(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        } //이메일이 중복이면 익셉션을 발생시킨다.


        //역할 ID검증 (숫자 2,3,만 허용)
        int roleId = user.getRoleId();
        if (roleId != 2 && roleId != 3) { //and 문과 not 문을 둘다사용  -> 2,3 이 둘다 아니면? 예외 발생
            throw new IllegalArgumentException("역할 ID는 학생:2,강사:3 만 등록 가능합니다");

        }
        //다 통과하면 반환해준다.
        return userDao.addUser(user);

    }

    /*------------------------------------------------------------------------------------*/

    //유저 삭제
    public boolean deleteUser(String email) throws SQLException {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("삭제할 사용자를 찾을 수 없습니다");
        }
        return userDao.deleteUser(email);
    }
    /*------------------------------------------------------------------------------------*/

    //유저 수정하기

    public boolean editUser(User user) throws SQLException {
        //유저 확인
        User existingUser = getUserByEmail(user.getEmail());
        if (existingUser == null) {
            throw new IllegalArgumentException("수정할 사용자를 찾을 수 없습니다.");
        }

        //유저 수정
        boolean result = userDao.editUser(user);
        if (!result) {
            throw new SQLException("수정하는 과정에서 오류가 발생되었습니다.");
        }
        return result;
    }
}


