package com.ohgiraffers.dao;

import com.ohgiraffers.model.User;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;

    }
    /*------------------------------------------------------------------------------------*/
    //전체유저 조회 메서드  = READ
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); //users 를 담아줄 리스트 생성
        String sql = "select * from users"; // 쿼리문

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getInt("role_id"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    /*------------------------------------------------------------------------------------*/
    //특정 유저 조회 메서드(이메일로 조회)  = READ
    public User getUserByEmail(String email) {
        String sql = "select * from users where email = ?"; //sql 쿼리 준비 ? 라는 빈칸을 미리 만들어둠
        User user = null; // user 정보를 담아줄 변수 생성
        //PreparedStatement SQL 쿼리를 미리 준비하고, 안전하게 값(매개변수)를 채워서 실행하는 JDBC의 도구
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);  // 숫자: n 번째 물음표 , 이메일
            ResultSet rs = ps.executeQuery(); // executeQuery()로 db에 조회
            //서 쿼리로 가져온 결과 표 처럼저장
            // rs.next() 표에 다음줄로 넘어가서 남았는지 체크

            //
            if (rs.next()) { // 다음 행(row)이 있는지 확인하고 있으면 그 행의 데이터를 읽을 준비.
                // 반복문이 아닌 if 문이라 한 행만 읽는다.
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getInt("role_id"),
                        rs.getTimestamp("created_at").toLocalDateTime() //.toLocalDateTime() 날짜 + 시간
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /*------------------------------------------------------------------------------------*/
    //사용자 추가 CREATE
    public boolean addUser(User user) {
        String sql = "";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRoleId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*------------------------------------------------------------------------------------*/
    //사용자 삭제 DELETE
    public boolean deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; //affectedRows가 0보다 크면(유저가 삭제되었으면) true를 반환

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*------------------------------------------------------------------------------------*/
    //사용자 수정 UPDATE
    public boolean editUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password_hash = ?, role_id = ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRoleId());
            ps.setInt(5, user.getUserId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

