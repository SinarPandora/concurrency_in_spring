package com.kingland.lunch.learn.demo.model;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class Person {
    @Id
    private Long id;
    private String name;
    private String email;
    private int age;
    private int gender;
    private String address;
    private boolean isActive;
    private Timestamp lastUpdateDate;
}
