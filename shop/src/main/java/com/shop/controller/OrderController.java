package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    //스프링이세 비동기 처리할때는 @RequestBody와 @ResponseBody 어노테이션을 사용
    //RequestBody : HTTP요청의 본문 body에 담긴 내용을 자바 객체로 전달
    //ResponseBody : 자바 객체를 HTTP요청의 body로 전달
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                              BindingResult bindingResult, Principal principal){

        //주문정보를 받는 orderDto객체에 데이터 바인딩 시 에러가 있는지 검사
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            //에러정보를 ReponseEntity객체에 담아서 반환
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        //principal 객체는 Spring Security에서 현재 로그인한 사용자의 정보를 나타내는 객체.
        //주로 메소드 파라미터로 넘겨주는 방식으로 현재 로그인한 사용자의 정보에 접근할 수 있음
        //@Controller 어노테이션이 선언된 클래스에서 메소드의 파라미터로 principal 객체를 선언하면,
        //Spring Security가 현재 로그인한 사용자의 정보를 이 파라미터에 자동으로 주입
        String email = principal.getName();
        Long orderId;

        try{
            //화면에서 넘어오는 주문정보 및 회원의 이메일 정보를 이용하여 주문로직 호출
            orderId = orderService.order(orderDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        //결과값으로 생성된 주문 번호와, 요청이 성공했다는 HTTP응답 상태 코드 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }


    //구매이력을 조회하기위한 메소드
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){



            //한번에 가지고 올 주문의 갯수는 4개
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4);

        //현재 로그인한 회원은 이메일과 페이징 객체를 파라미터로 전달하여
        //화면에 전달한 주문 목록 데이터를 리턴값으로 받음
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder
            (@PathVariable("orderId") Long orderId, Principal principal){

        //자바스크립트에서 취소할 주문 번호는 조작이 가능하기 때문에
        //다른사람이 주문을 취소하지 못하도록 주문 취소 권한을 검사하는 로직
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        //주문취소 로직 호출
        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
