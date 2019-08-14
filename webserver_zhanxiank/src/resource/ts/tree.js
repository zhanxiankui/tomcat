var tree = /** @class */ (function () {
    function tree(id, leave) {
        this.id = id;
        this.leave = leave;
    }
    tree.prototype.init = function (url, way, path) {
        var _this = this;
        this.query(url, way, path, function (data) {
            _this.addRootItem(data, _this.getDom());
            _this.domClick();
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
                // alert(data);
                result(data_1); //处理数据的函数
            }
        };
    };
    //第一层结点。
    tree.prototype.addRootItem = function (data, dom) {
        var item = new Item("-1", "0", "root", false, 0);
        item.addTreeItem("-1", "0", data, this.getDom());
    };
    tree.prototype.domClick = function () {
        var _this = this;
        // 选中子元素所有input框
        var dom = document.getElementsByTagName("span");
        for (var i = 0; i < dom.length; i++) {
            var self = this;
            dom[i].onclick = function (e) {
                var subItem = e.path;
                var obj = self;
                if (subItem == null) {
                    return;
                }
                var name = subItem[0].neme;
                var value = subItem[0].value; //选择输入框
                var txt = subItem[0].innerHTML; //点击文件名
                var className = subItem[2].className; //展开和伸缩。
                if (className == "to__dropdownList") {
                    _this.dropClick(e);
                }
                if (txt != null || txt != "") {
                    //查看文件
                }
            };
        }
    };
    tree.prototype.dropClick = function (e) {
        var id = e.path[3].id;
        var dom = document.getElementById(id); //li的dom;
        this.query("/show.do?path=" + e.path[2].title, "get", null, function (data) {
            var item = new Item(id, "", "", true, 0);
            item.addTreeItem(id, "", data, dom);
        });
        // 切换样式状态
        if (dom.className.indexOf("to_roate") > -1) {
            dom.className = ""; //改变样式
            e.path[0].src = "close.gif"; //改变图标
        }
        else {
            dom.className = "to_roate";
            e.path[0].src = "expend.gif";
        }
        var domShow = dom;
        if (domShow.className.indexOf("to_show") > -1) {
            dom.style.display = "none"; //隐藏东西
        }
        else {
            dom.style.display = "black"; //显示。
        }
    };
    return tree;
}());
var Item = /** @class */ (function () {
    function Item(parentid, id, name, isdir, leave) {
        this.path = "d:/";
        this.isExpend = false;
        this.parentid = parentid;
        this.id = id;
        this.name = name;
        this.isdir = isdir;
        this.leave = leave;
    }
    Item.prototype.setPath = function (path) {
        this.path = path;
    };
    Item.prototype.addTreeItem = function (pid, id, data, dom) {
        if (data == null) {
            return;
        }
        var t = new tree(null, 0); //使用对象，
        var arry = new Array();
        var json = JSON.parse(data);
        var ulmdom = document.createElement("ul");
        ulmdom.id = pid;
        var num = (JSON.parse(json[0]).leave) * 9;
        ulmdom.style.cssText = "margin-left:" + num + "px;";
        var ulhtml = '';
        for (var i = 0; i < json.length; i++) {
            var j = JSON.parse(json[i]);
            var name_1 = j.name;
            var isdir = j.isdir == "true";
            var leave = Number(j.leave);
            var path = j.path;
            var item = new Item(pid, id, name_1, isdir, leave);
            this.setPath(path); //路径
            arry.push(item);
            var sp1 = '<span class="to__dropdownList"  title=' + path + '> <i onclick="dropClick(this)">  <img src="expend.gif" class="icon"></i></span>';
            var sp2 = '<span ><input type="checkbox" name="input" value="' + name_1 + '" onclick="checkboxClick(this)" /> <div class="to_name">' + name_1 + '</div></span>';
            var temp = '<li id="' + leave + name_1 + '">' + sp1 + sp2 + '</li>';
            ulhtml += temp;
        }
        ulmdom.innerHTML = ulhtml;
        dom.appendChild(ulmdom);
        t.domClick(); //添加点击事件。
        return arry;
    };
    return Item;
}());
