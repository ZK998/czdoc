package info.tcjc.czdoc.entity;

import java.util.Date;

public class DocMaster {
    // `id`, `doc_name`, `doc_title`, `doc_auto`, `doc_date`, `creation_date`

    private int id;
    private String docName;
    private String docTitle;
    private String docAuth;
    private String docDate;
    private Date creationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocAuth() {
        return docAuth;
    }

    public void setDocAuth(String docAuth) {
        this.docAuth = docAuth;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
