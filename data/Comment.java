package data;

import java.text.SimpleDateFormat;
import java.util.*;

public class Comment {
    private Integer pcNo;
    private Integer pcPostNo;
    private String pcText;
    private String pcRegdt;
    private String pcAuthor;
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Comment() {}
    public Comment(Integer pcNo, Integer pcPostNo, String pcText, String pcAuthor) {
        this.pcNo = pcNo;
        this.pcPostNo = pcPostNo;
        this.pcText = pcText;
        setPcRegdt(new Date());
        this.pcAuthor = pcAuthor;
    }

    public Integer getPcNo() {
        return this.pcNo;
    }

    public void setPcNo(Integer pcNo) {
        this.pcNo = pcNo;
    }

    public Integer getPcPostNo() {
        return this.pcPostNo;
    }

    public void setPcPostNo(Integer pcPostNo) {
        this.pcPostNo = pcPostNo;
    }

    public String getPcText() {
        return this.pcText;
    }

    public void setPcText(String pcText) {
        this.pcText = pcText;
    }

    public String getPcRegdt() {
        return this.pcRegdt;
    }

    public void setPcRegdt(Date pcRegdt) {
        this.pcRegdt = f.format(pcRegdt);
    }

    public String getPcAuthor() {
        return this.pcAuthor;
    }

    public void setPcAuthor(String pcAuthor) {
        this.pcAuthor = pcAuthor;
    }

    public String toString() {
        String str = "";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        str += pcNo+". "+ pcAuthor+" / "+pcRegdt+"\n";
        str += pcText+"\n";
        return str;
    }

    public String makeDataString() { //데이터 저장용
       
        return pcNo+","+pcPostNo+","+pcText+","+pcRegdt+","+pcAuthor;
    }
}
