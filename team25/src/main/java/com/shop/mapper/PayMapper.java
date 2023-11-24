package com.shop.mapper;

import com.shop.domain.Pay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayMapper {
    int payInsert(Pay pay);
    Pay getPay(Long pno);
    List<Pay> myPayListByUserId(String userId);
    void updateShip(int ship, Long payNo);
    int updatePayByPno(Pay pay);
}
