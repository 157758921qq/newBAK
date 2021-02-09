package com.yz.baglk.controller.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yz.baglk.controller.service.RegisterService;
import com.yz.baglk.entity.Register;


public interface RegisterDao extends JpaRepository<Register, Integer>{

	//根据住院号查询
	List<Register> findByPatientId(String string);

	List<Register> findByRegisterTime(String string);

	List<Register> findByRegisterTimeLike(String string);

	List<Register> findByRegisterTimeBetween(String startTime, String endTime);
	
	
	

	//占位符的使用
	//@Query("select acc from Account acc where acc.id= ?1")
	//List<Account> findbyxxx2(int id);
	
	
	////数据库保存的为时间戳<br>select * from 表名 where FROM_UNIXTIME(存时间字段名,'%Y-%m-%d')='2017-12-12'<br><br>
	//数据库保存的为日期格式时间<br>SELECT * FROM 表名 where DATE_FORMAT(存时间字段名,'%Y-%m-%d')='2017-12-11';
	//查询某一天的值
//	@Query("select reg from register reg where date_format(reg.register_time,'%Y-%m-%d')= ?1 ")
//	List<Register> findByRegisterTimeLike(String registerTime);
}























