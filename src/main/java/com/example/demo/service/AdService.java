package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;

import com.example.demo.dao.AdDao;
import com.example.demo.model.Ad;


@Setter
@Service("adeService")
public class AdService {

    @Autowired
    private AdDao adDao;

    @Transactional
    public void add(Ad ad) {

        adDao.save(ad);
    }

    @Transactional
    public List<Ad> getByID(String envId) {
        System.out.println("Transaction active in AdService.getByID: " + TransactionSynchronizationManager.isActualTransactionActive());
        Map<String, String> filters = new HashMap<>();
        filters.put("environmentId", envId);

        return adDao.findAds(filters);
    }

    @Transactional
    public List<Ad> getAll() {

        return adDao.findAll();
    }

    @Transactional
    public void delete(String id) {

        adDao.delete(Long.parseLong(id));
    }

}
