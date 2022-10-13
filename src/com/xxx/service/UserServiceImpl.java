package com.xxx.service;

import com.xxx.annotation.Autowired;
import com.xxx.annotation.Service;
import com.xxx.dao.UserDao;

@Service
public class UserServiceImpl  implements UserService{
    @Autowired
    private UserDao userDao;
    @Override
    public void findUser() {

        userDao.findUser();
    }
}
