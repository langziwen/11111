package com.lzw.dao;

import java.util.List;

import javax.annotation.Resource;
import com.lzw.jdbcbean.User;

public interface UserDao {
	
	public User findByUserName(String userName);
	
	public List<User> findFriendsById(String id); 
}
