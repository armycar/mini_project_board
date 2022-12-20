package service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

import data.Comment;
import data.Member;
import data.Post;

public class BoardService {
  public static Scanner scan = new Scanner(System.in);
  public static List < Post > postList = new ArrayList < Post > ();
  public static List < Comment > commentList = new ArrayList < Comment > ();
  public static MemberService mService = new MemberService();
  public static Integer nextPostNo = 1;
  public static Integer nextCommentNo = 1;
  public static Post currentPost; // 상세보기에서 사용할 객체

  public static void showPostList() {
    System.out.printf("%-5s %-20s %-20s %-16s\n", "번호", "제목", "작성자", "작성일");
    for (Post p: postList) {
      if (p.getPostStat() == 1)
        p.showPostInfo();
    }
  }

  public static Post getPost(Integer no) {
    for (Post p: postList) { // 전체 글 리스트를 순차조회
      if (p.getNo() == no) { // 파라미터 post_no와 같은 객체가 있다면
        return p; // 그 객체를 내어보내준다.
      }
    }
    return null; // 모두 조회하고나서도 글 번호가 같은 객체를 찾지 못하면 null이 리턴
  }

  public static void selectPost() throws Exception {
    System.out.print("글 번호 : ");
    // 글의 번호를 입력받는다 (인덱스가 아니라 PostInfo의 post_no 값)
    Integer sel = scan.nextInt();
    scan.nextLine();
    currentPost = getPost(sel); // 글 번호를 통해 글 검색
    if (currentPost == null) { // 
      System.out.println("존재하지 않는 글 번호 입니다.");
    } else if (currentPost.getPostStat() != 1) {
      System.out.println("차단되었거나 삭제된 글 입니다.");
    } else {
      currentPost.showPostDetail();
      for (Comment c: commentList) { //게시글번호와 댓글 게시글번호가 같은것 조회
        if (currentPost.getNo().equals(c.getPcPostNo()))
          System.out.println(c);
      }
      if (MemberService.loginMember != null) {
        detailMenu();
      }
    }
  }

  public static void commentManage() throws Exception { //일반댓글관리
    if(MemberService.loginMember.getState()==5) {
      System.out.println("탈퇴한 회원입니다");
      return;
    }
    System.out.println("<댓글관리>");
    for (Comment c: commentList) {
      if (c.getPcAuthor().equals(MemberService.loginMember.getId()))
        System.out.println(c);
    }
    System.out.print("1. 댓글삭제 / 0. 이전으로 : ");
    Integer sel = scan.nextInt();
    scan.nextLine();
    if (sel == 1) {
      System.out.print("삭제할 댓글번호 : ");
      Integer num = scan.nextInt();
      scan.nextLine();
      Comment delComment = null;
      for (Comment c: commentList) {
        if (c.getPcNo() == num && c.getPcAuthor().equals(MemberService.loginMember.getId())) {
          System.out.println(c);
          delComment = c;
        }
      }
      if (delComment == null) {
        System.out.println("잘못된 댓글 번호입니다.");
      } else { // 
        System.out.print("삭제하시겠습니까? (y/n) : ");
        String confirm = scan.nextLine();  
        if (confirm.equals("y")) {
          commentList.remove(delComment);
          System.out.println("삭제했습니다.");
          BufferedWriter writer = new BufferedWriter( 
            new OutputStreamWriter(
              new FileOutputStream(
                new File("data_files/comment.dat")
              ), "UTF-8"
            )
          );
          for(Comment comm : commentList) {
            writer.write(comm.makeDataString() + "\r\n");
          }
          writer.close();
        } else if (confirm.equals("n")) {
          System.out.println("삭제가 취소되었습니다");
        }
      }
    }
    else if (sel == 0) {
      return;
    }
  }

  public static void adminCommentManage() throws Exception { //운영자 댓글관리
    System.out.println("<댓글관리>");
    for (Comment c: commentList) {
        System.out.println(c);
    }
    System.out.print("1. 댓글삭제 / 0. 이전으로 : ");
    Integer sel = scan.nextInt();
    scan.nextLine();
    if (sel == 1) {
      System.out.print("삭제할 댓글번호 : ");
      Integer num = scan.nextInt();
      scan.nextLine();
      Comment delComment = null;
      for (Comment c: commentList) {
        if (c.getPcNo() == num) {
          System.out.println(c);
          delComment = c;
        }
      }
      if (delComment == null) {
        System.out.println("잘못된 댓글 번호입니다.");
      } else { // 
        System.out.print("삭제하시겠습니까? (y/n) : ");
        String confirm = scan.nextLine();  
        if (confirm.equals("y")) {
          commentList.remove(delComment);
          System.out.println("삭제했습니다.");
          BufferedWriter writer = new BufferedWriter( 
            new OutputStreamWriter(
              new FileOutputStream(
                new File("data_files/comment.dat")
              ), "UTF-8"
            )
          );
          for(Comment comm : commentList) {
            writer.write(comm.makeDataString() + "\r\n");
          }
          writer.close();
        } else if (confirm.equals("n")) {
          System.out.println("삭제가 취소되었습니다");
        }
      }
    }
    else if (sel == 0) {
      return;
    }
  }  

public static void detailMenu() throws Exception {
  while (true) {
    System.out.print("1. 댓글쓰기 / 2. 글수정 / 3. 글삭제 / 0. 이전화면: ");
    Integer sel = scan.nextInt();
    scan.nextLine();
    if (sel == 1) {
      addComment();
    }
    if (sel == 2) {
      updatePost();
    }
    if (sel == 3) {
      deletePost();
    }
    if (sel == 0) {
      break;
    }
  }
}

public static void updatePost() throws Exception {
  if (!currentPost.getAuthor().equals(MemberService.loginMember.getId())) {
    System.out.println("작성자만 글을 수정할 수 있습니다.");
    return; // 종료
  } 
    else if(MemberService.loginMember.getState()==5) {
      System.out.println("탈퇴한 회원입니다");
      return;
    }
    else if (currentPost.getPostStat() != 1) {
    System.out.println("삭제된 게시글 입니다");
    return;
  }
  System.out.print("변경할 제목을 입력해주세요 : ");
  String editTitle = scan.nextLine();
  System.out.print("변경할 내용을 입력해주세요 : ");
  String editText = scan.nextLine();
  System.out.print("변경 하시겠습니까 ? (y/n) : ");
  String confirm = scan.nextLine();
  if (confirm.equalsIgnoreCase("y")) {
    currentPost.setTitle(editTitle);
    currentPost.setText(editText);
    currentPost.setModdt(new Date());
    BufferedWriter writer = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(
          new File("data_files/post.dat")
        ), "UTF-8"
      )
    );
    for (Post p: postList) {
      writer.write(p.makeDataString() + "\r\n");
    }
    writer.close();
    System.out.println("글을 수정했습니다.");
  } else if (confirm.equalsIgnoreCase("n")) {
    System.out.println("변경이 취소되었습니다");
    return;
  }
}

public static void deletePost() throws Exception {
  if(currentPost.getAuthor().equals(MemberService.loginMember.getId())||MemberService.loginMember.getState()==0) {
    currentPost.setPostStat(2);
    BufferedWriter writer = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(
          new File("data_files/post.dat")
        ), "UTF-8"
      )
    );
    for (Post p: postList) {
      writer.write(p.makeDataString() + "\r\n");
    }
    writer.close();
    System.out.println("게시글이 삭제되었습니다");
    }
  else if (!currentPost.getAuthor().equals(MemberService.loginMember.getId())){
    System.out.println("작성자만 글을 삭제할 수 있습니다.");
    return; // 종료
  }
  else if(MemberService.loginMember.getState()==5) {
    System.out.println("탈퇴한 회원입니다");
    return;
  }
}

public static void addComment() throws Exception {
  if(MemberService.loginMember.getState()==5) {
    System.out.println("탈퇴한 회원입니다");
    return;
  }
  else if(MemberService.loginMember.getState()==3) {
    System.out.println("활동이 정지된 계정입니다");
    return;
  }
  else if (currentPost == null){
    return;
  }
  String id = MemberService.loginMember.getId();
  Integer pcPostNo = currentPost.getNo();
  System.out.print("댓글 내용 : ");
  String pcText = scan.nextLine();
  Comment c = new Comment(nextCommentNo, pcPostNo, pcText, id);
  commentList.add(c);
  System.out.println("댓글이 추가되었습니다.");
  BufferedWriter writer = new BufferedWriter(
    new OutputStreamWriter(
      new FileOutputStream(
        new File("data_files/comment.dat"), true
      ), "UTF-8"
    )
  );
  writer.write(c.makeDataString() + "\r\n");
  writer.close();
  nextCommentNo++;
}

public static void addPost() throws Exception {
  if(MemberService.loginMember.getState()==5) {
    System.out.println("탈퇴한 회원입니다");
    return;
  }
  else if(MemberService.loginMember.getState()==3) {
    System.out.println("활동이 정지된 계정입니다");
    return;
  }
  System.out.print("제목 입력 : ");
  String title = scan.nextLine();
  System.out.print("내용 입력 : ");
  String text = scan.nextLine();
  System.out.print("등록하시겠습니까? (y/n) : ");
  String confirm = scan.nextLine();
  if (confirm.equalsIgnoreCase("y")) {
    String m = MemberService.loginMember.getId();
    Post p = new Post(nextPostNo, title, text, m, 1);
    postList.add(p);
    System.out.println("게시글 등록이 완료 되었습니다");
    nextPostNo++;
    BufferedWriter writer = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(
          new File("data_files/post.dat"), true
        ), "UTF-8"
      )
    );
    writer.write(p.makeDataString() + "\r\n");
    writer.close();

  } else {
    System.out.println("게시글 등록이 취소되었습니다.");
  }
}

public static void loadPostData() throws Exception { //저장된 게시글 정보 불러오기
  BufferedReader reader = new BufferedReader(
    new InputStreamReader(
      new FileInputStream(
        new File("data_files/post.dat")
      ), "UTF-8"
    )
  );
  while (true) {
    String line = reader.readLine();
    if (line == null) break;
    String[] split = line.split(",");
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Integer no = Integer.parseInt(split[0]);
    String title = split[1];
    String text = split[2];
    String author = split[3];
    Date regdt = f.parse(split[4]);
    Date moddt = f.parse(split[5]);
    Integer postStat = Integer.parseInt(split[6]);
    Post p = new Post(no, title, text, author, postStat);
    p.setRegdt(regdt);
    p.setModdt(moddt);
    postList.add(p);
  }
  if (postList.size() == 0) //만약 게시글이 없다면 다음 게시글 번호를 1로 지정한다
    nextPostNo = 1;
  else
    nextPostNo = postList.get(postList.size() - 1).getNo() + 1; //만약 게시글이 있다면 다음 게시글번호에서 1을 추가해준다
}

public static void loadCommentData() throws Exception { //저장된 게시글 댓글 불러오기
  BufferedReader reader = new BufferedReader(
    new InputStreamReader(
      new FileInputStream(
        new File("data_files/comment.dat")
      ), "UTF-8"
    )
  );
  while (true) {
    String line = reader.readLine();
    if (line == null) break;
    String[] split = line.split(",");
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Integer pcNo = Integer.parseInt(split[0]);
    Integer pcPostNo = Integer.parseInt(split[1]);
    String pcText = split[2];
    Date pcRegdt = f.parse(split[3]);
    String pcAuthor = split[4];
    Comment c = new Comment(pcNo, pcPostNo, pcText, pcAuthor);
    c.setPcRegdt(pcRegdt);
    commentList.add(c);
  }
  if (commentList.size() == 0) //만약 게시글이 없다면 다음 게시글 번호를 1로 지정한다
    nextCommentNo = 1;
  else
    nextCommentNo = commentList.get(commentList.size() - 1).getPcNo() + 1; //만약 게시글이 있다면 다음 게시글번호에서 1을 추가해준다
}
}