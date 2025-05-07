package com.shinhan.Ch_ym;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberPlayListDTO {
	int list_num;
	String list_title;
	char list_Status;
	int mem_no;

}
