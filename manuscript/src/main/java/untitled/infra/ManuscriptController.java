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

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.List;


@RestController
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @RequestMapping(
        value = "/manuscripts/request-publication",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript requestPublication(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestPublicationCommand requestPublicationCommand
    ) throws Exception {
        System.out.println(
            "##### /manuscript/request-publication  called #####"
        );

        if (requestPublicationCommand.getId() != null) {
            return manuscriptRepository.findById(requestPublicationCommand.getId())
                .map(manuscript -> {
                    manuscript.requestPublication(requestPublicationCommand);
                    return manuscript;
                })
                .orElseGet(() -> {
                    // ID는 있으나 없으면 새로 생성
                    Manuscript newManuscript = new Manuscript();
                    newManuscript.requestPublication(requestPublicationCommand);
                    return newManuscript;
                });
        }

        Manuscript manuscript = new Manuscript();
        manuscript.requestPublication(requestPublicationCommand);
        return manuscript;
    }

    @PostMapping("/manuscripts/{id}/complete-writing")
    public Manuscript completeWriting(@PathVariable Long id) {
        return manuscriptRepository.findById(id)
            .map(manuscript -> {
                WritingCompleted writingCompleted = new WritingCompleted();
                writingCompleted.setManuscriptId(manuscript.getId());
                writingCompleted.setAuthorId(manuscript.getAuthorId());
                writingCompleted.setTitle(manuscript.getTitle());
                writingCompleted.setContents(manuscript.getContents());
                writingCompleted.setImageUrl(manuscript.getImageUrl());
                writingCompleted.setSummary(manuscript.getSummary());
                writingCompleted.setCategory(manuscript.getCategory());
                writingCompleted.setPrice(manuscript.getPrice());

                writingCompleted.publishAfterCommit();

                return manuscript;
            })
            .orElseThrow(() -> new RuntimeException("해당 ID의 원고가 존재하지 않습니다."));
    }


    @PostMapping("/manuscripts/temp-save")
    public Manuscript tempSaveManuscript(@RequestBody TempSaveManuscriptCommand cmd) {

        // 1. 이미 저장된 원고 ID가 있다면 찾아서 갱신
        if (cmd.getManuscriptId() != null) {
            return manuscriptRepository.findById(cmd.getManuscriptId())
                .map(manuscript -> {
                    manuscript.tempSave(cmd);
                    return manuscript;
                })
                .orElseGet(() -> {
                    // ID는 있으나 찾을 수 없다면 새로 저장
                    Manuscript newManuscript = new Manuscript();
                    newManuscript.tempSave(cmd);
                    return manuscriptRepository.save(newManuscript);
                });
        }

        // 2. ID가 없으면 새로 저장
        Manuscript newManuscript = new Manuscript();
        newManuscript.tempSave(cmd);
        return manuscriptRepository.save(newManuscript);
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
