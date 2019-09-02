针对本目录的示例工程说明：

1.androidAppSsl

1.1	安卓示例工程（在原Android双向SSL通讯工程的基础上添加的TLV数据解析，8583报文解析与logback日志记录功能）

1.2	logback默认读取工程asset目录下的logback.xml配置文件来初始化日志记录功能。

1.3	CSDN中收藏了文章，对安卓下的logback日志记录使用有说明，网址：http://blog.csdn.net/angcyo/article/details/51405301

2.Java_Test

2.1	Java示例工程，测试TLV数据解析，8583报文解析与logback日志记录功能

2.2	logback默认读取工程根目录下的logback.xml配置文件来初始化日志记录功能。

2.3	当jar包中存在logback.xml并且工程中也存在logback.xml文件的时候，最终工程中的logback.xml生效，jar包中的不生效