package com.wenfengSAT.api.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wenfengSAT.api.model.Account;
import com.wenfengSAT.api.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	@Autowired
    private AccountRepository accountRepository;
	
	public Account save(Account account){
		
		return accountRepository.save(account);
	}
	
	public Page<Account> findByNameLike(String name,Pageable pageable){
		
		return accountRepository.findByNameLike(name, pageable);
	}
	
	public Page<Account> findByName(String name,Pageable pageable){
		
		return accountRepository.findByName(name, pageable);
	}
	
	public Page<Account> findBymoney(double money,Pageable pageable){
		
		return accountRepository.findBymoney(money, pageable);
	}
	
	public List<Account> findByName(String name){
		
		return accountRepository.findByName(name);
	}
	
	public void updateName(int id,String name){
		
		accountRepository.updateName(id, name);
	}
}
