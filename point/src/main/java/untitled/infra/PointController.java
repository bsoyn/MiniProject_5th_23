package untitled.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/points")
@Transactional
public class PointController {

    @Autowired
    PointRepository pointRepository;

    @PatchMapping("/{id}")
    public ResponseEntity<?> buyPoint(
        @PathVariable Long id,
        @RequestBody BuyDto command
        
    ) {
        Point point = pointRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Point 계정 없음"));

        point.buyPoint(command);  // <- 도메인 로직 수행

        return ResponseEntity.ok();
    }

}
//>>> Clean Arch / Inbound Adaptor
