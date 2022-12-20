import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import data.Post;
import data.Member;
import service.BoardService;
import service.MemberService;

public class Main {
  public static Scanner scan = new Scanner(System.in);
  public static void main(String[] args) throws Exception{
    MemberService.loadMemberData();
    BoardService.loadPostData();
    BoardService.loadCommentData();
    while(true) {
      mainScreen();
    }
  }

  public static void mainScreen() throws Exception{
    while(true) {
      String loginInfo = "";
      if(MemberService.loginMember != null&&MemberService.loginMember.getState()==0) { //운영자
        loginInfo = MemberService.loginMember.getName()+"("+MemberService.loginMember.getId()+")";
        System.out.println("(회원 정보 : "+loginInfo+")");        
        BoardService.showPostList();
        System.out.println();
        System.out.print("1. 상세보기 / 2. 글쓰기 / 3. 회원관리 / 4. 댓글관리 / 5. 로그아웃 : " );
        int sel = scan.nextInt();
        if(sel ==1) 
              {BoardService.selectPost();}
        else if(sel == 2) 
              {BoardService.addPost();}
        else if(sel == 3)  
              {MemberService.memberManage();}
        else if(sel == 4)
              {BoardService.adminCommentManage();}      
        else if(sel == 5)   
              MemberService.logout();
        else 
        System.out.println("메뉴 번호를 확인해주세요(1~4번)");   
        break;
      }

      else if(MemberService.loginMember != null) { //일반회원
        loginInfo = MemberService.loginMember.getName()+"("+MemberService.loginMember.getId()+")";
        System.out.println("(회원 정보 : "+loginInfo+")");        
        BoardService.showPostList();
        System.out.println();
        System.out.print("1. 상세보기 / 2. 글쓰기 / 3. 마이페이지 / 4. 로그아웃 : " );
        int sel = scan.nextInt();
        if(sel ==1) 
              {BoardService.selectPost();}
        else if(sel == 2) 
              {BoardService.addPost();}
        else if(sel == 3)  
              MemberService.myPage();
        else if(sel == 4)   
              MemberService.logout();
        else 
        System.out.println("메뉴 번호를 확인해주세요(1~4번)");   
        break;
      }

      else {
        loginInfo = "비회원";
        System.out.println("(회원 정보 : "+loginInfo+")");
        BoardService.showPostList();
        System.out.println();
        System.out.print("1. 상세보기 / 2. 회원가입 / 3. 로그인 : ");
        int sel = scan.nextInt();
        if(sel == 1) {BoardService.selectPost();}
        else if(sel ==2)
            MemberService.memberJoin();
        else if(sel == 3)
            MemberService.login();
            
        else    
        System.out.println("메뉴 번호를 확인해주세요(1~3번)"); 
      }
    }
  }
}
