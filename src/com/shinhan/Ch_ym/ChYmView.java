package com.shinhan.Ch_ym;

import java.util.List;

public class ChYmView {

    public static void menuDisplay() {
    	   System.out.println("\n======================= 🎵 Ch.ym 🎵 =======================");
    	    System.out.println("1. 회원가입");
    	    System.out.println("2. 로그인");
    	    System.out.println("3. 🎶 전체 음원 조회");
    	    System.out.println("4. ❤️ 좋아요");
    	    System.out.println("5. 📃 재생목록 조회");
    	    System.out.println("6. ➕ 재생목록 추가");
    	    System.out.println("7. 🔐 재생목록 전체공개/비공개 설정");
    	    System.out.println("8. 🔍 닉네임으로 재생목록 검색");
    	    System.out.println("9. 🔍 음원이름으로 재생목록 검색");
    	    System.out.println("10. 🗑️ 회원탈퇴");
    	    System.out.println("11. 🗑️ 재생목록 삭제");
    	    System.out.println("12. 로그아웃");
    	    System.out.println("13. ❌ 종료(뒤로가기)");
    	    System.out.println("===========================================================");
    	    System.out.print("👉 메뉴선택 >> ");
    }

    // 재생목록 전체 조회
    public static void MembermusicList(List<MemberPlayListDTO> playlist, int MEM_NO) {
        ChYmDAO chdao = new ChYmDAO();

        System.out.println("\n🎵===== 내 재생목록 조회 =====🎵");
        if (playlist.isEmpty()) {
            System.out.println("⚠️  재생목록이 없습니다.");
        } else {
            System.out.println("👤 " + chdao.selectMember(MEM_NO) + "님의 재생목록:");
            for (MemberPlayListDTO list : playlist) {
                System.out.println("   ▶ [재생목록] " + list.getList_title());
            }
        }
        System.out.println("────────────────────────────────────\n");
    }

    // 다른 사용자 재생목록 전체 조회
    public static void MembermusicList(List<MemberPlayListDTO> playlist, String mem_nickname) {
        System.out.println("\n👥 " + mem_nickname + "님의 재생목록:");
        for (MemberPlayListDTO list : playlist) {
            System.out.println("   ▶ [재생목록] " + list.getList_title());
        }
        System.out.println("────────────────────────────────────\n");
    }

    // 음원이 포함된 재생목록 전체 조회
    public static void MembermusicListByMusic(List<MemberPlayListDTO> playlist, String music_name) {
        System.out.println("\n🎶 '" + music_name + "'이(가) 포함된 재생목록:");
        for (MemberPlayListDTO list : playlist) {
            System.out.println("   ▶ [재생목록] " + list.getList_title());
        }
        System.out.println("────────────────────────────────────\n");
    }

    // 전체 음원 조회
    public static void selectMusiclist(List<MusicDTO> musiclist) {
        System.out.println("\n🎼============= 전체 음원 목록 =============🎼");
        for (MusicDTO music : musiclist) {
            System.out.printf("   🎵 %d. %s - %s%n", music.getMusic_no(), music.getMusic_title(), music.getMusic_singer());
        }
        System.out.println("────────────────────────────────────\n");
    }

    // 재생목록 상세 조회
    public static void selectPlaylistDatail(List<MusicDTO> DetailPlaylist) {
        System.out.println("\n📂 재생목록 음원 :");
        for (MusicDTO m : DetailPlaylist) {
            System.out.println("   🎵 " + m.getMusic_title() + " - " + m.getMusic_singer());
        }
        System.out.println("────────────────────────────────────\n");
    }
}
