# 同步网易云音乐歌单歌曲到qq音乐

网易云音乐现在因为很多歌曲都没有版权，导致无法播放，已经沦为了各种二手翻唱、音乐评论的社区了，
无奈之下转投qq音乐的怀抱。但是歌单里还有几百首收藏的歌该怎么办呢？
这个小程序的作用就是，利用网易云api将歌单里的所有歌曲同步到qq音乐里来。

使用的网易云音乐api来自开源项目：[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)

自己部署了应用，不会泄露用户名、密码，不放心的可以直接下载源码部署应用。

qq音乐由于没有找到合适的api，采用了无头浏览器的方案，模拟人工操作。

效果预览：

自动收藏中：

<img src="https://github.com/zhangjh/syncNetEaseMusic2qq/blob/master/VID_20210809_221628.gif" width="600" height="300" />

收藏完成：

<img src="https://z3.ax1x.com/2021/08/10/fYqgOI.md.jpg" alt="fYqgOI.jpg" border="0" height="600" width="300" />

## 使用方式
1. 安装chrome
2. [安装chrome selenium驱动chromedriver](https://chromedriver.storage.googleapis.com/index.html)，注意下载的驱动版本要和下载的chrome大版本保持一致
3. 修改配置文件application.properties，填写网易云音乐登录账号，当前项目是手机登录方式
    即使是其他方式，也可以先转为手机号登录
4. 登录qq音乐后，打开控制台，找到任意请求，从request header中复制出cookie字段，修改application.properties，填入qq.cookies中
5. 修改配置文件，填写正确的chromedriver的路径

## 注意事项
1. 当前没有找到qq音乐的api接口，有好的建议可以交流
2. 同步网易云歌单使用到了网易云api，添加qq音乐“我喜欢”使用了无头浏览器，依赖页面的元素css selector，
   由于接口升级、页面升级等，该程序的功能可能会随着升级失效，我不会提供长期维护，因为本身只是为方便自己临时写的小工具，有需要的自行下载源码后二次开发维护

   
