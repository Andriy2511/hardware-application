package com.example.hardware.DTO;

import com.example.hardware.model.Component;
import com.example.hardware.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@Slf4j
public class OrderDTO {
    private User user;
    private Component component;
    private boolean isConfirmed;
}
