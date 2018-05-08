package com.wenfengSAT.api.service;

import java.util.List;

public interface BaseService<T> {
    void save(T t);//持久化
    void save(List<T> t);//批量持久化
    void deleteById(Integer id);//通过主鍵刪除
    void deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”
    void update(T t);//更新
    T findById(Integer id);//通过ID查找
    List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”
    List<T> findAll();//获取所有
}
