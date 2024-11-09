package com.example.appranduser;

import java.util.ArrayList;
import java.util.Date;

public class User{
    public ArrayList<Result> results;
    public Info info;
}

class Coordinates{
    public String latitude;
    public String longitude;
}

class Dob{
    public Date date;
    public int age;
}

class Id{
    public String name;
    public String value;
}

class Info{
    public String seed;
    public int results;
    public int page;
    public String version;
}

class Location{
    public Street street;
    public String city;
    public String state;
    public String country;
    public String postcode;
    public Coordinates coordinates;
    public Timezone timezone;
}

class Login{
    public String uuid;
    public String username;
    public String password;
    public String salt;
    public String md5;
    public String sha1;
    public String sha256;
}

class Name{
    public String title;
    public String first;
    public String last;
}

class Picture{
    public String large;
    public String medium;
    public String thumbnail;
}

class Registered{
    public Date date;
    public int age;
}

class Result{
    public String gender;
    public Name name;
    public Location location;
    public String email;
    public Login login;
    public Dob dob;
    public Registered registered;
    public String phone;
    public String cell;
    public Id id;
    public Picture picture;
    public String nat;
}

class Street{
    public int number;
    public String name;
}

class Timezone{
    public String offset;
    public String description;
}