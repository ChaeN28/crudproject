package com.shinhan.Ch_ym;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.day15.DBUtill;
import com.shinhan.dept.DeptDTO;

public class ChYmDAO {
	// DB연결, 해제시 사용
	Connection conn;
	// SQL문을 DB에 전송
	Statement st;
	PreparedStatement pst;
	// Select결과
	ResultSet rs;
	// insert,delete,update결과는 영향받은 건수
	int resultCount;

	static final String LOGIN = "SELECT MEM_NO FROM MEMBER WHERE status = 'Y' AND MEM_ID = ? AND MEM_PW = ?";
	static final String PLAYLIST = "SELECT MUSIC_TITLE, MUSIC_SINGER,LIST_TITLE FROM PLAYLIST JOIN MUSIC USING(MUSIC_NO) WHERE MEM_NO = ?";
	static final String MEMBER = "SELECT MEM_NICKNAME FROM MEMBER WHERE MEM_NO = ?";

	// 회원 닉네임 찾기
	public String selectMember(int MEM_NO) {
		String MEM_NICKNAME = "";
		conn = DBUtil.getConnection();

		try {
			PreparedStatement pst = conn.prepareStatement(MEMBER);
			pst.setInt(1, MEM_NO);
			rs = pst.executeQuery();
			while (rs.next()) {
				MEM_NICKNAME = rs.getString("MEM_NICKNAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, rs);
		}

		return MEM_NICKNAME;
	}

	// 회원가입
	public int insertMember(ChYmDTO Chmem) {
		conn = DBUtil.getConnection();
		String sql = "INSERT INTO MEMBER values(SEQ_MEMBER.NEXTVAL,?,?,?,?,?,?,?,'Y')";

		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, Chmem.getMem_id());
			pst.setString(2, Chmem.getMem_pw());
			pst.setString(3, Chmem.getMem_name());
			pst.setString(4, String.valueOf(Chmem.getMem_gender())); // CHAR(1) 으로 형변환(한글자까지 문자열로 변환)
			pst.setString(5, Chmem.getMem_email());
			pst.setInt(6, Chmem.getMem_age());
			pst.setString(7, Chmem.getMem_nickname());

			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, null);
		}

		return resultCount;

	}

	// 로그인
	public int login(String mem_id, String mem_pw) {
		conn = DBUtil.getConnection();
		ResultSet rs = null;
		// 회원번호는 1부터 시작이기 때문에 0이면 회원이 아닌것
		int MEM_NO = 0;

		try {
			pst = conn.prepareStatement(LOGIN);
			pst.setString(1, mem_id);
			pst.setString(2, mem_pw);

			rs = pst.executeQuery();
			if (rs.next()) {
				MEM_NO = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, rs);
		}

		return MEM_NO;
	}

	// 음원 전체조회
	public List<MusicDTO> selectMusiclist() {
		List<MusicDTO> Musiclist = new ArrayList<>();
		conn = DBUtil.getConnection();
		String sql = "SELECT * FROM MUSIC";
		try {
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				MusicDTO music = makeMusic(rs);
				Musiclist.add(music);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}

		return Musiclist;
	}

	private MusicDTO makeMusic(ResultSet rs) throws SQLException {
		MusicDTO music = MusicDTO.builder().music_no(rs.getInt(1)).music_title(rs.getString(2))
				.music_singer(rs.getString(3)).build();
		return music;
	}

	// 나의 재생목록 조회
//	public List<MemberPlayListDTO> selectMyPlaylist(int MEM_NO) {
//		List<MemberPlayListDTO> MyPlaylist = new ArrayList<>();
//		String MEM_NICKNAME = selectMember(MEM_NO);
//		
//		try {
//			conn = DBUtil.getConnection();
//			PreparedStatement pst = conn.prepareStatement(PLAYLIST);
//			pst.setInt(1, MEM_NO);
//			ResultSet rs = pst.executeQuery();
//			while(rs.next()) {
//				//음악이랑, 닉네임을 넣어야함
//				MusicDTO music = makeMusic(rs);
//				MyPlaylist.add(music);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBUtill.dbDisconnect(conn, pst, rs);
//		}
//		
//		return Playlist;
//	}

	// 재생목록 조회
	public List<MemberPlayListDTO> selectPlaylist(int MEM_NO) {
		List<MemberPlayListDTO> Playlist = new ArrayList<>();
		String sql = "SELECT * FROM MEMBERPLAYLIST WHERE MEM_NO = ?";
		try {
			conn = DBUtil.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, MEM_NO);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				MemberPlayListDTO list = makeList(rs);
				Playlist.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, rs);
		}

		return Playlist;
	}

	// 재생목록 객체 함수
	private MemberPlayListDTO makeList(ResultSet rs) throws SQLException {
		MemberPlayListDTO Playlist = MemberPlayListDTO.builder().list_num(rs.getInt("LIST_NO"))
				.list_Status(rs.getString("STATUS").charAt(0)).list_title(rs.getString("LIST_TITLE"))
				.mem_no(rs.getInt("MEM_NO")).build();

		return Playlist;
	}

	// 재생목록 추가
	public int insertPlaylist(MemberPlayListDTO mplist, int MEM_NO) {
		int resultCount = 0;
		conn = DBUtil.getConnection();
		PreparedStatement pst = null;
		String sql = "INSERT INTO MEMBERPLAYLIST VALUES(SEQ_PLAYLIST.NEXTVAL,?,?,?)";
		try {

			pst = conn.prepareStatement(sql);
			pst.setString(1, mplist.getList_title());
			pst.setString(2, String.valueOf(mplist.getList_Status()));
			pst.setInt(3, MEM_NO);

			resultCount = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, null);
		}

		return resultCount;
	}

	private MusicDTO makeDetailPlaylist(ResultSet rs) throws SQLException {
		MusicDTO DetailPlaylist = MusicDTO.builder().music_no(rs.getInt(1)).music_title(rs.getString(3))
				.music_singer(rs.getString(2)).build();

		return DetailPlaylist;
	}
	
	
	//재생목록 삭제
	public int deletePlaylist(int MEM_NO,String list_title) {
		int resultCount = 0;
		conn = DBUtil.getConnection();
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		String sql = """
				DELETE FROM PLAYLIST
				WHERE LIST_NO = (SELECT LIST_NO
				FROM MEMBERPLAYLIST 
				WHERE LIST_TITLE = ?)
				""";
		String sql2 = """
				DELETE FROM MEMBERPLAYLIST 
				WHERE LIST_NO =(SELECT LIST_NO
				FROM MEMBERPLAYLIST 
				WHERE LIST_TITLE = ?)
				""";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, list_title);
			
			pst2 = conn.prepareStatement(sql2);
			pst2.setString(1, list_title);

			int result1 = pst.executeUpdate();
			int result2 = pst2.executeUpdate();
			
			resultCount = result1 + result2;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, null);
		}
		return resultCount;
	}
	


	// 재생목록 상세조회
	public List<MusicDTO> selectPlaylistDatail(String list_title) {

		List<MusicDTO> DetailPlaylist = new ArrayList<>();
		String sql = """
				SELECT M.MUSIC_NO, M.MUSIC_SINGER, M.MUSIC_TITLE
				FROM MEMBERPLAYLIST MP
				JOIN PLAYLIST P ON MP.LIST_NO = P.LIST_NO
				JOIN MUSIC M ON P.MUSIC_NO = M.MUSIC_NO
				WHERE MP.LIST_TITLE = ?
				""";

		try {
			conn = DBUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, list_title);
			rs = pst.executeQuery();

			while (rs.next()) {
				MusicDTO music = makeDetailPlaylist(rs);
				DetailPlaylist.add(music);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, rs);
		}

		return DetailPlaylist;
	}

	// 재생목록에 음원추가
	public int insertListMusic(PlaylistDTO plist, int MEM_NO) {
		int resultCount = 0;
		conn = DBUtil.getConnection();
		PreparedStatement pst2 = null;

		// String Sql = "INSERT INTO PLAYLIST (LIST_NO, MUSIC_NO) SELECT ?, ? FROM
		// MEMBERPLAYLIST WHERE MEM_NO = ?";
		String Sql = """
				INSERT INTO PLAYLIST (LIST_NO, MUSIC_NO)
				SELECT LIST_NO, ?
				FROM MEMBERPLAYLIST
				WHERE LIST_TITLE = ?
				AND MEM_NO = ?
				""";

		// 음원 제목으로 음원 번호 찾기
		String sql2 = "SELECT MUSIC_NO FROM MUSIC WHERE MUSIC_TITLE = ? ";

		try {
			// 음원 제목으로 음원 번호 찾기
			pst2 = conn.prepareStatement(sql2);

			pst2.setString(1, plist.music_title);

			int music_no2 = 0;

			ResultSet rs2 = pst2.executeQuery();
			while (rs2.next()) {
				music_no2 = rs2.getInt(1);
			}

			pst = conn.prepareStatement(Sql);
			pst.setInt(1, music_no2);
			pst.setString(2, plist.list_name);
			pst.setInt(3, MEM_NO);

//			pst = conn.prepareStatement(Sql);
//			pst.setInt(1,plist.list_no);
//			pst.setInt(2,music_no2);
//			pst.setInt(3,MEM_NO);

			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(null, pst2, null);
			DBUtill.dbDisconnect(conn, pst, null);
		}
		return resultCount;
	}

	// 재생목록에서 음원 삭제
	public int deleteListMusic(PlaylistDTO plist, int MEM_NO) {
		int resultCount = 0;
		conn = DBUtil.getConnection();
		PreparedStatement pst2 = null;
		String sql = """
				DELETE FROM PLAYLIST
				WHERE LIST_NO = (SELECT LIST_NO FROM MEMBERPLAYLIST WHERE LIST_TITLE = ?)
				AND MUSIC_NO = ?
				""";

		// 음원 제목으로 음원 번호 찾기
		String sql2 = "SELECT MUSIC_NO FROM MUSIC WHERE MUSIC_TITLE = ? ";

		// 음원 제목으로 음원 번호 찾기
		try {
			pst2 = conn.prepareStatement(sql2);

			pst2.setString(1, plist.music_title);

			int music_no2 = 0;

			ResultSet rs2 = pst2.executeQuery();
			while (rs2.next()) {
				music_no2 = rs2.getInt(1);
			}

			// 음원삭제하기
			pst = conn.prepareStatement(sql);
			pst.setString(1, plist.list_name);
			pst.setInt(2, music_no2);

			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(null, pst2, null);
			DBUtill.dbDisconnect(conn, pst, null);
		}

		return resultCount;
	}

	// 재생목록 전체공개, 비공개 처리
	public int updateStatus(String list_name, char list_status) {
		int resultCount = 0;
		conn = DBUtil.getConnection();
		String sql = """
				UPDATE MEMBERPLAYLIST
					SET STATUS = ?
					WHERE LIST_NO = (SELECT LIST_NO FROM MEMBERPLAYLIST WHERE LIST_TITLE = ?)
				""";

		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, String.valueOf(list_status));
			pst.setString(2, list_name);

			resultCount = pst.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, null);
		}

		return resultCount;
	}

	// 재생목록 닉네임으로 검색
	// 닉네임으로 재생목록 검색 후 재생목록 상세보기 할건지 물어보기
	public List<MemberPlayListDTO> selectListNName(String mem_nickname) {
		List<MemberPlayListDTO> Playlist = new ArrayList<>();
		conn = DBUtil.getConnection();

		String sql = """
				SELECT *
				FROM MEMBERPLAYLIST
				WHERE MEM_NO = (SELECT MEM_NO FROM MEMBER WHERE MEM_NICKNAME= ?)
				AND STATUS = 'Y'
				""";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, mem_nickname);

			rs = pst.executeQuery();

			while (rs.next()) {
				MemberPlayListDTO list = makeList(rs);
				Playlist.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}

		return Playlist;
	}

	// 재생목록 음원이름으로 검색
	public List<MemberPlayListDTO> selectListMName(String music_name) {
		List<MemberPlayListDTO> Playlist = new ArrayList<>();
		conn = DBUtil.getConnection();
		String mem_nickname = "";

		String sql = """
				SELECT *
					FROM MEMBERPLAYLIST
					WHERE LIST_NO IN (
					SELECT LIST_NO
					FROM PLAYLIST
					WHERE MUSIC_NO = (
						SELECT MUSIC_NO
						FROM MUSIC
						WHERE MUSIC_TITLE = ?
							AND STATUS = 'Y'
							)
						)
				""";
		String sql2 = """
				SELECT MEM_NICKNAME
				FROM MEMBER
				WHERE MEM_NO = ?
				""";

		try {
			conn = DBUtil.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, music_name);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				MemberPlayListDTO list = makeList(rs);
				Playlist.add(list);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, pst, rs);
		}

		return Playlist;
	}

	// 회원탈퇴
	public int deleteMember(String del, int MEM_NO) {
		conn = DBUtil.getConnection();
		int resultCount = 0;
		String sql = """
				UPDATE MEMBER
				SET STATUS = 'N'
				WHERE MEM_NO = ?
				""";
		try {
			pst = conn.prepareStatement(sql);
			
			pst.setInt(1, MEM_NO);
			
			resultCount = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultCount;
	}

	// 좋아요 추가
	public int insertFavorite(String music_title, int MEM_NO) {
		int resultCount = 0;
		int music_no = findMusic_No(music_title);
		conn = DBUtil.getConnection();
		String sql = """
				INSERT INTO FAVORITE VALUES(SEQ_PLAYLIST.NEXTVAL,?,?)
				""";
		
		try {
			pst = conn.prepareStatement(sql);
			
			pst.setInt(1, MEM_NO);
			pst.setInt(2, music_no);
			
			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		
		return resultCount;
	}
	
	private MusicDTO makeDetailPlaylist2(ResultSet rs) throws SQLException {
		MusicDTO DetailPlaylist = MusicDTO.builder().music_title(rs.getString(1))
				.music_singer(rs.getString(2)).build();
		return DetailPlaylist;
	}
	
	// 좋아요 리스트 보여주기
	public List<MusicDTO>  selectFavoriteDatail(int MEM_NO) {
		List<MusicDTO> FavoriteDatail = new ArrayList<>();
		conn = DBUtil.getConnection();
		
		String sql = """
				SELECT  MUSIC_TITLE, MUSIC_SINGER
				FROM MUSIC
				WHERE MUSIC_NO IN (SELECT MUSIC_NO
					FROM FAVORITE
					WHERE MEM_NO = ?)
				""";
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, MEM_NO);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				MusicDTO music = makeDetailPlaylist2(rs);
				FavoriteDatail.add(music);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return FavoriteDatail;
	}
	
	//음원 제목으로 음원번호 알아내기 함수
	public int findMusic_No(String music_title) {
		int music_no2 = 0;
		conn = DBUtil.getConnection();
		
		// 음원 제목으로 음원 번호 찾기
		String sql = "SELECT MUSIC_NO FROM MUSIC WHERE MUSIC_TITLE = ? ";

		// 음원 제목으로 음원 번호 찾기
		try {
			pst = conn.prepareStatement(sql);

			pst.setString(1, music_title);

			rs = pst.executeQuery();
			while(rs.next()) {
				music_no2 = rs.getInt(1);
			}
			
		
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return music_no2;
	}

}
