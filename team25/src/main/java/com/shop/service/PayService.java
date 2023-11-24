package com.shop.service;
import com.shop.domain.Pay;

import java.util.List;

public interface PayService {
    public int payInsert(Pay pay);
    public Pay getPay(Long pno);
    public List<Pay> myPayListByUserId(String userId);
    public void updateShip(int ship, Long payNo);
    public int updatePayByPno(Pay pay);
}

