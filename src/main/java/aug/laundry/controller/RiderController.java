package aug.laundry.controller;

import aug.laundry.domain.DeliveryImage;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Rider;
import aug.laundry.dto.DongInfoDto;
import aug.laundry.dto.OrdersEnum;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.fileUpload.FileUploadType;
import aug.laundry.enums.orderStatus.OrderStatus;
import aug.laundry.enums.orderStatus.routineOrder;
import aug.laundry.service.FileUploadService_ksh;
import aug.laundry.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.util.*;

import static aug.laundry.enums.orderStatus.routineOrder.regionList;

@Controller
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/ride/*")
public class RiderController {

    private final RiderService riderService;
    private final FileUploadService_ksh fileUpload;

    @GetMapping("/ride/wait")
    public String waitList(Model model) {
        List<Map<String, Integer>> cnt = riderService.orderListCnt();
//        List<Orders> orderList = riderService.OrderList("대기중");
        List<OrdersEnum> orderList = riderService.OrderListEnum("대기중");
        Rider riderInfo = riderService.riderInfo("스크류바");


//        List<Orders> orderList = new ArrayList<>();

//        for(Orders area : list){
//            if(area.getOrdersAddress().contains(riderInfo.getWorkingArea())){
//                orderList.add(area);
//            }
//        }
//        System.out.println(orderList);

        model.addAttribute("orderList", orderList);
        model.addAttribute("cnt", cnt);
        model.addAttribute("riderInfo",riderInfo);

        return "project_rider_list_on_call";
    }

    @GetMapping("/ride/accept")
    public String acceptList(Model model) {
//        List<Orders> orderList = riderService.OrderList("진행중");
        List<OrdersEnum> orderList = riderService.OrderList("진행중");
        List<Map<String, Integer>> cnt = riderService.orderListCnt();

        model.addAttribute("orderList", orderList);
        model.addAttribute("cnt", cnt);

        return "project_rider_using_list";
    }

    @GetMapping("/ride/finish")
    public String finishList(Model model) {
//        List<Orders> orderList = riderService.OrderList("완료");
        List<OrdersEnum> orderList = riderService.OrderListEnum("완료");
        List<Map<String, Integer>> cnt = riderService.orderListCnt();

        model.addAttribute("orderList", orderList);
        model.addAttribute("cnt", cnt);

        return "project_rider_complete";
    }


    @MessageMapping("/order-complete-message/{storeId}")
    @SendTo("/topic/order-complete/{storeId}")
    public Map<String, Object> message(@DestinationVariable long storeId, String message) {
        System.out.println("가게번호 : " + storeId);
        System.out.println("메세지 도착 :" + message);

        Map<String, Object> map = new HashMap<>();

        Orders orders = new Orders();
        orders.setOrdersId(37L);
        orders.setOrdersDate("2023/08/31");
        orders.setOrdersStatus(2);
        orders.setOrdersAddress("서울시 서대문구 홍은동 454");
//        orders.setOrdersAddress("서울시 강북구 번동 657");
        orders.setOrdersAddressDetails("108동 505호");

        map.put("a",orders);
        map.put("storeId", storeId);
        return map;
    }

    @GetMapping("/oo/kk/{storeId}")
    public String http33(@PathVariable Long storeId, HttpSession session, Model model) {
        model.addAttribute("storeId", storeId);
        return "redirect:/oo/63";
    }

    @GetMapping("/oo/63")
    public String h(){
        return "oo";
    }

    @GetMapping("/ride/orders/{ordersId}")
    public String orderInfo(@PathVariable("ordersId") Long ordersId, Model model) {
        Orders info = riderService.orderInfo(ordersId);
        DeliveryImage img = riderService.finishImg(ordersId);

        System.out.println(info);
        System.out.println(img);
        // 이미지가 없을때 img에 담기는거 처리 해줘야함

        model.addAttribute("img", img);
        model.addAttribute("info", info);
        return "project_rider_read_more";
    }

    @PostMapping("/ride/assign/{ordersId}/{riderId}")
    public String orderCheck(@PathVariable("ordersId") Long ordersId, @PathVariable("riderId") Long riderId, Model model){
        System.out.println(riderId);
        // 정기배송
        if(riderId != 0){
            Orders orders = new Orders();
            orders.setOrdersId(ordersId);

            int res2 = riderService.updateOrderStatus(orders);
            System.out.println(res2);
            return "redirect:/ride/routine";
        }else{
            Orders orders = new Orders();
            orders.setOrdersId(ordersId);

            int res = riderService.updateOrderRider(orders);
            int res2 = riderService.updateOrderStatus(orders);
            System.out.println(res);
            System.out.println(res2);
            return "redirect:/ride/accept";
        }
//        Orders orders = new Orders();
//        orders.setOrdersId(ordersId);
//
////        int res = riderService.updateOrderRider(orders);
//        int res2 = riderService.updateOrderStatus(orders);
////        System.out.println(res);
//        System.out.println(res2);

//        return "redirect:/ride/accept";
    }

    @PostMapping("/ride/pickUp/{ordersId}/{riderId}")
    public String deliveryFinish(@PathVariable("ordersId") Long ordersId, @PathVariable("riderId") Long riderId, @RequestParam("files") List<MultipartFile> files ){
        System.out.println("riderId : " + riderId);
        System.out.println(ordersId);
        System.out.println(files);

        // 정기배송에서 넘어올때
        if(riderId != 0){
            int fileRes = fileUpload.saveFile(files, ordersId, FileUploadType.DELIVERY);
            System.out.println("fileRes : " + fileRes);
            Orders orders = new Orders();
            orders.setOrdersId(ordersId);
            int res2 = riderService.updateOrderStatus(orders);
            return "redirect:/ride/routine/"+ordersId;
        }else{
            int fileRes = fileUpload.saveFile(files, ordersId, FileUploadType.DELIVERY);
            System.out.println("fileRes : " + fileRes);
            Orders orders = new Orders();
            orders.setOrdersId(ordersId);
            int res = riderService.updateOrderRider(orders);
            int res2 = riderService.updateOrderStatus(orders);
            return "redirect:/ride/finish";
        }
    }

    @GetMapping("/ride/routine")
    public String routineList(Model model){
        Rider rider = riderService.routineRider("홀란드");

        routineOrder order = routineOrder.valueOf(rider.getRiderPossibleZipcode());
        List<String> dongNames = order.getDongName();
        System.out.println(dongNames);

        List<OrdersEnum> list = riderService.routineOrderList(dongNames.get(0), "진행중");
        System.out.println(list);

        Map<String,Integer> total = riderService.routineTotalCnt(rider.getRiderPossibleZipcode());
        System.out.println(total);

        List<Map<String, Integer>> cnt = riderService.routineOrderCnt();
        System.out.println(cnt);

//        List<Map<String, Integer>> dongCnt = riderService.dongCnt(rider.getRiderPossibleZipcode());
        List<Integer> dongCntWait = riderService.dongCnt(rider.getRiderPossibleZipcode(), "진행중");
        List<Integer> dongCntComplete = riderService.dongCnt(rider.getRiderPossibleZipcode(), "완료");

        List<Map<String, DongInfoDto>> regionCntWait = new ArrayList<>();
        List<Map<String, DongInfoDto>> regionCntComplete = new ArrayList<>();

        for(int i=0; i<dongCntWait.size(); i++){
            DongInfoDto dongInfo = new DongInfoDto();
            dongInfo.setDongNames(dongNames.get(i));
            dongInfo.setDongCnt(dongCntWait.get(i));

            Map<String, DongInfoDto> resultMap = new HashMap<>();
            resultMap.put("dongInfo", dongInfo);
            regionCntWait.add(resultMap);
        }
        for(int i=0; i<dongCntComplete.size(); i++){
            DongInfoDto dongInfo = new DongInfoDto();
            dongInfo.setDongNames(dongNames.get(i));
            dongInfo.setDongCnt(dongCntComplete.get(i));

            Map<String, DongInfoDto> resultMap = new HashMap<>();
            resultMap.put("dongInfo", dongInfo);
            regionCntComplete.add(resultMap);
        }


//        model.addAttribute("dongNames",dongNames);
        model.addAttribute("rider", rider);
        model.addAttribute("list", list);
        model.addAttribute("total", total);
        model.addAttribute("cnt", cnt);
        model.addAttribute("regionCntWait",regionCntWait);
        model.addAttribute("regionCntComplete",regionCntComplete);
        return "project_rider_routine";
    }


    @GetMapping("/ride/routine/{ordersId}")
    public String routineRead(@PathVariable("ordersId") Long ordersId, Model model){
        Orders info = riderService.orderInfo(ordersId);
        DeliveryImage img = riderService.finishImg(ordersId);

        System.out.println(info);
        System.out.println(img);
        // 이미지가 없을때 img에 담기는거 처리 해줘야함

        model.addAttribute("img", img);
        model.addAttribute("info", info);

        return "project_rider_routine_read_more";
    }
}
