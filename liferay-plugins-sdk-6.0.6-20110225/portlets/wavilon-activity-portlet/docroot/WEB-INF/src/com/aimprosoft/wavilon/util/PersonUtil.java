package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.model.Person;

import java.util.LinkedList;
import java.util.List;

public class PersonUtil {

    private static String[] fnames = {"Peter", "Alice", "Joshua", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
            "Lisa", "Marge"};
    private static String[] lnames = {"Smith", "Gordon", "Simpson", "Brown", "Clavel",
            "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
            "Barks", "Ross", "Schneider", "Tate"};

    private static String[] categories = {"Support", "Problematic customer", "Human Resources", "Design Agency",
            "Designers", "Delivery", "Fashion", "Software", "Web Apps", "Magazines", "Music", "Portfolio"};

    private static String[] times = {"9:24", "8:52", "6:35", "4:65",
            "9:56", "18:22", "23:01", "14:40", "15;45"};

    private static String[] avatarNames = {"face1.png", "face2.png", "face3.png", "face4.png"};


    public static Person createRandomPerson(String id) {
        Person person = new Person();
        person.setName(fnames[(int) (fnames.length * Math.random())]);
        person.setSurname(lnames[(int) (lnames.length * Math.random())]);
        person.setTime(times[(int) (times.length * Math.random())]);
        person.setAvatarName(avatarNames[(int) (avatarNames.length * Math.random())]);
        person.setId(id);

        List<String> personsCategories = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            personsCategories.add(categories[(int) (categories.length * Math.random())]);
        }
        person.setCategories(personsCategories);

        return person;
    }
}
