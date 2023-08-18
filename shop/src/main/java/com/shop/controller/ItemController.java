package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entitiy.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try{
            //조회한 상품 데이터를 모델에 담아서 뷰로 전달
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        }
        //상품엔티티가 존재하지 않을 경우, 에러메세지를 담아 상품등록페이지로 이동
        catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}") //상품을 수정하는 url을 추가, 상품 등록 때와 거의 비슷
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try { //상품수정로직 호출
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }



    @GetMapping(value = {"/admin/items", "admin/items/{page}"}) //value에 상품관리 화면 진입 시 URL에 페이지 번호가 없는 경우와, 페이지 번호가 있는경우 둘다 매핑함
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        //페이징을 위해 pageRequest.of 메소드를 통하여 Pageabel객체를 생성
        //첫번째 파라미터로 조회할 페이지 번호 / 두번째 파라미터로 한번에 가지고 올 데이터 수를 넣음
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        //조회 조건과 페이징 정보를 파라미터로 넘겨 Page<Item>객체를 반환받음
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("items", items);
        //페이지 전환 시 기존 검색 조건을 유지한채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("itemSearchDto", itemSearchDto);
        //메뉴하단에 보여주는페이지 번호의 최대 갯수
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }
}
