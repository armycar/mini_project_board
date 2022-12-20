package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import data.Comment;
import data.Member;

public class MemberService {
  public static Scanner scan = new Scanner(System.in);
  public static List<Member> memberList = new ArrayList<Member>();
  public static Member loginMember = null; //로그인상태, 기본값을 null로 지정
  
  public static void memberJoin() throws Exception {
    String id = ""; //while문 밖에서도 사용할수있도록 기본값 설정
    String pwd = ""; 
    String name = ""; 
    String birth = ""; 
    System.out.println("==회원가입=====");
    while(true) {
    System.out.print("아이디를 입력(6글자이상) : ");
    id = scan.nextLine();
    if(id.length()<6){
    System.out.println("아이디는 6글자 이상이어야 합니다.");
    }
    else {
      if(isDuplicatedId(id)==true) {
        System.out.println("이미 등록된 아이디 입니다."); 
      } else {
        break;
        }
      }
    }
    while(true) {
    System.out.print("비밀번호를 입력(8글자이상) : ");
    pwd = scan.nextLine();
    if(pwd.length()<8){
      System.out.println("비밀번호는 8글자 이상이어야 합니다.");
      }
      else {
        break;
      }
    }

    while(true) {
    System.out.print("이름(닉네임)을 입력 : ");
    name = scan.nextLine();
    if(name.length()<2){
      System.out.println("이름(닉네임)은 2자리 이상입니다");
    }
    else {
      if(isDuplicatedMember(name)==true) {
        System.out.println("이미 등록된 이름(닉네임) 입니다.");
      } else {
        break;
          }
        }
      }

    while(true) {
    System.out.print("주민등록번호을 입력(-제외) : ");
    birth = scan.nextLine();
    if(birth.length()!=13){
      System.out.println("주민등록번호는 13자리 입니다");
      }
      else{
        if(isDuplicatedMember(birth)==true) {
          System.out.println("이미 등록된 사용자 정보 입니다.");
        } else {
          break;
        }
      }
    }

    Member m = new Member(id, pwd, name, birth, 1);
    memberList.add(m);
    System.out.println("회원 등록이 완료되었습니다");

    BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(
            new File("data_files/member.dat"), true
          ), "UTF-8"
        )
      );
      writer.write(m.makeDataString()+"\r\n");
      writer.close();
  }

  public static void loadMemberData() throws Exception { //저장된 회원정보를 불러오기
    BufferedReader reader = new BufferedReader(
      new InputStreamReader(
        new FileInputStream(
          new File("data_files/member.dat")
        ), "UTF-8"
      )
    );
    while(true) {
      String line = reader.readLine();
      if(line == null) break;
      String[] split = line.split(",");
      String id = split[0];
      String pwd = split[1];
      String name = split[2];
      String birth = split[3];
      Integer state = Integer.parseInt(split[4]);
      Member m = new Member(id, pwd, name, birth, state);
      memberList.add(m);
    }
  }

  public static void login() {
    System.out.print("아이디를 입력 : ");
    String id = scan.nextLine();
    System.out.print("비밀번호를 입력 : ");
    String pwd = scan.nextLine();
    for(Member m : memberList) { //데이터타입 임시변수명 : 배열명 
      if(m.getId().equals(id) && m.getPwd().equals(pwd)) {
        loginMember = m; //로그인성공시 임시변수 대입
                }
          } 
        if(loginMember == null) {
          System.out.println("아이디 또는 비밀번호를 확인해주세요");
        }  
        }

  public static void logout() {
    loginMember = null;
    System.out.println("로그아웃 되었습니다");
  }      

  public static Boolean isDuplicatedId(String id) {
    for(Member m : memberList) {
      if(m.getId().equals(id)) return true; //반복문으로 중복찾기
    }
    return false;
  }
  
  public static Boolean isDuplicatedName(String name) {
    for(Member m : memberList) {
      if(m.getName().equals(name)) return true; //반복문으로 중복찾기
    }
    return false;
  }

  public static Boolean isDuplicatedMember(String birth) {
    for(Member m : memberList) {
      if(m.getBirth().equals(birth)) return true; //반복문으로 중복찾기
    }
    return false;
  }
          
 public static void myPage() throws Exception{ 
  for(Member m : memberList) 
  { 
    if(m.getId().equals(loginMember.getId())&&m.getPwd().equals(loginMember.getPwd()))
     System.out.println(m);
  }  
  while(true) {
   System.out.print("1. 비밀번호 변경 / 2. 닉네임 변경 / 3. 댓글관리 / 4. 회원탈퇴 / 0. 이전화면 : ");
   Integer sel = scan.nextInt(); scan.nextLine();
   if(sel == 1) {editPwd();}
   else if(sel == 2) {editName();}
   else if(sel == 3) {BoardService.commentManage();}
   else if(sel == 4) {deleteMember();}
   else if(sel == 0) {break;}
  }
}

public static void editPwd() throws Exception{
  if(MemberService.loginMember.getState()==5) {
    System.out.println("탈퇴한 회원입니다");
    return;
  }
  while(true) {
  System.out.print("기존 비밀번호를 입력 : ");
  String pwd = scan.nextLine();
    if(pwd.equals(loginMember.getPwd())) {    
      System.out.print("새로운 비밀번호를 입력(8글자이상): ");
      String editpwd = scan.nextLine();
        if(editpwd.length()<8){
          System.out.println("비밀번호는 8글자 이상이어야 합니다.");
          continue;
          }else {
      System.out.print("변경하시겠습니까? (y/n) : ");
      String confirm = scan.nextLine();
      if(confirm.equalsIgnoreCase("y")) {
      loginMember.setPwd(editpwd);

      BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(
            new File("data_files/member.dat")
          ), "UTF-8"
        )
      );
        for(Member m : memberList) {
      writer.write(m.makeDataString()+"\r\n");
    }
    writer.close();
  

      System.out.println("변경이 완료되었습니다");
      break;
      }
      else if(confirm.equalsIgnoreCase("n")) {
        System.out.println("변경이 취소되었습니다");
        break;
      }
    }
  }
    else {
      System.out.println("비밀번호를 확인해주세요");
      break;
    }
  }
}

public static void editName() throws Exception{
  if(MemberService.loginMember.getState()==5) {
    System.out.println("탈퇴한 회원입니다");
    return;
  }
  while(true) {
    System.out.print("새로운 이름(닉네임)을 입력 : ");
    String name = scan.nextLine();
    if(name.length()<2){
      System.out.println("이름(닉네임)은 2자리 이상입니다");
    }
    else {
      if(isDuplicatedMember(name)==true) {
        System.out.println("이미 등록된 이름(닉네임) 입니다.");
      } else {
        System.out.print("변경하시겠습니까? (y/n) : ");
        String confirm = scan.nextLine();
        if(confirm.equalsIgnoreCase("y")) {
        loginMember.setName(name);
        BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(
            new File("data_files/member.dat")
          ), "UTF-8"
        )
      );
        for(Member m : memberList) {
      writer.write(m.makeDataString()+"\r\n");
    }
    writer.close();
        System.out.println("변경이 완료되었습니다");
        break;
        }
        else if(confirm.equalsIgnoreCase("n")) {
          System.out.println("변경이 취소되었습니다");
          break;
        }
        break;
          }
        }
      }
}
public static void deleteMember() throws Exception{
  if(MemberService.loginMember.getState()==5) {
    System.out.println("이미 탈퇴한 회원입니다");
    return;
  }
 while(true) {
  System.out.print("비밀번호를 입력 : ");
  String pwd = scan.nextLine();
  if(pwd.equals(loginMember.getPwd())) {
    System.out.print("정말 탈퇴하시겠습니까?(y/n) : ");
        String confirm = scan.nextLine();
        if(confirm.equalsIgnoreCase("y")) {
          loginMember.setState(5);
          BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(
            new File("data_files/member.dat")
          ), "UTF-8"
        )
      );
        for(Member m : memberList) {
      writer.write(m.makeDataString()+"\r\n");
    }
    writer.close();
          System.out.println("탈퇴 처리되었습니다");
          break;
        }
        else if(confirm.equalsIgnoreCase("n")) {
          System.out.println("탈퇴가 취소되었습니다");
          break;
        }
        else 
          System.out.println("잘못된 접근입니다");
          continue;
            }
            else
            System.out.println("비밀번호가 잘못되었습니다");
        }
      }

      public static void memberManage() throws Exception{
        while(true) {
        System.out.print("1. 회원정보 조회 / 2. 회원등급 변경 / 0. 이전화면 : ");
        Integer sel = scan.nextInt(); scan.nextLine();
        if(sel == 1) {
          for(Member m : memberList) 
           { 
              System.out.println(m);
           }
        }
        else if(sel == 2) {changeGrade();}
        else if(sel == 0) {
          break;
        }
        else {
          System.out.println("선택이 잘못되었습니다(0~2)");
          return;
        }
      }
    }

    public static void changeGrade() throws Exception{
      System.out.print("변경할 회원 아이디 입력 : ");
      String memberId = scan.nextLine();
      for(Member m2 : memberList) { //데이터타입 임시변수명 : 배열명 
        if(m2.getId().equals(memberId)) {
            System.out.print("1. 일반회원 / 2. 우수회원 / 3. 활동정지 / 4. 영구정지 / 5. 탈퇴처리 : ");
            Integer sel = scan.nextInt(); scan.nextLine();
            m2.setState(sel);
            System.out.println("변경이 완료 되었습니다");
            BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(
            new FileOutputStream(
              new File("data_files/member.dat")
            ), "UTF-8"
          )
        );
          for(Member m : memberList) {
        writer.write(m.makeDataString()+"\r\n");
      }
      writer.close();
                  }
            } 
    }
}

