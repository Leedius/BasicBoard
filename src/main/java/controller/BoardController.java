package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import dto.BoardDTO;


@WebServlet("*.do")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//글넘버에 들어갈 변수 선언   
	private int boardNum;
	//게시글 들을 저장할 객체 선언
	private List<BoardDTO> boardList;

	
    public BoardController() {
        super();
        boardNum = 1;
        boardList = new ArrayList<>();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	
	public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//한글 인코딩
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//어떤 페이지에서 요청이 왓는지 확인
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		
		
		//응답 페이지
		String page = "";
		boolean isRedirect = false;
		
		//게시글 목록 페이지로 이동
		if(command.equals("/boardList.do")) {
			request.setAttribute("list", boardList);
			//이동할 페이지 지정
			page = "board_list.jsp";
		}
		
		//글쓰기 페이지로 이동
		if(command.equals("/regBoardForm.do")) {
			//이동할 페이지 지정
			page = "board_write_form.jsp";
		}
		
		//글 등록 실행
		if(command.equals("/regBoard.do")) {
			//데이터 받기
			String title = request.getParameter("title");
			String writer = request.getParameter("writer");
			String createDate = request.getParameter("createDate");
			String content = request.getParameter("content");
			
			//위에서 전달받은 데이터를 갖는 게시글 생성
			BoardDTO write = new BoardDTO(boardNum, title, content, writer, createDate);
			boardNum++;
			
			//글 등록
			boardList.add(write);
			
			page = "boardList.do";
			isRedirect = true;
		}
		
		//글 상세 페이지 이동
		if(command.equals("/boardDetail.do")) {
			int num = Integer.parseInt(request.getParameter("boardNum"));
			for(BoardDTO board : boardList) {
				if(board.getBoardNum() == num) {
					request.setAttribute("detail", board);
					System.out.println(boardNum);
				}
			}
			
			page = "select_board_detail.jsp";
			
		}
		
		//글 수정 페이지 이동하면서 상세페이지 정보 넘기기
		if(command.equals("/detailToUpdate.do")) {
			int num = Integer.parseInt(request.getParameter("boardNum"));
			for(BoardDTO board : boardList) {
				if(board.getBoardNum() == num) {
					request.setAttribute("board", board);
				}
			}
			page = "update_write_form.jsp";
		}
		
		
		
		//글 수정
		if(command.equals("/updateBoardDetail.do")) {
			int num = Integer.parseInt(request.getParameter("boardNum"));
			for(BoardDTO board : boardList) {
				if(board.getBoardNum() == num) {
					String title = request.getParameter("title");
					String writer = request.getParameter("writer");
					String createDate = request.getParameter("createDate");
					String content = request.getParameter("content");
					
					board.setTitle(title);
					board.setWriter(writer);
					board.setCreateDate(createDate);
					board.setContent(content);
				}
			}
			page = "boardDetail.do";
		}
		
		//글 삭제
		if(command.equals("/deleteBoard.do")) {
			int num = Integer.parseInt(request.getParameter("boardNum"));
			for(int i = 0; i < boardList.size(); i++) {
				if(boardList.get(i).getBoardNum() == num) {
					
					//글삭제
					boardList.remove(boardList.get(i));
				}
			}
			page = "boardList.do";
		}
			
	
		//페이지 이동
		if(isRedirect) {
			response.sendRedirect(page);
		}
		
		else if(!isRedirect) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(page);
			dispatcher.forward(request, response);	
		}
		
	}
	
}	

