package untitled.infra;

import java.util.Optional;
import java.util.*;
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

        point.buyPoint(command);  // <- 도메인 로직 수행

        pointRepository.save(point);  // 저장 확정

        return ResponseEntity.ok(Map.of(
            "readerId", point.getReaderId(),
            "requestedPoint", command.getPoint(),
            "status", "REQUESTED"
        ));
    }

}
//>>> Clean Arch / Inbound Adaptor

// http PATCH localhost:8080/points/1 readerId=1 point=5000 impUid="imp_12345" cost=4900
