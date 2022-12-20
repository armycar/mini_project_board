package data;

public class Member { //회원정보 은닉
  private String name;
  private String birth;
  private String id;
  private String pwd;
  private Integer state;


  public void setName(String name) {
    this.name = name;
  }
  
  public void setBirth(String birth) {
    this.birth = birth;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getName() {
    return this.name;
  }

  public String getBirth() {
    return this.birth;
  }

  public String getId() {
    return this.id;
  }

  public String getPwd() {
    return this.pwd;
  }

  public Integer getState() {
    return this.state;
  }

  public Member(
    String id, String pwd, String name, String birth, Integer state) {
    this.id = id;
    this.pwd = pwd;
    this.name = name;
    this.birth = birth;
    this.state = state;
    }

    public String toString() {
      String str = "";
      String strState = "";
      if(state == 1) strState = "일반회원";
      else if(state == 2) strState = "우수회원";
      else if(state == 3) strState = "활동정지";
      else if(state == 4) strState = "영구정지";
      else if(state == 5) strState = "탈퇴처리";
      else if(state == 0) strState = "운영자";
      str += 
          "<회원정보>"+"\n"+
          "아이디 : "+id+"\n"+
          "이름(닉네임) : "+name+"\n"+
          "회원상태 : "+strState+"\n";
      return str;
    }

    public String makeDataString() { //데이터 저장용
      return id+","+pwd+","+name+","+birth+","+state;
      
  }
}

