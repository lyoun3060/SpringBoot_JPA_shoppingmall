package com.shop.service;

import com.shop.entitiy.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    //application.properties에 등록한 itemImgLocation값 불러와서 itemImgLocation변수에 넣어줌
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            //사용자가 상품이미지를 등록한 경우, uploadFile메소드를 호출해 정보들을 가져온 후 imgName변수에 저장
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //저장한 상품의 이미지를 볼러올 경로를 설정
            imgUrl = "/images/item/"+imgName;
        }

        //상품이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}
