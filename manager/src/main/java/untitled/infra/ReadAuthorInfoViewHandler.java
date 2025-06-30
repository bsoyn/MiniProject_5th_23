package untitled.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import untitled.config.kafka.KafkaProcessor;
import untitled.domain.*;

@Service
public class ReadAuthorInfoViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ReadAuthorInfoRepository readAuthorInfoRepository;
    
    @Autowired
    private ManageAuthorRepository manageAuthorRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorApproved_then_CREATE_1(
        @Payload AuthorApproved authorApproved
    ) {
        try {
            if (!authorApproved.validate()) return;

            // ManageAuthor에서 상세 정보 조회
            Optional<ManageAuthor> manageAuthorOpt = manageAuthorRepository.findById(authorApproved.getId());
            if (manageAuthorOpt.isPresent()) {
                ManageAuthor manageAuthor = manageAuthorOpt.get();
                
                // view 객체 생성
                ReadAuthorInfo readAuthorInfo = new ReadAuthorInfo();
                // view 객체에 ManageAuthor의 데이터를 set 함
                readAuthorInfo.setIsApproval(manageAuthor.getIsApproval());
                readAuthorInfo.setName(manageAuthor.getName());
                readAuthorInfo.setBio(manageAuthor.getBio());
                readAuthorInfo.setMajorWork(manageAuthor.getMajorWork());
                readAuthorInfo.setPortfolio(
                    String.valueOf(manageAuthor.getPortfolio())
                );
                readAuthorInfo.setEmail(manageAuthor.getEmail());
                readAuthorInfo.setAuthorId(manageAuthor.getAuthorId());
                // view 레파지 토리에 save
                readAuthorInfoRepository.save(readAuthorInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorInfoUpdated_then_UPDATE_1(
        @Payload AuthorInfoUpdated authorInfoUpdated
    ) {
        try {
            if (!authorInfoUpdated.validate()) return;
            // view 객체 조회

            List<ReadAuthorInfo> readAuthorInfoList = readAuthorInfoRepository.findByAuthorId(
                authorInfoUpdated.getId()
            );
            for (ReadAuthorInfo readAuthorInfo : readAuthorInfoList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                readAuthorInfo.setAuthorId(authorInfoUpdated.getId());
                readAuthorInfo.setIsApproval(authorInfoUpdated.getIsApproval());
                readAuthorInfo.setName(authorInfoUpdated.getName());
                readAuthorInfo.setBio(authorInfoUpdated.getBio());
                readAuthorInfo.setMajorWork(authorInfoUpdated.getMajorWork());
                readAuthorInfo.setPortfolio(
                    String.valueOf(authorInfoUpdated.getPortfolio())
                );
                readAuthorInfo.setEmail(authorInfoUpdated.getEmail());
                // view 레파지 토리에 save
                readAuthorInfoRepository.save(readAuthorInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
