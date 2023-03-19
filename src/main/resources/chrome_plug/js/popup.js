document.addEventListener('DOMContentLoaded', function () {
    var defaultConfig = {injectFlag: true}; // 默认配置
    // 读取数据，第一个参数是指定要读取的key以及设置默认值
    chrome.storage.sync.get(defaultConfig, function (items) {
        document.getElementById('onoffswitch').checked = items.injectFlag;
        setBadgeText(items.injectFlag);
    });
});

document.getElementById('onoffswitch').addEventListener('click', function () {
    var injectFlag = document.getElementById('onoffswitch').checked;
    chrome.storage.sync.set({injectFlag: injectFlag}, function () {
    });
    setBadgeText(injectFlag);
});

function setBadgeText(injectFlag) {
    if (injectFlag) {
        chrome.action.setBadgeText({text: 'on'});
    } else {
        chrome.action.setBadgeText({text: 'off'});
    }
}