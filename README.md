# chat
1.基本框架，基于mvp框架实现（model、view、presenter）
2.java代码放在在com.example.dell.chat包中
  其中base放一些用于继承的类（活动的基类），bean放javabean（比如储存账户信息的user类），tools放工具类，数据层放在model，传输层放在presenter，视图层放在view（其实就是activity）
3.app.libs放的是需要引进的jar包，其实想引进其他的库只要在app.build.gradle里面打依赖就能自动下载，app.src.main.res下放的是静态文件(页面布局等）
4.每一个实体类采用实现接口的方式，接口中定义好类要实现的方法，这样方便不同层层之间的交互
5.参考博客：http://www.jb51.net/article/104888.htm      https://blog.csdn.net/u010456903/article/details/53439254
