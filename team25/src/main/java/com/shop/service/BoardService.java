package com.shop.service;

import com.shop.domain.Board;

import java.util.List;

public interface BoardService {
    public int insertBoard(Board board);
    public List<Board> getBoardList();
    public Board getBoard(int bno);
    public int updateBoard(Board board);
    public int deleteBoard(int bno);
}
