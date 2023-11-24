package com.shop.mapper;

import com.shop.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insertBoard(Board board);
    List<Board> getBoardList();
    Board getBoard(int bno);
    int updateBoard(Board board);
    int deleteBoard(int bno);
    void updateVisited(int bno);
}
