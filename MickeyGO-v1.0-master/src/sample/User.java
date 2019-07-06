package sample;

import javafx.beans.property.SimpleStringProperty;

public class User {
    SimpleStringProperty username;
    SimpleStringProperty country;
    SimpleStringProperty win;
    SimpleStringProperty lose;

    public User(String username1, String country1, String win1, String lose1) {
        username = new SimpleStringProperty(username1);
        country = new SimpleStringProperty(country1);
        win = new SimpleStringProperty(win1);
        lose = new SimpleStringProperty(lose1);
    }

    public String getUsername() {
        return username.get();
    }

    public String getCountry() {
        return country.get();
    }

    public String getWin() {
        return win.get();
    }

    public String getLose() {
        return lose.get();
    }
}
