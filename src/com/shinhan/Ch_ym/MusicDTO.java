package com.shinhan.Ch_ym;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor	
@NoArgsConstructor
public class MusicDTO {

	private int music_no;
	private String music_title;
	private String music_singer;
}
