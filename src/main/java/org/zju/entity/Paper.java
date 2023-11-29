package org.zju.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * Paper类
 * @author Tao
 * @date 2023/11/27
 */
@Data
@AllArgsConstructor
public class Paper {
    private Integer id;
    private String AnthologyID;
    private String Volume;
    private String Month;
    private String Year;
    private String Address;
    private String Editors;
    private String Venue;
    private String SIG;
    private String Publisher;
    private String Note;
    private String Pages;
    private String Language;
    private String URL;
    private String DOI;
    private String Bibkey;
    private String CiteACL;
    private String CiteInformation;
    private String CopyCitation;
    private String PDF;
    private String Software;
    private String Video;
    private String Code;
    private String Data;
    private String Content;

    /**
     * 组装Paper对象
     * @param map
     */
    public Paper(HashMap<String,String> map){
        this.AnthologyID = map.get("Anthology ID");
        this.Volume = map.get("Volume");
        this.Month = map.get("Month");
        this.Year = map.get("Year");
        this.Address = map.get("Address");
        this.Editors = map.get("Editors");
        this.Venue = map.get("Venue");
        this.SIG = map.get("SIG");
        this.Publisher = map.get("Publisher");
        this.Note = map.get("Note");
        this.Pages = map.get("Pages");
        this.Language = map.get("Language");
        this.URL = map.get("URL");
        this.DOI = map.get("DOI");
        this.Bibkey = map.get("Bibkey");
        this.CiteACL = map.get("Cite (ACL)");
        this.CiteInformation = map.get("Cite (Informal)");
        this.CopyCitation = map.get("Copy Citation");
        this.PDF = map.get("PDF");
        this.Software = map.get("Software");
        this.Video = map.get("Video");
        this.Code = map.get("Code");
        this.Data = map.get("Data");
        this.Content = "-";
    }

    /**
     * 创建一个paper模板
     * @return {@link HashMap}
     */
    public static HashMap CreatePaper(){
        return new HashMap(){{
            put("Anthology ID","-");
            put("Volume","-");
            put("Month","-");
            put("Year","-");
            put("Address","-");
            put("Editors","-");
            put("Venue","-");
            put("SIG","-");
            put("Publisher","-");
            put("Note","-");
            put("Pages","-");
            put("Language;","-");
            put("URL","-");
            put("DOI","-");
            put("Bibkey","-");
            put("Cite (ACL)","-");
            put("Cite (Informal)","-");
            put("Copy Citation","-");
            put("PDF","-");
            put("Software","-");
            put("Video","-");
            put("Code","-");
            put("Data","-");
        }};
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", AnthologyID='" + AnthologyID + '\'' +
                ", Volume='" + Volume + '\'' +
                ", Month='" + Month + '\'' +
                ", Year='" + Year + '\'' +
                ", Address='" + Address + '\'' +
                ", Editors='" + Editors + '\'' +
                ", Venue='" + Venue + '\'' +
                ", SIG='" + SIG + '\'' +
                ", Publisher='" + Publisher + '\'' +
                ", Note='" + Note + '\'' +
                ", Pages='" + Pages + '\'' +
                ", Language='" + Language + '\'' +
                ", URL='" + URL + '\'' +
                ", DOI='" + DOI + '\'' +
                ", Bibkey='" + Bibkey + '\'' +
                ", CiteACL='" + CiteACL + '\'' +
                ", CiteInformation='" + CiteInformation + '\'' +
                ", CopyCitation='" + CopyCitation + '\'' +
                ", PDF='" + PDF + '\'' +
                ", Software='" + Software + '\'' +
                ", Video='" + Video + '\'' +
                ", Code='" + Code + '\'' +
                ", Data='" + Data + '\'';
    }
}
