package com.ohgiraffers;

import com.ohgiraffers.config.JDBCConnection;
import com.ohgiraffers.view.UserView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Application {


    public static void main(String[] args) throws SQLException {
        Connection conn = JDBCConnection.getConnection(); //jdbc커넥션 파일에있는 정보들을 불러옴
        Scanner scanner = new Scanner(System.in); // 사용자에게 값을 입력받기위해 사용

        while (true) { // 조건이 true 이면 계속 반복됨
            System.out.println("\n===== LMS 관리 시스템 =====");
            System.out.println("1. 사용자(User) 관리");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int choice; //사용자가 입력한 값을 담아줄 변수 생성

            //숫자값이 아닌 다른값이들어오면 예외처리
            try {
                choice = scanner.nextInt(); // 숫자값 입력
                scanner.nextLine(); // 개행 문자 처리 (입력 버퍼에 남은 개행 문자(엔터)를 처리)
            } catch (InputMismatchException e) { //숫자가 아닌경우 NumberFormatException 를 일으킨다.
                //InputMismatchException = 입력받으려는 데이터 타입과 실제 입력된 데이터 타입이 맞지 않을 때 일으키는 인셉션

                System.out.println("숫자만 입력해야합니다. 다시 시도해주세요");
                scanner.nextLine(); //개행 문자 처리 (엔터쳐서 정리해줌)
                continue; //루프 처음으로 이동
                // (continue = 원래는 스킵 하고 다음으로 이동, 하지만 무한루프인 상황이라 다시 처음으로 돌아간다)
            }


            switch (choice) {  //switch : 표현식의 값에 따라 case 중 하나를 선택해 실행
                case 1 -> startUserManagement(conn);
                case 0 -> {
                    conn.close();
                    System.out.println("🚀 프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("❌ 잘못된 입력입니다. 다시 선택하세요.");
                //default -> case 중 아무것도 해당하지 않을때 기본적으로 실행
            }
        }
    }

    /*------------------------------------------------------------------------------------*/
    /**
     * 📌 사용자(User) 관리 시작
     * - 사용자(User) 관련 기능 실행
     */
    private static void startUserManagement(Connection conn) { //Connection conn = db 커넥션 정보를담고있는 매개변수
        UserView userView = new UserView(conn); // UserView 객체를 생성하며 db커넥션(연결)을 전달
        userView.showMenu(); // 사용자 관리 메뉴를 화면에 표시
    }

}

