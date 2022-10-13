package com.xxx.dao;

import com.xxx.annotation.Repository;

@Repository
public class UserDaoImpl implements UserDao{
    @Override
    public void findUser() {

        System.out.println("成功层层调通，查询到了USER");

    }
}
