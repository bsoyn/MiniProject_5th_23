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

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorApproved_then_CREATE_1(
        @Payload AuthorApproved authorApproved
    ) {
        try {
            if (!authorApproved.validate()) return;

            // view 객체 생성
            ReadAuthorInfo readAuthorInfo = new ReadAuthorInfo();
            // view 객체에 이벤트의 Value 를 set 함
            readAuthorInfo.setIsApproval(authorApproved.getIsApproval());
            readAuthorInfo.setName(authorApproved.getName());
            readAuthorInfo.setBio(authorApproved.getBio());
            readAuthorInfo.setMajorWork(authorApproved.getMajorWork());
            readAuthorInfo.setPortfolio(
                String.valueOf(authorApproved.getPortfolio())
            );
            readAuthorInfo.setEmail(authorApproved.getEmail());
            readAuthorInfo.setAuthorId(authorApproved.getId());
            // view 레파지 토리에 save
            readAuthorInfoRepository.save(readAuthorInfo);
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
