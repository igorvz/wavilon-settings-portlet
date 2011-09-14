package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.model.User;
import com.fourspaces.couchdb.Document;

import java.util.Calendar;
import java.util.Random;


public class MappingUtil {
    private static Calendar calendar = Calendar.getInstance();
    public static Random random = new Random();

    public static Document toDocument(User user) {
        Document document = new Document();

        document.put("name", user.getName());
        document.put("surname", user.getSurname());
        document.put("phoneNumber", user.getPhoneNumber());

        //todo generate unique ID more correctly
        String uniqueParam = String.valueOf(calendar.getTimeInMillis() * random.nextInt(999));

        if (user.getId() == null) {
            document.setId(uniqueParam);
        }else {
            document.setId(user.getId());
            document.setRev(user.getRevision());
        }

        return document;
    }

    public static User toUser(Document document) {

        String id = document.getId();
        String revision = document.getRev();
        String name = document.getString("name");
        String surname = document.getString("surname");
        String phoneNumber = document.getString("phoneNumber");

        User user = new User();

        user.setId(id);
        user.setRevision(revision);
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phoneNumber);


        return user;
    }


}
