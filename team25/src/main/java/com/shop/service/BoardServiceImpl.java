package com.shop.service;

import com.shop.domain.Board;
import com.shop.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardMapper boardMapper;

    @Override
    public int insertBoard(Board board) {
        int check = boardMapper.insertBoard(board);
        if (check >=1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Board> getBoardList() {
        return boardMapper.getBoardList();
    }

    @Override
    public Board getBoard(int bno) {
        boardMapper.updateVisited(bno);
        return boardMapper.getBoard(bno);
    }

    @Override
    public int updateBoard(Board board) {
        int check = boardMapper.updateBoard(board);
        if (check >=1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int deleteBoard(int bno) {
        int check = boardMapper.deleteBoard(bno);
        if (check >=1) {
            return 1;
        } else {
            return 0;
        }
    }
}
