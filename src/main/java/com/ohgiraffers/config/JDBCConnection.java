package com.ohgiraffers.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {
    // 1. 커넥션을 만들어주는 리소스를 생성함. (static)
    private static final HikariDataSource dataSource ;
    // * static은 프로그램과 생명주기가 같음 그래서 초기화 시점이 늦으면 안됨;
    static {
        try{
            // * 프로퍼티스 객체 생성
            Properties props = new Properties();
            // * hikari config 필요?
            HikariConfig config = new HikariConfig();
            // 1-1. 프로퍼티스 읽어오기
            props.load(JDBCConnection.class.getClassLoader().getResourceAsStream("config.properties"));
            // 1-2. 커넥션 설정 정보(아이디)
            config.setUsername(props.getProperty("username"));
            // 1-2. 커넥션 정보(비밀번호)
            config.setPassword(props.getProperty("password"));
            // 1-3. 커넥션 정보 (url)
            config.setJdbcUrl(props.getProperty("url"));
            // 2. 커넥션 객체 생성
            dataSource = new HikariDataSource(config);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    // 커넥션 풀에서 연결되어 있는 객체를 꺼내오기
    public static Connection getConnection() throws SQLException {
        // 1. 데이터 소스에서 커넥션 꺼내기
        Connection connection = dataSource.getConnection();
        // 2. 반환
        return connection;
    }

    // 커넥션 객체 닫아주기
    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}