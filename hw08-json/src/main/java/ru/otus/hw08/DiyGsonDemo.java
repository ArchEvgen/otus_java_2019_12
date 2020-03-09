package ru.otus.hw08;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class DiyGsonDemo {
    Gson gson = new Gson();
    DiyGson diyGson = new DiyGson();

    public static void main(String... args) throws Exception {
        var p1 = new Person();
        p1.setBirthDay(LocalDate.of(1988, 7, 12));
        p1.setName("John McClane");
        p1.setAbout(Map.of(
                "Slogan", "Yippie-Kai-Yay",
                "ArchEnemy", "Hans Gruber",
                "SuperPower", "Sarcasm"));

        var u1 = new User();
        u1.setBirthDay(LocalDate.of(1990, 2, 2));
        u1.setName("Holly Gennero");
        u1.setId(42);
        u1.setGrants(Set.of(Grants.READ, Grants.EXECUTE));
        u1.setInts(new int[]{42, 777, 13});

        var demo = new DiyGsonDemo();

        demo.testSerializeObject(p1);
        demo.testSerializeObject(u1);
        demo.testSerializeObject(6.3);
    }

    void testSerializeObject(Object o) {
        System.out.println("Test object: " + o.toString());
        System.out.println("Gson: " + gson.toJson(o));
        var json = diyGson.toJson(o);
        System.out.println("DiyGson: " + json);
        Object o2 = gson.fromJson(json, o.getClass());
        System.out.println("Deserialized object: " + o2.toString());
        System.out.println("Is equal: " + o2.equals(o));
        System.out.println();
    }
}
