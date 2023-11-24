package com.shop.controller;

import com.shop.domain.Board;
import com.shop.domain.User;
import com.shop.service.BoardService;
import com.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board/")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @GetMapping("/boardList")
    public String boardList(Model model){
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList",boardList);
        return "board/boardList";
    }
    @GetMapping("/getBoard/{bno}")
    public String getBoard(@PathVariable("bno") int bno, Model model){
        Board board = boardService.getBoard(bno);
        model.addAttribute("board", board);
        return "board/getBoard";
    }

    @GetMapping("/insertBoard")
    public String insertBoard(Model model) {
        return "board/insertBoard";
    }

    @PostMapping("/insertBoard")
    public String insertBoardPro(Board board) {
        int check = boardService.insertBoard(board);
        if (check == 1) {
            log.info("글 작성 성공");
            return "redirect:/board/boardList";
        }
        else {
            log.info("글 작성 실패");
            return "redirect:/board/boardList";
        }
    }

    @GetMapping("/updateBoard/{bno}")
    public String updateBoard(@PathVariable("bno") int bno, Model model) {
        Board board = boardService.getBoard(bno);
        model.addAttribute("board",board);
        return "board/updateBoard";
    }

    @PostMapping("/updateBoard")
    public String updateBoardPro(Board board) {
        System.out.println(board);
        int check = boardService.updateBoard(board);
        if (check == 1) {
            log.info("글 수정 성공");
            return "redirect:/board/boardList";
        }
        else {
            log.info("글 수정 실패");
            return "redirect:/board/boardList";
        }
    }

    @GetMapping("/deleteBoard/{bno}")
    public String deleteBoard(@PathVariable("bno") int bno) {
        int check = boardService.deleteBoard(bno);
        if (check == 1) {
            log.info("글 삭제 성공");
            return "redirect:/board/boardList";
        }
        else {
            log.info("글 삭제 실패");
            return "redirect:/board/boardList";
        }
    }
}
