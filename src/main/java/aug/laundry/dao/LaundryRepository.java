package aug.laundry.dao;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.dto.Address;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.CategoryOption;
import aug.laundry.service.LaundryServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LaundryRepository {

    private final LaundryMapper laundryMapper;

    public OrderInfo firstInfo(Long memberId) {
//        Map<String, Integer> map = new ConcurrentHashMap<>();
//        if (Objects.nonNull(laundryMapper.isQuick(memberId))) map.put(CategoryOption.QUICK_LAUNDRY.getTitle(), 1);
//        if (Objects.nonNull(laundryMapper.isCommon(memberId))) map.put(CategoryOption.COMMON_LAUNDRY.getTitle(), 1);
//        if (Objects.nonNull(laundryMapper.isDry(memberId))) map.put(CategoryOption.DRYCLEANING.getTitle(), 1);
//        if (Objects.nonNull(laundryMapper.isRepair(memberId))) map.put(CategoryOption.REPAIR.getTitle(), 1);
        return laundryMapper.firstInfo(memberId);
    }

    public List<MyCoupon> getCoupon(Long memberId) {
        return laundryMapper.getCoupon(memberId);
    }


    public Address getAddress(Long memberId) {
        return laundryMapper.getAddress(memberId);
    }

    public List<Category> getDry(Long memberId) {
        return laundryMapper.getDry(memberId).stream().map(x -> Category.valueOf(x)).collect(Collectors.toList());
    }

    public List<Category> getRepair(Long memberId) {
        return laundryMapper.getRepair(memberId).stream().map(x -> Category.valueOf(x)).collect(Collectors.toList());
    }
}
