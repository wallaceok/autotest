package com.xn;

import cn.xn.user.domain.*;
import cn.xn.user.service.ILoginService;
import cn.xn.user.service.IPwdService;
import cn.xn.user.service.IRegisterService;
import com.xn.test.util.DBUtil;
import com.xn.test.util.StringUtil;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class oldDataChangePwdTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(oldDataChangePwdTest.class);

    @Resource
    IRegisterService iRegisterService;
    @Resource
    ILoginService iLoginService;
    @Resource
    IPwdService iPwdService;

    @BeforeClass
    public static void beforeClass() {
        DBUtil.newDB();

    }

    @AfterClass
    public static void afterClass() {
        DBUtil.closeDB();

    }

    @Test
    public void changePwdTest() {


        LoginReq loginReq = new LoginReq();
        loginReq.setAppVersion("2.4.0");
        loginReq.setSourceType("android");
        loginReq.setSystemType("QGZ");
        loginReq.setLoginName("15914169844");
        loginReq.setLoginPwd("111111");
        loginReq.setSign(StringUtil.setSign(loginReq));
        logger.warn("loginReq------" + loginReq);
        CommonRlt<LoginRlt> loginRltCommonRlt = iLoginService.doLogin(loginReq);
        logger.warn("登陆" + loginRltCommonRlt.toString());
        String memberNo = loginRltCommonRlt.getData().getMemberNo();
        String tokenId = loginRltCommonRlt.getData().getTokenId();


        ModifyLoginPwdReq modifyLoginPwdReq = new ModifyLoginPwdReq();
        modifyLoginPwdReq.setAppVersion("2.4.0");
        modifyLoginPwdReq.setSourceType("android");
        modifyLoginPwdReq.setSystemType("QGZ");
        modifyLoginPwdReq.setMemberNo(memberNo);
        modifyLoginPwdReq.setLoginPwd("111111");
        modifyLoginPwdReq.setLoginNewPwd("666666");
        modifyLoginPwdReq.setTokenId(tokenId);
        modifyLoginPwdReq.setSign(StringUtil.setSign(modifyLoginPwdReq));
        logger.warn("modify----" + modifyLoginPwdReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt = iPwdService.doModifyLoginPwd(modifyLoginPwdReq);
        logger.warn("修改密码" + emptyObjectCommonRlt.toString());

        LoginOutReq loginOutReq=new LoginOutReq();
        loginOutReq.setAppVersion("2.4.0");
        loginOutReq.setSourceType("android");
        loginOutReq.setSystemType("QGZ");
        loginOutReq.setTokenId(tokenId);
        loginOutReq.setMemberNo(memberNo);
        loginOutReq.setSign(StringUtil.setSign(loginOutReq));
        logger.warn("logout----" + loginOutReq);
        CommonRlt<EmptyObject> emptyObjectCommonRlt1=iLoginService.doLoginOut(loginOutReq);
        logger.warn("登出"+emptyObjectCommonRlt.toString());


    }

}