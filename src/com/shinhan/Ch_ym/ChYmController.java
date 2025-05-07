package com.shinhan.Ch_ym;

import java.util.List;
import java.util.Scanner;

public class ChYmController {
	static Scanner sc = new Scanner(System.in);
	static ChYmService chYmService = new ChYmService();

	public static void main(String[] args) {
		execute();
	}

	public static void execute() {
		boolean isStop = false;
		int MEM_NO = 0;

		while (!isStop) {
			ChYmView.menuDisplay();
			int job = sc.nextInt();
			
			switch (job) {
			case 1 -> {
				f_insertMember();
			}
			case 2 -> {
				MEM_NO = f_login();
			}
			case 3 -> {
				
				f_selectMusiclist();
			}
			case 4 ->{
				f_insertFavorite(MEM_NO);
			}
			case 5 -> {
				f_selecPlaylist(MEM_NO);
			}
//			case 6 -> {
//				f_selectPlaylistDatail(MEM_NO);
//			}
			case 6 -> {
				f_insertPlaylist(MEM_NO);
			}
//			case 7 -> {
//				f_insertListMusic(MEM_NO);
//			}
//			case 8 -> {
//				f_deleteListMusic(MEM_NO);
//			}
			case 7 -> {
				f_updateStatus(MEM_NO);
			}
			case 8 -> {
				f_selectListNName(MEM_NO);
			}
			case 9 -> {
				f_selectListMName();
			}
			case 10 -> {
				f_deleteMember(MEM_NO);
			}
			case 11 -> {
				f_deletePlaylist(MEM_NO);
			}
			case 12 -> {
				MEM_NO = f_logOut(MEM_NO);
			}
			case 13 -> {
				isStop = true;
			}
			}

		}
	}

	// 재생목록 삭제
	private static int f_deletePlaylist(int MEM_NO) {
		int resultCount = 0;
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			System.out.print("삭제할 재생목록을 선택하세요 : ");
			sc.nextLine();
			String list_title = sc.nextLine();


			resultCount = chYmService.deletePlaylist(list_title, MEM_NO);
		}

		if (resultCount == 2) {
			System.out.println("삭제 성공!");
		} else {
			System.out.println("삭제 실패!");
		}

		return resultCount;
		
	}

	// 회원가입
	private static void f_insertMember() {
		 System.out.println("\n🌟 [회원가입] 아래 정보를 입력해주세요.");
		
		System.out.print("아이디를 입력하세요 : ");
		String mem_id = sc.next();

		System.out.print("비밀번호를 입력하세요 : ");
		String mem_pw = sc.next();

		System.out.print("이름을 입력하세요 : ");
		String mem_name = sc.next();

		System.out.print("닉네임을 입력하세요 : ");
		String mem_nickname = sc.next();

		System.out.print("성별을 입력하세(F/M) : ");
		char mem_gender = sc.next().charAt(0);

		System.out.print("이메일을 입력하세요 : ");
		String mem_email = sc.next();

		System.out.print("나이를 입력하세요 : ");
		int mem_age = sc.nextInt();

		ChYmDTO Chmem = ChYmDTO.builder().mem_age(mem_age).mem_name(mem_name).mem_email(mem_email)
				.mem_gender(mem_gender).mem_id(mem_id).mem_nickname(mem_nickname).mem_pw(mem_pw).build();

		int resultCount = chYmService.insertMember(Chmem);

		if (resultCount == 0) {
			 System.out.println("❌ 회원가입 실패! 입력값을 다시 확인해주세요.");
		} else {

			System.out.println("🎉 회원가입 성공! 환영합니다, " + mem_nickname + "님!");

		}

	}

	// 로그인
	public static int f_login() {
		System.out.println("\n🔐 [로그인]");
		
		System.out.print("아이디를 입력하세요 : ");
		String mem_id = sc.next();

		System.out.print("비밀번호를 입력하세요 : ");
		String mem_pw = sc.next();

		int MEM_NO = chYmService.login(mem_id, mem_pw);

		if (MEM_NO == 0) {
			System.out.println("❌ 로그인 실패! 회원 정보를 확인해주세요.");
		} else
			 System.out.println("✅ 로그인 성공! 환영합니다 :)");

		return MEM_NO;
	}
	
	public static int f_logOut(int MEM_NO) {
	    if (MEM_NO == 0) {
	        System.out.println("❌ 로그인하지 않았습니다.");
	        return MEM_NO;  // 이미 로그인이 안 된 상태에서 로그아웃 시도
	    }

	    // 로그아웃 처리
	    MEM_NO = 0;  // 로그아웃하면 MEM_NO를 0으로 설정

	    System.out.println("✅ 로그아웃 성공!");
	    return MEM_NO;  // 로그아웃 후 0 반환
	}

	// 음원목록 조회
	private static void f_selectMusiclist() {
		List<MusicDTO> Musiclist = chYmService.selectMusiclist();

		ChYmView.selectMusiclist(Musiclist);
	}

	// 재생목록 조회
	private static void f_selecPlaylist(int MEM_NO) {
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			List<MemberPlayListDTO> Playlist = chYmService.selectPlaylist(MEM_NO);
			ChYmView.MembermusicList(Playlist, MEM_NO);
			
			// Playlist가 존재한다면 상세보기 함수 호출
			if(!Playlist.isEmpty()) {
				
				// 사용자에게 물어보기
				System.out.println("1. 상세보기");
				System.out.println("0. 뒤로가기");
				System.out.print("선택: ");
				String choice2 = sc.next();
				
				  switch (choice2) {
			        case "1" -> {
			            System.out.println("\n🎶 [재생목록 상세보기]");
			            f_selectPlaylistDatail(MEM_NO);
			        }
			        case "0" -> {
			            System.out.println("🔙 이전 메뉴로 돌아갑니다.");
			            return;
			        }
			        default -> {
			            System.out.println("❌ 잘못된 입력입니다. 처음부터 다시 시도해주세요.");
			        }
			    }
				

			    System.out.println("\n📌 [재생목록 관리 메뉴]");
			    System.out.println("1. 음원 추가");
			    System.out.println("2. 음원 삭제");
			    System.out.println("0. 뒤로가기");
			    System.out.print("선택: ");
			    String choice = sc.next();

			    switch (choice) {
			        case "1" -> {
			            System.out.println("\n🎶 [재생목록에 음원 추가]");
			            f_insertListMusic(MEM_NO);
			        }
			        case "2" -> {
			            System.out.println("\n🗑️ [재생목록에서 음원 삭제]");
			            f_deleteListMusic(MEM_NO);
			        }
			        case "0" -> {
			            System.out.println("🔙 이전 메뉴로 돌아갑니다.");
			            return;
			        }
			        default -> {
			            System.out.println("❌ 잘못된 입력입니다. 처음부터 다시 시도해주세요.");
			        }
			    }
			}
			
		}

	}

	// 재생목록 상세조회
	private static void f_selectPlaylistDatail(int MEM_NO) {
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {

			System.out.print("\n🔍 상세 보기할 재생목록의 제목을 입력하세요: ");
			// int LIST_NO = sc.nextInt();
			sc.nextLine();
			String list_title = sc.nextLine();

			List<MusicDTO> DetailPlaylist = chYmService.selectPlaylistDatail(list_title);

			   if (DetailPlaylist.isEmpty()) {
		            System.out.println("\n📂 해당 재생목록은 비어 있습니다.");
		            
					// 비어있을 경우 음원 추가 의사 묻기
		            System.out.println("\n");
					System.out.println("1. 재생목록에 음원 추가");
					System.out.println("0. 뒤로가기");
					System.out.print("선택: ");
					String choice = sc.next();
					
					  switch (choice) {
				        case "1" -> {
				        	// 음원추가 함수 호출
				            System.out.println("\n🎶 [재생목록에 음원 추가]");
				            f_insertListMusic(MEM_NO);
				        }
				        case "0" -> {
				            System.out.println("🔙 이전 메뉴로 돌아갑니다.");
				            return;
				        }
				        default -> {
				            System.out.println("❌ 잘못된 입력입니다. 처음부터 다시 시도해주세요.");
				        }
				    }
		        } else {
		            System.out.println("\n🎵 '" + list_title + "'");
		            System.out.println("========================================");
		            ChYmView.selectPlaylistDatail(DetailPlaylist);
		            System.out.println("========================================");
		        }

		}

	}

	// 재생목록 추가
	private static int f_insertPlaylist(int MEM_NO) {
		int resultCount = 0;

		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			 System.out.print("\n🎵 재생목록의 제목을 입력하세요: ");
			sc.nextLine();
			String list_title = sc.nextLine();

		    System.out.print("\n🔒 재생목록을 공개할까요? (Y: 공개, N: 비공개): ");
			char list_public = sc.next().charAt(0);
			sc.nextLine();

			MemberPlayListDTO mplist = MemberPlayListDTO.builder().list_title(list_title).list_Status(list_public)
					.build();
			
			resultCount = chYmService.insertPlaylist(mplist, MEM_NO);
		}
		 if (resultCount == 0) {
		        System.out.println("\n❌ 재생목록 생성 실패! 다시 시도해주세요.");
		    } else {
		        System.out.println("\n✅ 재생목록 생성 성공! 새로 만든 재생목록을 확인해 보세요.");
		    }

		return resultCount;
	}

	// 재생목록에 음원추가
	public static int f_insertListMusic(int MEM_NO) {
		int resultCount = 0;
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		}
			System.out.println("\n📂 [재생목록에 음원 추가]");
			 System.out.print("▶ 재생목록 이름을 입력하세요: ");
			sc.nextLine();
			String list_name = sc.nextLine();

			System.out.print("🎵 추가할 음원 제목을 입력하세요: ");
			String music_title = sc.nextLine();

//			System.out.print("재생목록의 번호를 입력하세요 : ");
//			int list_no = sc.nextInt();
//			
//			System.out.print("음원 번호를 입력하세요 : ");
//			int music_no = sc.nextInt();

			PlaylistDTO plist = PlaylistDTO.builder().list_name(list_name).music_title(music_title).build();

			resultCount = chYmService.insertListMusic(plist, MEM_NO);
		

		if (resultCount == 0) {
			   System.out.println("\n❌ [실패] 음원 추가에 실패했습니다. 입력값을 확인해주세요.");
		} else {
			  System.out.println("\n✅ [성공] '" + music_title + "' 음원이 '" + list_name + "' 재생목록에 추가되었습니다!");
		}

		return resultCount;
	}

	// 재생목록에서 음원 삭제
	private static int f_deleteListMusic(int MEM_NO) {
		int resultCount = 0;
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		}
		    System.out.println("\n🗂 [재생목록에서 음원 삭제]");
		    System.out.print("▶ 삭제할 재생목록 이름을 입력하세요: ");
			sc.nextLine();
			String list_name = sc.nextLine();

			System.out.print("🎵 삭제할 음원 제목을 입력하세요: ");
			String music_title = sc.nextLine();

			PlaylistDTO plist = PlaylistDTO.builder().list_name(list_name).music_title(music_title).build();

			resultCount = chYmService.deleteListMusic(plist, MEM_NO);

		if (resultCount == 0) {
			 System.out.println("\n❌ [실패] 음원 삭제에 실패했습니다. 목록 이름이나 음원 제목을 다시 확인해주세요.");
		} else {
			 System.out.println("\n✅ [성공] '" + music_title + "' 음원이 '" + list_name + "' 재생목록에서 삭제되었습니다.");
		}

		return resultCount;

	}

	// 재생목록 전체공개, 비공개 처리
	public static int f_updateStatus(int MEM_NO) {
		int resultCount = 0;
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			sc.nextLine();

			 System.out.print("\n🎵 공개/비공개 설정할 재생목록을 입력하세요: ");
			String list_name = sc.nextLine();

			 System.out.print("\n🔒 재생목록을 공개할까요? (Y: 공개, N: 비공개): ");
			char list_status = sc.nextLine().trim().toUpperCase().charAt(0);

			resultCount = chYmService.updateStatus(list_name, list_status);
		}


	    if (resultCount == 0) {
	        System.out.println("\n❌ 변경 실패! 다시 시도해주세요.");
	    } else {
	        System.out.println("\n✅ 변경 성공! 재생목록 상태가 업데이트되었습니다.");
	    }

		return resultCount;
	}

	// 재생목록 닉네임으로 검색
	public static void f_selectListNName(int MEM_NO) {
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			sc.nextLine();

			System.out.print("닉네임 : ");
			String mem_nickname = sc.nextLine();

			List<MemberPlayListDTO> Playlist = chYmService.selectListNName(mem_nickname);
			ChYmView.MembermusicList(Playlist, mem_nickname);
			
			if(Playlist.isEmpty()) {
				System.out.println(mem_nickname +"님은 아직 재생목록을 생성하지 않았습니다.");
				return;
			}else {
				System.out.print("▶ 상세보기 할 재생목록 : ");
				String list_name = sc.nextLine();

				List<MusicDTO> PlaylistDetail = chYmService.selectPlaylistDatail(list_name);
				ChYmView.selectPlaylistDatail(PlaylistDetail);
			}
		}

	}

	// 재생목록 음원이름으로 검색
	public static void f_selectListMName() {
		sc.nextLine();
		System.out.print("\n🎶 음원이름을 입력하세요: ");
		String music_name = sc.nextLine();

		// 음원리스트 가져오기
		List<MemberPlayListDTO> Playlist = chYmService.selectListMName(music_name);
		ChYmView.MembermusicListByMusic(Playlist, music_name);

		if (Playlist.isEmpty()) {
			 System.out.println("\n❌ 해당 음원이 포함된 재생목록이 없습니다.");
			return;

		}

		// 상세보기할 음원리스트 이름 받기
		 System.out.print("\n🎵 상세보기 할 재생목록의 이름을 입력하세요: ");
		String list_name = sc.nextLine();

		// 음원리스트 이름 받아서 넘기기
		List<MusicDTO> PlaylistDetail = chYmService.selectPlaylistDatail(list_name);
		ChYmView.selectPlaylistDatail(PlaylistDetail);

	}

	// 음원 좋아요 하기
	public static int f_insertFavorite(int MEM_NO) {
		int resultCount = 0;

		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			
			// 현재 좋아요 리스트 보여주기
			List<MusicDTO> FavoriteDatail = chYmService.selectFavoriteDatail(MEM_NO);
			
			if(!FavoriteDatail.isEmpty()) {
	            System.out.println("\n==============================");
	            System.out.println("        좋아요 목록        ");
	            System.out.println("==============================");
				for(MusicDTO m : FavoriteDatail) {
					System.out.printf("🎵 %s - %s\n",m.getMusic_title(),m.getMusic_singer());
				}
				System.out.println("==============================");
			}else {
	            System.out.println("현재 좋아요한 음원이 없습니다.");
	        }
			sc.nextLine();
			System.out.print("\n🎶음원 제목을 입력하세요 : ");
			String music_title = sc.nextLine();


			resultCount = chYmService.insertFavorite(music_title, MEM_NO);
		}

		if (resultCount == 0) {
			System.out.println("\n❌ 추가 실패!");
		} else {
			 System.out.println("\n✅ 추가 성공!");
		}

		return resultCount;

	}

	// 회원탈퇴
	public static void f_deleteMember(int MEM_NO) {
		String del = "";
		int resultCount = 0;
		if (MEM_NO == 0) {
			System.out.println("⚠️ 로그인 후 사용 가능한 기능입니다.");
			execute();
		} else {
			sc.nextLine();
			 System.out.print("\n🔒 정말 탈퇴하시겠습니까? (Y/N) : ");
			del = sc.nextLine();

			if (del.equals("Y")) {
				resultCount = chYmService.deleteMember(del, MEM_NO);

				if (resultCount == 1) {
	                System.out.println("\n🎉 탈퇴가 완료되었습니다.");
	                System.out.println("그동안 저희 프로그램을 이용해주셔서 감사합니다.");
	                System.out.println("더 나은 서비스로 다시 만나뵙기를 바랍니다.");
				}else {
	                System.out.println("\n❌ 탈퇴 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
	            }
			} else {
				 System.out.println("\n💖 늘 저희 프로그램을 이용해주셔서 감사합니다.");
			}
		}

	}

}
