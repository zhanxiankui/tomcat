var tree = /** @class */ (function () {
    function tree(id, leave) {
        this.id = id;
        this.leave = leave;
    }
    tree.prototype.init = function (url, way, path) {
        var _this = this;
        this.query(url, way, path, function (data) {
            _this.addRootItem(data);
        });
    };
    tree.prototype.getDom = function () {
        return document.getElementById("left");
    };
    /**
    *   查询方法，封装了ajax的请求
    * @param url
    * @param method
    * @param data
    * @param result
    */
    tree.prototype.query = function (url, method, data, result) {
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
    tree.prototype.addRootItem = function (data) {
        if (data == null) {
            return;
        }
        // 添加标题
        var itemdom = document.createElement("div");
        var arrow = '<span class="to__dropdownList" ><i onclick="dropClick(this)"><svg t="1550632829702" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1783" xmlns:xlink="http://www.w3.org/1999/xlink" width="100%" height="100%"><defs><style type="text/css"></style></defs><path d="M959.52557 254.29773 511.674589 702.334953 63.824631 254.29773Z" p-id="1784"></path></svg></i></span>';
        var json = JSON.parse(data);
        for (var i = 0; i < data.length; i++) {
            var j = JSON.parse(json[i]);
            var name_1 = j.name;
            var isdir = j.isdir;
            var leave = j.leave;
            itemdom.innerHTML = '<div class="to__item level-' + i + '">' + arrow + '<span onclick="domClick(this)"><input type="checkbox" name="cName" value="' + name_1 + '" onclick="checkboxClick(this)" /><div class="to__name">' + name_1 + '</div></span></div>';
            this.getDom().appendChild(itemdom);
        }
        // let item=new Item();
    };
    return tree;
}());
var Item = /** @class */ (function () {
    function Item(parentid, id, name, isdir, leave) {
        this.isExpend = false;
        this.parentid = parentid;
        this.id = id;
        this.name = name;
        this.isdir = isdir;
        this.leave = leave;
    }
    return Item;
}());
