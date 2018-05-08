package com.wenfengSAT.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.wenfengSAT.api.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    public User findByUsername(String username);
    
    //@Query("from User u inner join fetch u.dept as d where d.id=?1") 
    //List<User> findByDeptId(Integer deptId);
    
    @Query("UPDATE User u SET u.password=?2 WHERE u.username=?1")  
    @Modifying  
    @Transactional  
    void updatePwd(String username, String password);

    @Query("DELETE FROM User u WHERE u.username=?1")  
    @Modifying  
    @Transactional  
    void deleteByUserName(String userName);

    @Query("UPDATE User u SET u.password= :password WHERE u.username = :username")  
    @Modifying  
    @Transactional  
    void updateEmail(@Param("username") String userName, @Param("password") String password);  

}
