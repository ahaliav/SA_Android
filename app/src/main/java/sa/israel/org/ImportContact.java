package sa.israel.org;

/**
 * Created by Ahaliav on 15/02/2018.
 */

public class ImportContact {
    public ImportContact(String name, String phoneNumber, String email){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    private int position = -1;

    public int getPosition() {
        return position;
    }
    public void setPosition(int pos) {
        position = pos;
    }

    private String name = "";

    public String getName() {
        return name;
    }


    private String phoneNumber = "";

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String email = "";

    public String getEmail() {
        return email;
    }
}
