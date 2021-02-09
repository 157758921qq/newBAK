package com.yz.baglk;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.excel.EasyExcel;
import com.yz.baglk.entity.Register;

@SpringBootTest
class BaglkApplicationTests {

	@Test
	void contextLoads() {
	}
	
	
	@Test
	void testExcel() {
	        List<Register> regs = new ArrayList<>();
	        Register reg = new Register();
	        reg.setId(1);
	        reg.setPatientId("11130");
	        reg.setPatientName("markus");
	        reg.setReason("1");
	        reg.setCopies("2");
	        reg.setSheets("2");
	       
	        reg.setPrice("0.35");
	        reg.setTotalPrice("100");
	        reg.setCashPay("1");
	        reg.setRegisterTime("2021-02-07 11:03:43");
	        reg.setCashPay("zhifu");
	        regs.add(reg);
	 

	        String fileName =  "c:/hhhh.xlsx";
	        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
	        // 如果这里想使用03 则 传入excelType参数即可
	        EasyExcel.write(fileName, Register.class).sheet("模板").doWrite(regs);
	}

}
