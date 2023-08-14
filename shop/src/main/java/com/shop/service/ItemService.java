package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.entitiy.Item;
import com.shop.entitiy.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품등록
        Item item = itemFormDto.createItem(); //상품 등록 폼으로부터 입력 받은 데이터를 이용하여, item객체를 생성합니다.
        itemRepository.save(item); //상품데이터를 저장.

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            //첫 번쨰 이미지일 경우 대표 상품 이미지 여부 값을 Y로 세팅
            if(i==0)
                itemImg.setRepimgYn("Y");
            //나머지 상품 이미지는 N으로 설정
            else
                itemImg.setRepimgYn("N");

            //상품의 이미지 정보를 저장
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));

        }
        return item.getId();
    }
}
