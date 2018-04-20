package com.example.service;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/25.
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message"+String.valueOf(userId);
    }
}
