package untitled.infra;

import java.util.Optional;
import java.util.*;
import lombok.RequiredArgsConstructor;    
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/points")
@CrossOrigin(origins = "*")             
@RequiredArgsConstructor
public class PointController {

    private final PointRepository pointRepository;

    @PatchMapping("/{id}")
    public ResponseEntity<?> buyPoint(
        @PathVariable Long id,
        @RequestBody BuyPointDto command
        
    ) {
        Point point = pointRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Point 계정 없음"));

        point.buyPoint(command); 

        pointRepository.save(point);  // 저장 확정

        pointRepository.save(point);  // 저장 확정

        return ResponseEntity.ok(Map.of(
            "readerId", point.getReaderId(),
            "chargedPoint", command.getPoint(),
            "totalPoint", point.getPoint(),
            "status", "CHARGED"
        ));
    }

    // readerId로 포인트 조회
    @GetMapping("/reader/{readerId}")
    public ResponseEntity<?> getPointByReaderId(@PathVariable Long readerId) {
        Point point = pointRepository.findByReaderId(readerId)
            .orElseThrow(() -> new RuntimeException("포인트 계정이 없습니다."));

        return ResponseEntity.ok(Map.of(
            "readerId", point.getReaderId(),
            "totalPoint", point.getPoint() != null ? point.getPoint() : 0,
            "pointId", point.getId(),
            "status", "SUCCESS"
        ));
    }

}
//>>> Clean Arch / Inbound Adaptor

// http PATCH localhost:8080/points/1 readerId=1 point=5000 impUid="imp_12345" cost=4900
