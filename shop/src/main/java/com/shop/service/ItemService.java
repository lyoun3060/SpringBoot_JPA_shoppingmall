package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entitiy.Item;
import com.shop.entitiy.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정
    // 이렇게 할 경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능을 향상 시킬 수 있습니다.
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        //행당 상품의 이미지를 조회. 등록순으로 가지고 오기위해 상품이미지 아이디 오름차순으로 가지고 옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        //조회한 ItemImg 엔티티를 ItemImgDto객체로 만들어서 리스트에 추가
        for(ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        //상품의 아이디를 통해 상품 엔티티를 조회, 존재하지 않을때는 에러를 발생시킴
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    //상품을 업데이트할 변경 감지 기능
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품수정
        Item item = itemRepository.findById(itemFormDto.getId()) //상품등록 화면으로부터 전달 받은 상품아이디를 이용하여 상품 엔티티를 조회
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto); //상품등록화면으로부터 전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트함.

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회


        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            //상품 이미지를 업데이트하기 위해서 updateItemImg() 메소드에 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 전달
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }
}
