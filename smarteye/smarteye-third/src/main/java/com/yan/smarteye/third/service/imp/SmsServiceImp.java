package com.yan.smarteye.third.service.imp;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.yan.common.utils.R;
import com.yan.smarteye.third.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;
import java.util.UUID;

//阿里云短信服务
//基本上是复制阿里云写好的，而accessKeyId、accessKeySecret是写死在代码的
//我在此基础上改进，让accessKeyId、accessKeySecret成为可配置
//将静态方法都改为了非静态的，
//因为static作为一个静态方法，在加载类的时候就被加载到内存中，不管你用不用都占用这个位置，这种设计是不推荐的。
//将阿里云连接注入IOC容器，避免每次调用就会产生一个与阿里云短信服务新的连接，造成大量的连接损耗资源
//注入对象到IOC，通过对象调方法，每次都是这以恶个对象，就只保持一个阿里云短信服务连接即可。
//并且将原本的主方法调用改写为了send方法，并增加了phone、code参数
@Slf4j
@Service
public class SmsServiceImp implements SmsService {
    @Autowired
    com.aliyun.dysmsapi20170525.Client client;
    //发送短信
    public void send(String phone, String code) throws Exception {
        //com.aliyun.dysmsapi20170525.Client client = createClient(accessKeyId,accessKeySecret); 改为IOC注入
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")         //修改参数需向阿里云备案
                .setTemplateCode("SMS_154950909")   //修改参数需向阿里云备案
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\""+code+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            //System.out.println(sendSmsResponse.body.message);  //有一些问题需要在这里查看，比如模板格式不对就需要在此，下面catch的error只是一些大问题
        } catch (TeaException error) {
            // 如有需要，请打印 error
            log.error(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            log.error(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

    /**
     * 发送短信内容，预警库存
     */
    @Override
    public void sendWarnInfo(String phone, String msg) {
        //TODO 暂未申请阿里云通知短信模板，暂时用发送验证码作为测试
        Random random = new Random();
        String code = String.valueOf(random.nextInt(100000));
        try {
            send(phone,code);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
