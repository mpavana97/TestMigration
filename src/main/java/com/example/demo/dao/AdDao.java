package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.demo.model.Ad;
import com.example.demo.util.GenericDAO;
import com.example.demo.vo.SimpleCriteria;


@Repository("adDao")
@SuppressWarnings("unchecked")
public class AdDao extends GenericDAO<Ad, Long> {

    public Long storeAds(Ad ad) {
        try {
            saveOrUpdate(ad);
            return ad.getAdvertisementId();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Ad> findAds(Map<String, String> filters) {
        System.out.println("Transaction active in AdDao.findAds: " + TransactionSynchronizationManager.isActualTransactionActive());
        try {
            SimpleCriteria<Ad> sc = criteria(Ad.class).ne("state", "Deleted");

            if (!filters.isEmpty()) {

                if (filters.get("environmentId") != null && !filters.get("environmentId").isEmpty()) {
                    sc.eq("environmentId", Long.parseLong(filters.get("environmentId")));
                }
            }

            return sc.list();

        } catch (Exception e) {
            throw e;
        }
    }

}
