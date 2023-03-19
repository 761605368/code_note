chrome.runtime.onInstalled.addListener(() => {
	// 读取数据，第一个参数是指定要读取的key以及设置默认值
	chrome.storage.sync.get({injectFlag: true}, function (items) {
		setBadgeText(items.injectFlag);
	});
});

chrome.commands.onCommand.addListener(async (command) => {
	if ("removal_restriction" === command) {
		const tab = await getCurrentTab();
		chrome.scripting.executeScript({
			target: { tabId: tab.id },
			func: removalRestriction
		});
	} else if ("on_off" === command) {
		console.log(command);
		chrome.storage.sync.get({injectFlag: true}, function (items) {
			chrome.storage.sync.set({injectFlag: !items.injectFlag}, function () {
				setBadgeText(!items.injectFlag);
			});
		});

	}

});

// chrome.tabs.onCreated.addListener(function(tab) {
// 	chrome.scripting.executeScript({
// 		target: { tabId: tab.id },
// 		func: removalRestriction
// 	});
// });

function setBadgeText(injectFlag) {
	if (injectFlag) {
		chrome.action.setBadgeText({text: 'on'});
	} else {
		chrome.action.setBadgeText({text: 'off'});
	}
}

async function getCurrentTab() {
	const queryOptions = { active: true, currentWindow: true };
	const [tab] = await chrome.tabs.query(queryOptions);
	return tab;
}

function removalRestriction() {
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

