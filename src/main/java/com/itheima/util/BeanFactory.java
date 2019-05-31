package com.itheima.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class BeanFactory {
    /**
     * 根据给定的key，得到配置文件中配置的实现类对象
     * @param key
     * @return
     */
    public static Object getBean(String key){
        Object object = null;
        try {
            //1.读取配置文件
            ResourceBundle bundle = ResourceBundle.getBundle("beans");
            //2.得到想要的bean的类名
            String className = bundle.getString(key);
            //3.反射生成类的实例对象
            Class clazz = Class.forName(className);
            Object target = clazz.newInstance();

            //生成target的代理对象
            object = Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //增强：输出日志
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            System.out.println(format.format(new Date()) + "执行了：" +className+"."+ method.getName()+"()....");


                            //调用目标对象
                            Object result = method.invoke(target, args);

                            //增强：

                            return result;
                        }
                    }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
