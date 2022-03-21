package com.young.gen.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class City implements Serializable {
    private static final long serialVersionUID = 9166567633332891680L;

    private Long id;

    private String name;

    private String state;

    private String country;

}
