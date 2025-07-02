package untitled.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import untitled.domain.aggregate.*;
import untitled.dto.*; 
import untitled.repository.*; 
import untitled.service.ManuscriptService; 

import java.util.List;


@RestController
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @Autowired
    ManuscriptService manuscriptService; 

    @RequestMapping(
        value = "/manuscripts/request-publication",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript requestPublication(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestPublicationDTO dto
    ) throws Exception {
        System.out.println(
            "##### /manuscript/request-publication  called #####"
        );

        return manuscriptService.saveOrUpdateManuscript(dto);
    }

    @PostMapping("/manuscripts/{id}/complete-writing")
    public Manuscript completeWriting(@PathVariable Long id) {
        return manuscriptService.completeWriting(id); 
    }


    @PostMapping("/manuscripts/temp-save")
    public Manuscript tempSaveManuscript(@RequestBody TempSaveManuscriptDTO dto) {
        return manuscriptService.tempSaveManuscript(dto); 
    }

    // 임시 저장 원고 조회 
    @GetMapping("/manuscripts/temp")
    public List<Manuscript> getTempManuscripts(@RequestParam Long authorId) {
        return manuscriptRepository.findByAuthorIdAndStatus(authorId, ManuscriptStatus.TEMP);
    }

    @GetMapping("/manuscripts/temp/{id}")
    public Manuscript getTempManuscriptById(@PathVariable Long id) {
        return manuscriptRepository.findById(id)
            .filter(m -> m.getStatus() == ManuscriptStatus.TEMP)
            .orElseThrow(() -> new RuntimeException("임시 저장된 원고를 찾을 수 없습니다."));
    }

}
