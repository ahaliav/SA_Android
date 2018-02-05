package sa.israel.org;

/**
 * Created by ahaliav_fox on 14 נובמבר 2017.
 */

public class EmailContact {
    public EmailContact(String email, String title, String subject, String content){
        this.email = email;
        this.title = title;
        this.subject = subject;
        this.content = content;
    }

    private String email = "";
    public String getEmail(){
        return email;
    }

    private String title = "";
    public String getTitle(){
        return title;
    }

    private String subject = "";
    public String getSubject(){
        return subject;
    }

    private String content = "";
    public String getContent(){
        return content;
    }
}
