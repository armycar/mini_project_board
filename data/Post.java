package data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Post{
  private Integer no;
  private String title;
  private String text;
  private String regdt;
  private String moddt;
  private String author;
  private Integer postStat;
  SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public Post() {}

  public Post(Integer no, String title, String text, String author, Integer postStat) {
    this.no = no;
    this.title = title;
    this.text = text;
    setRegdt(new Date());
    setModdt(new Date());
    this.author = author;
    this.postStat = postStat;
}

  public Integer getPostStat() {
    return this.postStat;
  }

  public void setPostStat(Integer postStat) {
    this.postStat = postStat;
  }

  public Integer getNo() {
    return this.no;
  }

  public void setNo(Integer no) {
    this.no = no;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  // public Date getRegdt() {
  //   return this.regdt;
  // }

  public void setRegdt(Date regdt) {
    this.regdt = f.format(regdt);
  }

  // public Date getModdt() {
  //   return this.moddt;
  // }

  public void setModdt(Date moddt) {
    this.moddt = f.format(moddt);
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void showPostInfo() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.printf("%-5s %-20s %-20s %-16s\n", no, title, author, regdt);
  }

  public void showPostDetail() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    // 글 상세 정보 출력
    System.out.println("-------------------------------------------------------------------------------------------------");
    System.out.println("제목 : "+title+" / 작성자 : "+author+" / 등록일 : "+regdt+" / 수정일 : "+moddt);
    System.out.println("내용 : " + text);
    System.out.println("-------------------------------------------------------------------------------------------------");
}

  public String makeDataString() { //데이터 저장용
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return no+","+title+","+text+","+author+","+regdt+","+moddt+","+postStat;
    
}
}
