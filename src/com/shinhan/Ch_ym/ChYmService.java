package com.shinhan.Ch_ym;

import java.util.List;

public class ChYmService {
	ChYmDAO chYmDAO = new ChYmDAO();
	
	//회원가입
	public int insertMember(ChYmDTO Chmem) {
		int resultCount = chYmDAO.insertMember(Chmem);
		
		return resultCount;
	}

	// 로그인
	public int login(String mem_id, String mem_pw) {

		int MEM_NO = chYmDAO.login(mem_id,mem_pw);
		
		return MEM_NO;
		
	}

	// 재생목록조회
	public List<MemberPlayListDTO> selectPlaylist(int MEM_NO) {
		
		return chYmDAO.selectPlaylist(MEM_NO);
		
	}

	//재생목록추가
	public int insertPlaylist(MemberPlayListDTO mplist, int MEM_NO) {
		
		return chYmDAO.insertPlaylist(mplist,MEM_NO);
	}

	//음원 전체조회
	public List<MusicDTO> selectMusiclist() {
		
		return chYmDAO.selectMusiclist();
	}
	
	//재생목록에 음원추가
	public int insertListMusic(PlaylistDTO plist, int MEM_NO) {
		
		return chYmDAO.insertListMusic(plist,MEM_NO);
	}

	// 재생목록 상세보기
	public List<MusicDTO> selectPlaylistDatail(String list_title) {
		return chYmDAO.selectPlaylistDatail(list_title);
		
	}

	// 재생목록에서 음원 삭제
	public int deleteListMusic(PlaylistDTO plist, int MEM_NO) {
		
		return chYmDAO.deleteListMusic(plist,MEM_NO);
	}

	public int updateStatus(String list_name, char list_status) {

		return chYmDAO.updateStatus(list_name,list_status);
	}

	//재생목록 닉네임으로 검색
	public List<MemberPlayListDTO> selectListNName(String mem_nickname) {
		
		return chYmDAO.selectListNName(mem_nickname);
		
	}

	//재생목록 음원이름으로 검색
	public List<MemberPlayListDTO> selectListMName(String music_name) {


		return chYmDAO.selectListMName(music_name);
	}
	
	//회원탈퇴
	public int deleteMember(String del,int MEM_NO) {
		
		return chYmDAO.deleteMember(del,MEM_NO);
		
	}

	// 좋아요에 추가
	public int insertFavorite(String music_title, int MEM_NO) {
		
		return chYmDAO.insertFavorite(music_title,MEM_NO);
	}
	
	// 좋아요 리스트 보여주기
	public List<MusicDTO>  selectFavoriteDatail(int MEM_NO) {
		
		return chYmDAO.selectFavoriteDatail(MEM_NO);
		
	}

	// 재생목록 삭제
	public int deletePlaylist(String list_title, int mEM_NO) {
		
		return chYmDAO.deletePlaylist(mEM_NO, list_title);
	}

}
