package com.tp.services;

import com.tp.models.User1;
import com.tp.models.Users;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private static final String URL = "https://randomuser.me/api/?results=1";

    public Users[] fetchURL () {
        RestTemplate restTemplate = new RestTemplate();
        User1[] user1 = restTemplate.getForObject(URL, User1[].class);
        System.out.println(user1);
        Users[] users = new Users[1];
        return users;
    }

    public List<Users> getUsers() {
        List<Users> usersList = Arrays.asList(this.fetchURL());
        return usersList;
    }

}
