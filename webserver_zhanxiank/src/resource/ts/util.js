"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class utils {
    /**
 *   查询方法，封装了ajax的请求
 * @param url
 * @param method
 * @param data
 * @param result
 */
    query(url, method, data, result) {
        const xhr = new XMLHttpRequest(); //创建对象
        xhr.open(method, url, true); //true异步请求，false同步
        if (method == "post") {
            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //请求头部
        }
        xhr.send(data);
        xhr.onreadystatechange = () => {
            if (xhr.readyState == 4 && xhr.status == 200) {
                let data = xhr.response || xhr.responseText;
                alert(data);
                result(data); //处理数据的函数
            }
        };
    }
}
exports.utils = utils;
