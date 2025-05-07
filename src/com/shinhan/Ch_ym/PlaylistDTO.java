package com.shinhan.Ch_ym;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
	int list_no;
	int music_no;
	String list_name;
	String music_title;
}
