package com.shop.service;

import com.shop.domain.Pay;
import com.shop.mapper.PayMapper;
import com.shop.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService{
    @Autowired
    private PayMapper payMapper;
    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public int payInsert(Pay pay) {
        int check = payMapper.payInsert(pay);
        if (check >=1) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("num", 2); // 또는 2
            paramMap.put("pno", pay.getPno()); // pno 값 설정
            productService.updateStatus(paramMap); // 상품 상태 변경하고
            return 1;

        } else {
            return 0;
        }
    }

    @Override
    public Pay getPay(Long pno) {
        return payMapper.getPay(pno);
    }

    @Override
    public List<Pay> myPayListByUserId(String userId) {
        return payMapper.myPayListByUserId(userId);
    }

    @Override
    public void updateShip(int ship,Long payNo) {
        payMapper.updateShip(ship, payNo);
    }

    @Override
    public int updatePayByPno(Pay pay) {
        int check = payMapper.updatePayByPno(pay);
        if (check >=1) {
            productService.updateProductStatus("거래중",pay.getPno());
            return 1;

        } else {
            return 0;
        }
    }
}
