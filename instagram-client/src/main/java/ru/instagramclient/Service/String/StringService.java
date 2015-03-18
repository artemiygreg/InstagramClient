package ru.instagramclient.Service.String;

/**
 * Created by Admin on 17.03.15.
 */
public class StringService {

    public StringService() {

    }
    public static Long parseString(String s){
        String arr[] = s.split("_");

        return Long.parseLong(arr[0]);
    }
}
