package com.github.dalianghe;

import com.github.dalianghe.test.UserBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception{

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("activiti-context.xml");

        UserBean userBean = (UserBean) applicationContext.getBean("userBean");
        userBean.hello();

    }
}
