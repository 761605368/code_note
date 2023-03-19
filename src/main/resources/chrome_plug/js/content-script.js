console.log('这是content script!');
var defaultConfig = {injectFlag: true}; // 默认配置
// 读取数据，第一个参数是指定要读取的key以及设置默认值
chrome.storage.sync.get(defaultConfig, function (items) {
    if (items.injectFlag) {
        console.log('执行CSDN解除脚本。。。');
        // 复制
        document.body.contentEditable = 'true';
        document.designMode = 'on';

        // 阅读全文
        var article_content = document.getElementById("article_content");
        article_content.removeAttribute("style");

        var follow_text = document.getElementsByClassName('follow-text')[0];
        follow_text.parentElement.parentElement.removeChild(follow_text.parentElement);

        var hide_article_box = document.getElementsByClassName(' hide-article-box')[0];
        hide_article_box.parentElement.removeChild(hide_article_box);

        // 阅读全部
        $("div.article_content").removeAttr("style");
        $("#btn-readmore").parent().remove();

        console.log("执行CSDN解除脚本完毕");
    }
});

