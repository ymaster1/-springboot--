package com.ym.provider;

import com.ym.provider.commons.utils.beans.BeanConvert;
import com.ym.provider.entity.UserInfo;
import com.ym.provider.entity.request.TimingTaskRequest;
import com.ym.provider.entity.response.TimingTaskResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author ymaster1
 * @date 2020/11/18 14:58
 * @description
 */
@SpringBootTest
@Slf4j
public class ConvertTest {
    @Test
    public void BeanUtilTest() {
        TimingTaskRequest timingTaskRequest = new TimingTaskRequest();
        TimingTaskResponse timingTaskResponse = new TimingTaskResponse();
        UserInfo info = new UserInfo();
        info.setCity("dazhou");
        timingTaskRequest.setMethodName("test");
        timingTaskRequest.setServiceName("service");
        timingTaskRequest.setTaskStatus(true);
        timingTaskRequest.setSum(1);
        timingTaskRequest.setInfo(info);
        BeanUtils.copyProperties(timingTaskRequest, timingTaskResponse);
//        timingTaskResponse = BeanConvert.convert(TimingTaskResponse.class, timingTaskRequest);
        timingTaskResponse.getInfo().setCity("chengdu");

        System.out.println(timingTaskResponse.getMethodName() == timingTaskRequest.getMethodName());
        System.out.println(timingTaskResponse.getServiceName() == timingTaskRequest.getServiceName());
        System.out.println(timingTaskResponse.getTaskStatus() == timingTaskRequest.getTaskStatus());
        System.out.println(timingTaskResponse.getSum() == timingTaskRequest.getSum());
        System.out.println(timingTaskResponse.getInfo() == timingTaskRequest.getInfo());
        System.out.println(timingTaskRequest.getInfo().getCity());
    }
}
