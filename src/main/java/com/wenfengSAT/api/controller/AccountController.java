package com.wenfengSAT.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.wenfengSAT.api.model.Account;
import com.wenfengSAT.api.repository.AccountRepository;
import com.wenfengSAT.api.service.AccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Account> getAccounts() {
    	System.out.println("list。。。。。。。。。。。。");
        return accountRepository.findAll();
    }
    
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
    public Page<Account> getAccount1() {
        return accountService.findByName("aaa", new PageRequest(0, 10));
    }
    
    @RequestMapping(value = "/list2", method = RequestMethod.GET)
    public Page<Account> getAccount2() {
        return accountService.findByNameLike("bbb", new PageRequest(0, 10));
    }
    
    @RequestMapping(value = "/list3", method = RequestMethod.GET)
    public Page<Account> getAccount3() {
        return accountService.findBymoney(1000, new PageRequest(0, 10));
    }
    
    @RequestMapping(value = "/list4", method = RequestMethod.GET)
    public List<Account> getAccount4() {
        return accountService.findByName("aaa");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable("id") int id) {
    	Optional<Account> option = accountRepository.findById(id);
    	if(option!=null && option.isPresent()){
    		return option.get();
    	}else{
    		return null;
    	}
    	//return accountRepository.findAccountById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateAccount(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name,
                                @RequestParam(value = "money", required = true) double money) {
        Account account = new Account();
        account.setMoney(money);
        account.setName(name);
        account.setId(id);
        Account account1 = accountRepository.saveAndFlush(account);

        return account1.toString();

    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String postAccount(@RequestParam(value = "name") String name,
                              @RequestParam(value = "money") double money) {
        Account account = new Account();
        account.setMoney(money);
        account.setName(name);
        Account account1 = accountRepository.save(account);
        return account1.toString();

    }


}
