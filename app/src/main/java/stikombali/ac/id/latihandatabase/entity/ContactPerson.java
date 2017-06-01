package stikombali.ac.id.latihandatabase.entity;

import com.orm.SugarRecord;

/**
 * Created by USER on 30/05/2017.
 */

public class ContactPerson extends SugarRecord<ContactPerson> {

    String firstName;
    String lastName;
    String address;
    String phone;
    String email;
    String dob;
    String sex;
    String photo;

    public ContactPerson() {

    }

    public ContactPerson(String firstName, String lastName, String address, String phone, String email
            ,String dob,String sex,String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.sex = sex;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getSex() {
        return sex;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
