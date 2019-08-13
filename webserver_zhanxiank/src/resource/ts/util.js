"use strict";
exports.__esModule = true;
var utils = /** @class */ (function () {
    function utils() {
    }
    /**
 *   查询方法，封装了ajax的请求
 * @param url
 * @param method
 * @param data
 * @param result
 */
    utils.prototype.query = function (url, method, data, result) {
        var xhr = new XMLHttpRequest(); //创建对象
        xhr.open(method, url, true); //true异步请求，false同步
        if (method == "post") {
            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //请求头部
        }
        xhr.send(data);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var data_1 = xhr.response || xhr.responseText;
                alert(data_1);
                result(data_1); //处理数据的函数
            }
        };
    };
    return utils;
}());
exports.utils = utils;
