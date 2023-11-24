package com.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private int roleId; // 직권 등급 아이디 -> 전 id
    private String roleName; // 직권 이름 -> 전 role
}
