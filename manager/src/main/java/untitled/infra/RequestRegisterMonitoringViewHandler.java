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
public class RequestRegisterMonitoringViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private RequestRegisterMonitoringRepository requestRegisterMonitoringRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegisterRequested_then_CREATE_1(
        @Payload RegisterRequested registerRequested
    ) {
        try {
            if (!registerRequested.validate()) return;

            // view 객체 생성
            RequestRegisterMonitoring requestRegisterMonitoring = new RequestRegisterMonitoring();
            // view 객체에 이벤트의 Value 를 set 함
            requestRegisterMonitoring.setAuthorId(registerRequested.getId());
            requestRegisterMonitoring.setIsApproval(
                registerRequested.getIsApproval()
            );
            requestRegisterMonitoring.setName(registerRequested.getName());
            requestRegisterMonitoring.setBio(registerRequested.getBio());
            requestRegisterMonitoring.setMajorWork(
                registerRequested.getMajorWork()
            );
            requestRegisterMonitoring.setPortfolio(
                String.valueOf(registerRequested.getPortfolio())
            );
            requestRegisterMonitoring.setEmail(registerRequested.getEmail());
            // view 레파지 토리에 save
            requestRegisterMonitoringRepository.save(requestRegisterMonitoring);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
