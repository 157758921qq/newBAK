package com.yz.baglk.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yz.baglk.controller.service.RegisterService;
import com.yz.baglk.entity.Register;
import com.yz.baglk.wxpay.util.DataJoinUtils;



@Controller
public class MainController {
	
	

	@Autowired
	RegisterService regSrv;
	

	

	@RequestMapping("/dq")
	public String index() {
		return "index";
	}
	


	@RequestMapping("/model")
	public String model() {
		return "testmodel";
	}
	
	@RequestMapping("/works")
	public String works() {
		return "works";
	}
	

	
//	@RequestMapping("/list/{time}")
//	public String list(@PathVariable String time) {
//		List<Register> regs = regSrv.findByRegisterTime(time);
//		System.out.println(ToStringBuilder.reflectionToString(regs, ToStringStyle.MULTI_LINE_STYLE));
//		return "list";
//	}
	
	@RequestMapping("/listAll")
	public String listAll(Model model) {
		List<Register> regs = regSrv.findAll();
		System.out.println(ToStringBuilder.reflectionToString(regs, ToStringStyle.MULTI_LINE_STYLE));
		model.addAttribute("page", regs);
		return "listoneday";
	}
	
	//根据住院号查询
//	@RequestMapping("/listOneDay")
//	public String listOneDay(Model model) {
//		List<Register> regs = regSrv.findByPatientId("1");
//		
//		System.out.println(ToStringBuilder.reflectionToString(regs, ToStringStyle.MULTI_LINE_STYLE));
//		model.addAttribute("page", regs);
//		return "listall";
//	}
	
	  @RequestMapping({"/listToday"})
	  public String listToday(Model model)
	  {
	    double sum = 0.0D;

	    Date date = new Date();

	    String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
	    List<Register> regs = this.regSrv.findByRegisterTimeLike("%" + today + "%");

	    java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
	    for (Register r : regs) {
	      String[] split = r.getTotalPrice().split("元");
	      for (int i = 0; i < split.length; i++)
	      {
	        sum += Double.parseDouble(split[i]);
	      }

	    }
	    String sumStr = df.format(sum);
	    System.out.println("sum =" + sumStr);

	    System.out.println(ToStringBuilder.reflectionToString(regs, ToStringStyle.MULTI_LINE_STYLE));
	    model.addAttribute("page", regs);
	    model.addAttribute("TotalPrice", sumStr);
	    return "listtoday";
	  }
	
	
	
	@RequestMapping("/listMonth")
	public String listOneDay(Model model) {
		Date date = new Date();//获得系统时间.
		//将时间格式转换成符合Timestamp要求的格式.
		String month = new SimpleDateFormat("yyyy-MM").format(date);
		
		List<Register> regs = regSrv.findByRegisterTimeLike("%"+month+"%");
		
		System.out.println(ToStringBuilder.reflectionToString(regs, ToStringStyle.MULTI_LINE_STYLE));
		model.addAttribute("page", regs);
		return "listmonth";
	}
	
	
	@RequestMapping("/listScope")
	public String listScope(Model model) {
		return "listscope";
	}
	

	  @RequestMapping({"/listDoScope"})
	  public String doScope(@DateTimeFormat(pattern="yyyy-MM-dd") Date startTime, @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime, Model model)
	  {
	    System.out.println("startTime = " + startTime);
	    System.out.println("endTime = " + endTime);
	    if ((startTime == null) || (endTime == null)) {
	      return "error1";
	    }

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(endTime);
	    calendar.add(6, 1);
	    Date dt1 = calendar.getTime();

	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String startTimeStr = df.format(startTime);
	    String endTimeStr = df.format(dt1);

	    System.out.println("startTime=" + startTimeStr + ", endTiem= " + endTimeStr);

	    List regs = this.regSrv.findByRegisterTimeBetween(startTimeStr, endTimeStr);
	    System.out.println(ToStringBuilder.reflectionToString(regs, ToStringStyle.MULTI_LINE_STYLE));
	    model.addAttribute("page", regs);
	    return "listall";
	  }
	

	//收集表单元素
	@PostMapping("/collect")
	@ResponseBody
	public String collect(HttpServletRequest req, Model map) {
	
		//System.out.println(ToStringBuilder.reflectionToString(register, ToStringStyle.MULTI_LINE_STYLE));
		
//		Date date = new Date();
//
//		Timestamp timeStamp = new Timestamp(date.getTime());
//		register.setRegisterTime(timeStamp);
		String patientId = req.getParameter("patientId");
		String patientName = req.getParameter("patientName");
		String reason = req.getParameter("reason");
		String copies = req.getParameter("copies");
		String sheets = req.getParameter("sheets");
		String price = req.getParameter("price");
		String totalPrice = req.getParameter("totalPrice");
		String pay = req.getParameter("pay");
		if(pay.equals("1")) {
			pay = "现金支付";
		} else if(pay.equals("0")) {
			pay = "扫码支付";
		}
		
		Register register = new Register();
		register.setPatientId(patientId);
		register.setPatientName(patientName);
		register.setReason(reason);
		register.setCopies(copies);
		register.setSheets(sheets);
		register.setPrice(price);
		register.setTotalPrice(totalPrice);
		register.setCashPay(pay);
		
		Date date = new Date();//获得系统时间.
		//将时间格式转换成符合Timestamp要求的格式.
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		register.setRegisterTime(nowTime);

		System.out.println(ToStringBuilder.reflectionToString(register, ToStringStyle.MULTI_LINE_STYLE));
		Register reg = regSrv.save(register);
		
		if(reg != null) {
			System.out.println("SUCCESS");
			return "SUCCESS";
		} else {
			System.out.println("ERROR");
			return "ERROR";
		}
}
	
	
	//收集表单元素
		@PostMapping("/collectWorkers")
		@ResponseBody
		public String collectWorkers(HttpServletRequest req, Model map) {
		
			//System.out.println(ToStringBuilder.reflectionToString(register, ToStringStyle.MULTI_LINE_STYLE));
			
//			Date date = new Date();
	//
//			Timestamp timeStamp = new Timestamp(date.getTime());
//			register.setRegisterTime(timeStamp);
			String patientId = req.getParameter("patientId");
			String patientName = req.getParameter("patientName");
			String reason = req.getParameter("reason");
			String copies = req.getParameter("copies");
			String sheets = req.getParameter("sheets");
			String price = req.getParameter("price");
			String totalPrice = req.getParameter("totalPrice");
			String pay = req.getParameter("pay");
			if(pay.equals("2")) {
				pay = "本院职工复印";
			}
			
			Register register = new Register();
			register.setPatientId(patientId);
			register.setPatientName(patientName);
			register.setReason(reason);
			register.setCopies(copies);
			register.setSheets(sheets);
			register.setPrice(price);
			register.setTotalPrice(totalPrice);
			System.out.println("pay = " + pay);
			register.setCashPay(pay);
			
			Date date = new Date();//获得系统时间.
			//将时间格式转换成符合Timestamp要求的格式.
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			register.setRegisterTime(nowTime);

			System.out.println(ToStringBuilder.reflectionToString(register, ToStringStyle.MULTI_LINE_STYLE));
			Register reg = regSrv.save(register);
			System.out.println("Register = " + reg);
			if(reg != null) {
				System.out.println("SUCCESS");
				return "SUCCESS";
			} else {
				System.out.println("ERROR");
				return "ERROR";
			}
	}
		
	

	/**
	 * 	点击 前端html页面上的提交按钮
	 * 	接收到前端传递过来的参数
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/orderSubmit")
	public String orderSubmit(HttpServletRequest req, HttpServletResponse resp, Model map) {
		try {
			// 订单标号
			String orderNo = req.getParameter("orderNo");
			// 订单描述
			String body = req.getParameter("remark");
			// 订单价格
			String price = req.getParameter("price");

			// 数据保存到数据库
			
			
			DataJoinUtils wxPayUtils = new DataJoinUtils();
			// 回调地址
			String url = "http://www.jd.com";
			Map<String, String> result = null;
			result = wxPayUtils.wxPay(url, orderNo, price, "127.0.0.1", body);
			System.out.println("微信统一下单，获取的微信返回结果 = " + result);
	
			//req.setAttribute("code_url", result.get("code_url"));
			//req.getRequestDispatcher("/orderPay.html").forward(req, resp);
			map.addAttribute("code_url", result.get("code_url"));
			map.addAttribute("return_msg", result.get("return_msg"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "testpay2";
	}
	
	
	@ResponseBody
	@RequestMapping("/queryPayStatus")
	public String queryStatus(String orderNo) {
		String tip = "";
		
		try {
			int num = 0;
			DataJoinUtils dataJoinUtils = new DataJoinUtils();
			while(true) {
//				Map<String, String> map = dataJoinUtils.wxOrderQuery(orderNo);
//				if(map == null) {
//					tip = "支付发送错误";
//					break;
//				}
//				
//				if(map.get("trade_state").equals("SUCCESS")) {
//					tip = "OK";
//					
//					//修改数据库中的订单状态
//					break;
//				}
				
				Thread.sleep(3000);
				num++;
				if(num >= 10) {
					tip = "支付超时";
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tip;
	}
	
	
	
	
	@RequestMapping("/orderQuery")
	public String orderquery(HttpServletRequest req, HttpServletResponse resp, Model map) {
		String orderNo = req.getParameter("orderNo");
		System.out.println("orderNo= " + orderNo);
		DataJoinUtils wxPayUtils = new DataJoinUtils();
		Map<String, String> resultSet = null;
		try {
			resultSet = wxPayUtils.wxOrderQuery(orderNo);
			System.out.println("===================微信订单详情=====================");
			System.out.println("resultSet:"+resultSet.toString());
			map.addAttribute("resultSet", resultSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "orderquery";
	}
	
	
	@RequestMapping("/orderClose")
	public String orderClose(HttpServletRequest req, HttpServletResponse resp, Model map) {
		String orderNo = req.getParameter("orderNo");
		System.out.println("orderNo= " + orderNo);
		DataJoinUtils wxPayUtils = new DataJoinUtils();
		Map<String, String> resultSet = null;
		try {
			resultSet = wxPayUtils.wxOrderClose(orderNo);
			System.out.println("===================微信订单详情=====================");
			System.out.println("resultSet:"+resultSet.toString());
			map.addAttribute("resultSet", resultSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "orderclose";
	}
	
	 
	
	
	@RequestMapping("/excel")
	//@ResponseBody
	public String excel(Model model) {
		  List<Register> registerLists = new ArrayList<>();
		  Date date = new Date();
		  String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
		  List<Register> regs = this.regSrv.findByRegisterTimeLike("%" + today + "%");

		  for (Register r : regs) {
			  //System.out.println(r.getCashPay());
			  registerLists.add(r);
		  }

		  //<a th:href="@{/download1}" type="button" class="btn btn-primary">2、先生成Excel表格，再下载到C盘</a></td>

		  String fileName =  today+"dqbak.xlsx";
		  System.out.println("excel fileName = " + fileName);
		  String fileNamePath = "c:\\"+ fileName;
	      // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
	      // 如果这里想使用03 则 传入excelType参数即可
	      EasyExcel.write(fileNamePath, Register.class).sheet("模板").doWrite(registerLists);
	      //return "redirect:/download1?filename"+ fileName;
	      return "forward:/download1?filename="+fileName;
	}
	
	
	
	/**
	 * 调试发现ServletFileUpload对象为空， 在Spring Boot中有默认的文件上传组件，
	 * 在使用ServletFileUpload时需要关闭Spring Boot的默认配置 ，
	 * 
	 * @param request
	 */
	@RequestMapping("/UpLoadImg")
	@ResponseBody
	public void uploadimg(HttpServletRequest request) {
		System.out.println("文件上传后端开始");
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				// 文件的限制条件在parseRequest之前
				// 设置上传文件用到的临时文件
				factory.setSizeThreshold(10240); // 临时目录文件大小
				factory.setRepository(new File("D:\\imgtemp")); // 临时文件目录
				// 控制上传文件的大小20KB
				upload.setSizeMax(20480);

				List<FileItem> items = upload.parseRequest(request);
				System.out.println(items.size());
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();
					String itemName = item.getFieldName(); // 获取的是普通表单文件

					if (item.isFormField()) { // 如果是图片表单字段
						if (itemName.equals("sno")) {

						} else if (itemName.equals("sname")) {

						} else {

						}

					} else { // 上传的是文件
						// String path =
						// request.getSession().getServletContext().getContextPath("upload");
						String path = "D:\\img";
						String fileName = item.getName(); // 获取的是上传文件名

						String suffix = fileName.substring(fileName.indexOf(".") + 1);
						System.out.println("suffix = " + suffix);

						if (!(suffix.equals("png") || suffix.equals("jpg") || suffix.equals("gif")
								|| suffix.equals("doc") || suffix.equals("ppt"))) {
							System.out.println("您上传的是非法文件");
							return; // 终止
						}

						File file = new File(path, fileName);
						try {
							item.write(file); // 将item传给指定的地址
							System.out.println("文件上传成功");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (FileUploadBase.SizeLimitExceededException e) {
			System.out.println("上传文件大小超过20KB");
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	// 文件下载
		// <a href="ImgLoadDown?filename=12.jgp">12.jpg图片下载</a>
		@RequestMapping("/download1")
		@ResponseBody
		public void download(HttpServletRequest request, HttpServletResponse response) {
			String fileName = request.getParameter("filename");
			System.out.println("fileName = " + fileName);

			response.addHeader("content-Type", "application/octet-stream");
			response.addHeader("content-Disposition", "attachment; filename=" + fileName);

			byte[] buff = new byte[1024];
			BufferedInputStream bis = null;
			OutputStream os = null;

			try {
				os = response.getOutputStream();
				bis = new BufferedInputStream(new FileInputStream(new File("C:/" + fileName)));

				int i = bis.read(buff);

				while (i != -1) {
					os.write(buff, 0, buff.length);
					os.flush();
					i = bis.read(buff);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	
	
	

}
























