package com.shinhan.Ch_ym;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChYmDTO {
	 private int mem_no;
	 private String mem_id;
	 private String mem_pw;
	 private String mem_name;
	 private char mem_gender;
	 private char status;
	 private String mem_email;
	 private int mem_age;
	 private String mem_nickname;

}
