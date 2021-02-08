package com.yz.baglk.controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.baglk.controller.dao.RegisterDao;
import com.yz.baglk.entity.Register;

@Service
public class RegisterService {
	
	@Autowired
	RegisterDao regDao;

	//记录保存到数据库
	public Register save(Register register) {
		return regDao.save(register);
	}

	//查找所有记录
	public List<Register> findAll() {
		// TODO Auto-generated method stub
		return regDao.findAll();
	}

	public List<Register> findByPatientId(String string) {
		// TODO Auto-generated method stub
		return regDao.findByPatientId(string);
	}

	public List<Register> findByRegisterTime(String string) {
		// TODO Auto-generated method stub
		return regDao.findByRegisterTime(string);
	}

	public List<Register> findByRegisterTimeLike(String string) {
		// TODO Auto-generated method stub
		return regDao.findByRegisterTimeLike(string);
	}

	public List<Register> findByRegisterTimeBetween(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return regDao.findByRegisterTimeBetween(startTime, endTime) ;
	}


	

}
