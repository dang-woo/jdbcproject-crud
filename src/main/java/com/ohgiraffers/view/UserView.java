package com.ohgiraffers.view;

import com.ohgiraffers.model.User;
import com.ohgiraffers.service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private final UserService userService;
    private final Scanner scanner;

    public UserView(Connection conn) {
        this.userService = new UserService(conn);
        this.scanner = new Scanner(System.in);
    }

    //메뉴 보여주는화면
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 사용자 관리 시스템 =====");
            System.out.println("1. 전체 사용자 조회");
            System.out.println("2. 특정 사용자 조회");
            System.out.println("3. 사용자 등록");
            System.out.println("4. 사용자 수정");
            System.out.println("5. 사용자 삭제");
            System.out.println("0. 종료");
            System.out.print("선택하세요: ");


            int choice; // 사용자가 입력한 값을 담아줄 변수 생성
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력해야합니다. 다시 시도해주세요");
                scanner.nextLine();
                continue;
            }


            switch (choice) {
                case 1 -> getAllUsers();
                case 2 -> getUserByEmail();
                case 3 -> addUser();
                case 4 -> editUser();
                case 5 -> deleteUser();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }


        }

    }


    /**
     * 📌 1.전체 사용자 조회
     * - `UserService`의 `getAllUsers()` 메서드를 호출하여 사용자 목록을 출력
     */
    private void getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) { //"".isEmpty는 문자열이 비어 있으면 true를 반환합니다.
            System.out.println("등록된 사용자가 없습니다.");
        } else {
            System.out.println("\n===== 전체 사용자 목록 =====");
            users.forEach(user -> System.out.println(user));
        }

    }

    /**
     * 📌 2. 단일 사용자 조회 (이메일로 조회)
     **/
    private void getUserByEmail() {
        System.out.println("조회할 사용자 이메일을 입력해주세요");
        String email = scanner.nextLine();
        // 이메일 유효성 검사 정규표현식 (영어대,소문자 숫자 0-9 _.-) @ (영어대,소문자 숫자 0-9 _.-) . (영어대소문자 2글자 이상)
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        if (email == null || email.isEmpty() || !email.matches(emailRegex)) {
            // null 이거나 " "(공백)이거나 이메일 형식이 아닐경우 ||는 or 메서드
            System.out.println("올바른 이메일 형식이 아닙니다");
            return;
        }

        try {
            User user = userService.getUserByEmail(email); //유저서비스에서 getUserByEmail를 받아와 유저변수에 담아줌
            System.out.println("\n===== 사용자 정보 =====");
            System.out.println(user); //받아온 유저 보여주기
        } catch (SQLException e) {
            System.out.println("사용자 조회 중 오류가 발생하였습니다");
        }

    }

    /**
     * 📌사용자 등록
     */
    private void addUser() {
        // 변수선언
        String email;
        int roleId;

        // 이메일 중복 확인
        while (true) {
            // 이메일 입력 받는 폼 생성

            System.out.println("*주의사항* : 가입시 이메일은 변경 할 수 없습니다");
            System.out.print("이메일: ");
            email = scanner.nextLine();

            try {
                // 이메일 유효성 검사
                if (!userService.notEmail(email)) {
                    System.out.println("올바른 이메일 형식이 아닙니다");
                    continue;
                }
                // 이메일 중복 확인
                if (userService.emailCheck(email)) {
                    System.out.println("이미 존재하는 이메일입니다.");
                } else {
                    System.out.println("사용 가능한 이메일입니다.");
                    break;
                }
            } catch (SQLException e) {
                System.out.println("이메일 확인 중 오류가 발생했습니다");
                return; // 메서드 종료
            }
        }

        //이름 비밀번호 역할 값을 받는 폼 생성
        System.out.print("사용자 이름: ");
        String username = scanner.nextLine();


        System.out.print("비밀번호: ");
        String password = scanner.nextLine();


        // 역할 아이디 숫자형 입력 (서비스에서 해야할것같은데 이미 뷰에서 만들어버림..)
        while (true) {
            System.out.print("역할 ID:  '학생: 2 , 강사: 3 ' :");
            try {
                roleId = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력해야 합니다. 학생: 2 , 강사: 3 중 하나를 선택해주세요.");
                scanner.nextLine();
            }
        }

        User user = new User(0, username, email, password, roleId, null);
        try {
            boolean success = userService.addUser(user);
            if (success) {
                System.out.println("사용자가 성공적으로 등록되었습니다.");
            }
        } catch (Exception e) {
            System.out.println("사용자 등록 중 오류가 발생했습니다.");
        }


    }

    /**
     * 📌 사용자 수정
     */
    private void editUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("수정할 유저의 이메일을 입력해주세요");
        String email = scanner.nextLine();

        User existingUser;
        try {
            existingUser = userService.getUserByEmail(email); // 기존 사용자 조회
            if (existingUser == null) {
                System.out.println("해당 이메일로 등록된 사용자가 없습니다.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("사용자 조회 중 오류가 발생했습니다.");
            return;
        }

        System.out.println("현재 이메일: " + existingUser.getEmail() + " (이메일은 수정 불가)");
        System.out.print("새로운 사용자 이름: ");
        String username = scanner.nextLine();

        System.out.print("새로운 비밀번호: ");
        String password = scanner.nextLine();

        System.out.print("새로운 역할 ID: ");
        int roleId;
        try {
            roleId = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리
        } catch (InputMismatchException e) {
            System.out.println("역할 ID는 숫자여야 합니다.");
            return;
        }

        // 이메일은 기존 값 유지
        User user = new User(existingUser.getUserId(), username, existingUser.getEmail(), password, roleId, null);
        try {
            boolean success = userService.editUser(user);
            if (success) {
                System.out.println("사용자 정보가 성공적으로 수정되었습니다.");
            } else {
                System.out.println("사용자 정보 수정에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("사용자 정보 수정 중 오류가 발생했습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * 📌사용자 삭제
     * - 사용자 이메일을 입력받아 삭제
     */
    private void deleteUser() {


        System.out.println("삭제할 사용자 이메일을 입력하세요: ");
        String email = scanner.nextLine(); // 이메일 입력
        scanner.nextLine(); // 개행 문자 처리

        try {
            boolean success = userService.deleteUser(email);
            if (success) {
                System.out.println("사용자가 성공적으로 삭제되었습니다");
            } else {
                System.out.println("사용자 삭제에 실패하였습니다");
            }
        }catch (Exception e) {
            System.out.println("사용자 삭제 중 오류가 발생했습니다.");
        }

    }

}
