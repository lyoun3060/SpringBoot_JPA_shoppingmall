package com.shop.service;

import com.shop.entitiy.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

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
        //imgName : 로컬에 저장된 상품이미지 파일의 이름
        //oriImgName: 업로드했던 상품이미지 파일 원본이름
        //imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        //상품 이미지를 수정한 경우 상품 이미지를 업데이트
        if(!itemImgFile.isEmpty()){
            //상품이미지 아이들 이용하여, 기존에 저장했던 상품이미지 엔티티를 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일 삭제(기전에 등록된 상품이미지 파일이 있을경우 해당 파일을 삭제)
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            //업데이트한 상품 이미지 파일을 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            //변경된 상품 이미지 정보를 세팅
            //상품 등록때처럼 itemImgRepository.save()를 호출하는게 아니라는것
            //여기서 savedItemImg 엔티티는 영속 상태이기 때문에 데이터를 변경하는 것 만으로 변경 감지 기능이 동작함
            //따리서, 트랜잭션이 끝날때 update쿼리가 실행됨
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
