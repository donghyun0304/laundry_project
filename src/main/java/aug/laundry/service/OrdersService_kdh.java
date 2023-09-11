package aug.laundry.service;

import aug.laundry.dao.orders.OrdersDao;
import aug.laundry.dao.point.PointDao;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.DrycleaningResponseDto;
import aug.laundry.dto.OrdersResponseDto;
import aug.laundry.dto.RepairResponseDto;
import aug.laundry.enums.category.Category;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService_kdh {

    private final OrdersDao ordersDao;
    private final PointDao pointDao;


    public OrdersResponseDto findByOrdersId(Long ordersId){
        OrdersResponseDto ordersResponseDto = ordersDao.findByOrdersId(ordersId);

        Double commonLaundryWeight = ordersResponseDto.getCommonLaundryWeight();

        //생활빨래 계산로직
        if(commonLaundryWeight != null){
            Category commonLaundry = Category.BASIC;
            Category commonAdditional = Category.ADDITIONAL;

            calculateCommonLaundry(ordersResponseDto, commonLaundryWeight, commonLaundry, commonAdditional);
        }

        return ordersResponseDto;
    }

    private static void calculateCommonLaundry(OrdersResponseDto ordersResponseDto, Double commonLaundryWeight, Category commonLaundry, Category commonAdditional) {
        if (commonLaundryWeight > 0 && commonLaundryWeight <= 10) {
            //생활빨래 0~10리터
            Long price = 1 * commonLaundry.getPrice();
            ordersResponseDto.setCommonLaundryPrice(price);

        } else if(commonLaundryWeight > 10 && commonLaundryWeight <=20){
            //생활빨래 10~20리터
            Long price = 2 * commonLaundry.getPrice();
            ordersResponseDto.setCommonLaundryPrice(price);

        } else if(commonLaundryWeight > 20){
            //생활빨래 20리터까지 계산
            Long price1 = 2 * commonLaundry.getPrice();
            //생활빨래 20리터 이후 가격 계산
            Long price2 = (long) Math.ceil((commonLaundryWeight - 20) / 10) * commonAdditional.getPrice();
            ordersResponseDto.setCommonLaundryPrice(price1 + price2);
        }
    }

    public Map<String, Object> findDryCleaningByOrdersId(Long ordersId){

        List<Drycleaning> dryList = ordersDao.findDryCleaningByOrdersId(ordersId);

        Map<String, Object> dryMap = new HashMap<>();

        if(dryList.isEmpty()) {
            dryMap.put("dryList", Collections.EMPTY_LIST);
            dryMap.put("totalDryPrice", 0L);
        } else{
            List<DrycleaningResponseDto> dryDtoList = MapToDryResponseDto(dryList);
            long totalDryPrice = dryDtoList.stream()
                    .filter(dry -> dry.getDrycleaningPossibility() == 'Y')
                    .mapToLong(dry -> dry.getDrycleaningPrice())
                    .sum();

            dryMap.put("dryList", dryDtoList);
            dryMap.put("totalDryPrice", totalDryPrice);
        }

        return dryMap;
    }

    private static List<DrycleaningResponseDto> MapToDryResponseDto(List<Drycleaning> dryList) {
        return dryList.stream()
                .map(dry -> new DrycleaningResponseDto(
                        Category.valueOf(dry.getDrycleaningCategory()),
                        dry.getDrycleaningPossibility(),
                        dry.getDrycleaningNotReason(),
                        Category.valueOf(dry.getDrycleaningCategory()).getPrice()
                )).collect(Collectors.toList());
    }

    public Map<String, Object> findRepairByOrdersId(Long ordersId){

        List<Repair> repairList = ordersDao.findRepairByOrdersId(ordersId);

        Map<String, Object> repairMap = new HashMap<>();


        if(repairList.isEmpty()){
            repairMap.put("repairList", Collections.EMPTY_LIST);
            repairMap.put("totalRepairPrice", 0L);
        } else {
            List<RepairResponseDto> repairDtoList = mapToRepairResponseDto(repairList);
            long totalRepairPrice = repairDtoList.stream()
                    .filter(repair -> repair.getRepairPossibility() == 'Y')
                    .mapToLong(repair -> repair.getRepairPrice())
                    .sum();

            repairMap.put("repairList", repairDtoList);
            repairMap.put("totalRepairPrice", totalRepairPrice);
        }

        return repairMap;
    }

    @NotNull
    private static List<RepairResponseDto> mapToRepairResponseDto(List<Repair> repairList) {
        return repairList.stream()
                .map(repair -> new RepairResponseDto(
                        Category.valueOf(repair.getRepairCategory()),
                        repair.getRepairPossibility(),
                        repair.getRepairNotReason(),
                        Category.valueOf(repair.getRepairCategory()).getPrice()
                )).collect(Collectors.toList());
    }

    public Integer findPointByMemberId(Long memberId){
        return pointDao.findByMemberId(memberId);
    }

    public boolean isQuickLaundry(Long ordersId){
        return ordersDao.isQuickLaundry(ordersId);
    }


    @Transactional
    public void updateExpectedPriceByOrdersId(Long ordersId, Long expectedPrice) {
        int result = ordersDao.updateExpectedPriceByOrdersId(ordersId, expectedPrice);
        if(result==0) {
            throw new IllegalArgumentException("잘못된 회원아이디 입력");
        }
    }


}
