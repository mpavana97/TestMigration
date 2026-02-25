package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Setter;

import com.example.demo.model.Ad;
import com.example.demo.service.AdService;


@Setter
@RestController
@RequestMapping("/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @PostMapping("/save")
    public String save(
        @RequestBody
        Ad ad) {
        adService.add(ad);
        return "Ad saved!";
    }

    @GetMapping(value = "/listById", produces = "application/json")
    public List<Ad> listById(
        @RequestParam("id")
        String envId) {

        return adService.getByID(envId);
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<Ad> list() {

        return adService.getAll();
    }

    @DeleteMapping(value = "/delete", produces = "application/json")
    public String delete(@RequestParam("id")
    String adId) {

        adService.delete(adId);

        return "deleted!";
    }

}
