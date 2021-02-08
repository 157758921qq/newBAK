package com.yz.baglk.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;



@Entity
@Table(name = "register")
public class Register {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@ExcelProperty("序号")
	private int id;
	
	@ExcelProperty("住院号")
	private String patientId;
	
	@ExcelProperty("姓名")
	private String patientName;
	
	@ExcelProperty("复印原因")
	private String reason;
	
	@ExcelProperty("复印份数")
	private String copies;
	
	@ExcelProperty("复印张数")
	private String sheets;
	
	@ExcelProperty("单价")
	private String price;
	
	@ExcelProperty("总价")
	private String totalPrice;
	

	@ExcelProperty("支付方式")
	private String cashPay;
	
	public String getCashPay() {
		return cashPay;
	}
	public void setCashPay(String cashPay) {
		this.cashPay = cashPay;
	}




	@ExcelProperty("打印时间")
	private String registerTime;
	
	


	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	public String getCopies() {
		return copies;
	}
	public void setCopies(String copies) {
		this.copies = copies;
	}
	public String getSheets() {
		return sheets;
	}
	public void setSheets(String sheets) {
		this.sheets = sheets;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
	
	
	public Register() {
	}
	
	
	
}
