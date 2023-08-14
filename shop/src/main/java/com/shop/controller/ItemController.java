package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model)
    {
        //ItemFormDto를 model객체에 담아서 뷰로 전달
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        //상품 등록 시 필수 값이 없다면 다시 상품 등록페이지로
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        //상품 등록 시 첫번째 상품에 대한 이미지가 없다면(상품이 없거나 or 사진이 없으면) 에러메시지와 함께 상품 등록 페이로 전환
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        //상품 저장 로직 호출
        //매개 변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemImgFileList를 넘겨줌
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        //상품이 정상적으로 등록되엇으면 메인페이지로
        return "redirect:/";
    }
}
