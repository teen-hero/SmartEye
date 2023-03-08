package com.yan.smarteye.third.controller;

import com.yan.common.exception.BizCodeEnume;
import com.yan.common.utils.R;
import com.yan.smarteye.third.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
	阿里云短信服务
 */
@Controller
@RequestMapping("/sms")
public class SmsSendController {

	@Autowired
	SmsService smsService;

	/**
	 * 提供给别的服务进行调用的
	 * 发送验证码
	 */
	@GetMapping("/sendcode")
	@ResponseBody
	public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code){
		//向阿里云短信服务发送短信信息，再由阿里云发送短信给用户
		try {
			smsService.send(phone, code);
			return R.ok();
		} catch (Exception exception) {
			exception.printStackTrace();
			return R.error(BizCodeEnume.SMS_SEND_CODE_EXCEPTION.getCode(), BizCodeEnume.SMS_SEND_CODE_EXCEPTION.getMsg());
		}
	}
	/**
	 * 发送短信内容，预0警库存
	 */
	@GetMapping("/sendwarninfo")
	@ResponseBody
	public R sendWarnInfo(@RequestParam("phones") List<String> phones, @RequestParam("msg") String msg){
		//依次向集合中的号码发送短信
		List<String> unSends = new ArrayList<>();
		for(String phone:phones) {
			//向阿里云短信服务发送短信信息，再由阿里云发送短信给用户
			try {
				smsService.sendWarnInfo(phone, msg);
				return R.ok();
			} catch (Exception exception) {
				exception.printStackTrace();
				//记录未发送成功的手机号
				unSends.add(phone);
			}
		}
		if(unSends.size()==0){
			return R.ok();
		}else {
			return R.error(100111,"部分号码发送失败").put("data",unSends);
		}
	}
}
