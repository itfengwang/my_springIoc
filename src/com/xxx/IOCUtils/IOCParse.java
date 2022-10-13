package com.xxx.IOCUtils;

/*spring底层的解析以及注入的过程*/

import com.xxx.annotation.Autowired;
import com.xxx.annotation.Controller;
import com.xxx.annotation.Repository;
import com.xxx.annotation.Service;
import com.xxx.factory.BeanFactory;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IOCParse {

    private static List<String> classesList = new ArrayList();

    public static void beginParseIOC(String pathName){

        pathName = pathName.replace(".","/");
        ///D:/springCloud/my_springIoc/out/production/my_springIoc/com/xxx
        //该路径 是需要扫描的包
        String path = ClassLoader.getSystemResource("").getPath()+pathName;

        //调用管理文件的方法//将符合要求的全类名装进我们的list中
        addFiles(path);
        //遍历我们的classesList 根据全类名创建实例,并添加到我们的实例中
        for (String aClass : classesList) {

            try{
                //根据全类名创建字节码对象
                Class classObj = Class.forName(aClass);
                //检测类上是否有自定义的注解
                Controller controller = (Controller)classObj.getAnnotation(Controller.class);
                Repository repository = (Repository)classObj.getAnnotation(Repository.class);
                Service service = (Service)classObj.getAnnotation(Service.class);

                if (controller != null || repository!=null||service!=null){
                    //其中三个注解有一个不为空，就将对应的实例添加到容器中
                    //通过反射创建对应的实例
                    Object instance = classObj.newInstance();
                    //获取类的类名当作唯一标识
                    String simpleName = classObj.getSimpleName();
                    //将对应的实例添加到我们的容器中
                    BeanFactory.getBeanMap().put(simpleName,instance);
                }


            }catch (Exception e){

                e.printStackTrace();
            }

        }

        //第二次遍历，完成实例的注入
        for (String aClass : classesList) {

            try{
                //根据全类名创建字节码对象
                Class classObj = Class.forName(aClass);
                //检测类上是否有自定义的注解
                Controller controller = (Controller)classObj.getAnnotation(Controller.class);
                Repository repository = (Repository)classObj.getAnnotation(Repository.class);
                Service service = (Service)classObj.getAnnotation(Service.class);

                if (controller != null || repository!=null||service!=null){

                    //获取有以上注解的类的所有属性
                    Field[] declaredFields = classObj.getDeclaredFields();
                    //遍历所有的属性
                    for (Field declaredField : declaredFields) {

                        //判断属性上是否有Autowired注解
                        Autowired autowired = declaredField.getAnnotation(Autowired.class);
                        //如果属性上存在这个注解，完成对属性的注入
                        if (autowired != null){
                            //打破封装
                            declaredField.setAccessible(true);

                            //遍历容器，从容器中获取对应的实例
                            for (Map.Entry<String, Object> stringObjectEntry : BeanFactory.getBeanMap().entrySet()) {

                                //获取每个容器实例所实现的接口
                                Class<?>[] entryInterfaces = stringObjectEntry.getValue().getClass().getInterfaces();
                                //如果实现了接口，比较接口的类型
                                if (entryInterfaces.length != 0){

                                    for (Class entryInterface : entryInterfaces) {

                                        if (entryInterface == declaredField.getType()){
                                            //如果咋们遍历到的属性类型（接口类型）和咋们所实现的接口类型一致的话
                                            //咋们这个属性就是接口哦  我们从实例工厂中去取出对应的实例，为我们这个属性变量赋值
                                            declaredField.set(BeanFactory.getBean(classObj.getSimpleName()),stringObjectEntry.getValue());


                                        }
                                    }
                                }
                                //获取当前实例的属性
                                //如果属性的类型和我们容器中的实例的类型相同，进行实例的注入
                               /* if (declaredField.getType() == stringObjectEntry.getValue().getClass()){
                                    //注入实例的过程
                                    declaredField.set(BeanFactory.getBean(classObj.getSimpleName()),
                                            stringObjectEntry.getValue());

                                }*/

                            }
                        }
                    }
                }

            }catch (Exception e){

                e.printStackTrace();
            }

        }


    }



    /**
     * 管理文件的方法
     * 它里面可能是文件夹嵌套文件夹，或者普通文本，或者是java类，jar包等等...
     * 我们需要一步一步去过滤出我们需要的类(后缀是以.class结尾的输出)
     */
    public static void addFiles(String path){
        //创建一个File对象
        File file = new File(path);
        //拿到所有符合要求的文件 后缀是.class的文件
        //FileFilter 是一个过滤文件的接口
        File[] files = file.listFiles(new FileFilter() {
            @Override
            //比较规则
            public boolean accept(File f) {

                //判断当前的文件是否是文件夹,如果是文件夹的话，继续去过滤
                if (f.isDirectory()){
                    //继续调用addFiles方法
                    addFiles(f.getPath());
                }
                return f.getPath().endsWith(".class"); //只要返回的是true，就会将文件添加到文件数组里

            }
        });

        //我们现在已经拿到了符合要求的文件
        //遍历文件数组
        for (File fileName : files) {

            //将符合要求的文件切割成全类名的形式
            String fileNamePath = fileName.getPath();
            //com\xxx\annotation\Autowired.class
            fileNamePath = fileNamePath.substring(fileNamePath.lastIndexOf("com"),fileNamePath.length());
            //将\替换成.
            //com.xxx.annotation.Autowired.class
            fileNamePath = fileNamePath.replace("\\",".");
            //将.class 删除
            //com.xxx.service.UserService
            fileNamePath = fileNamePath.replace(".class","");
            //将切割好的全类名添加到容器中
            classesList.add(fileNamePath);

            //System.out.println(fileNamePath);

        }



    }



}
