package com.young.gen.controller;

import com.young.gen.domain.City;
import com.young.gen.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityMapper mapper;

    @GetMapping
    public City get(@RequestParam String state) {
        return mapper.findByState(state);
    }
}
