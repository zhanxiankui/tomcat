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
            _this.addButtonOnclick("but", "/edit.do"); //编辑
            _this.addButtonOnclick("but0", "/save.do"); //保存
            _this.addButtonOnclick("but1", "/download.do"); //下载
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
                var txt = e.target.innerHTML; //点击文件名
                // let className = subItem[2].className;  //展开和伸缩。
                var className = e.target.parentNode.className;
                if (className == "to__dropdownList") { //文件夹才会展开。
                    _this.dropClick(e);
                }
                if (txt != null && txt != "") { //查看文件，点击文件名字。
                    _this.seeFile(e);
                    var file = e.target.title; //路径
                    self.setbuttonName(file, "but"); //编辑button
                    self.setbuttonName(file, "but0"); //保存button
                    self.setbuttonName(file, "but1"); //下载button
                }
            };
        }
    };
    tree.prototype.removeDom = function (id) {
        var key = document.getElementById(id);
        if (key) //清楚本身存在的dom结构。
         {
            var parentNode = key.parentNode;
            parentNode.removeChild(key);
        }
    };
    tree.prototype.seeFile = function (e) {
        // this.query("/watchFile.do?path="+e.path[2].title,"get",null,function(data){ 
        var dom = document.getElementById("right");
        var file = e.target.title;
        var index = file.indexOf(".");
        if (index == -1) {
            return; //不查看
        }
        var type = file.substring(index + 1).trim();
        var arrimg = ["gif", "bmp", "jpg", "png", "ico"];
        var txt = ["txt", "xml", "json"];
        this.removeDom("edit");
        this.removeDom("show"); //移除已经存在的dom结构
        if (arrimg.indexOf(type) > -1) {
            var img = document.createElement("img");
            img.id = "show";
            img.style.width = "100%";
            img.style.height = "100%";
            img.src = "/watchFile.do?path=" + file;
            dom.appendChild(img);
        }
        else if (type == "html") {
            var iframe = document.createElement("iframe");
            iframe.style.width = "100%";
            iframe.style.height = "100%";
            iframe.id = "show";
            iframe.src = "/watchFile.do?path=" + file;
            dom.appendChild(iframe);
        }
        else if (txt.indexOf(type) > -1) {
            var novel = document.createElement("textarea");
            novel.id = "show";
            novel.style.width = "100%";
            novel.style.height = "100%";
            this.query("/edit.do?path=" + file, "get", null, function (data) {
                novel.innerHTML = data;
                dom.appendChild(novel);
            });
        }
        else {
            alert("网页不能查看的文件类型");
        }
    };
    tree.prototype.addButtonOnclick = function (id, url) {
        var _this = this;
        var dom = document.getElementById(id);
        dom.addEventListener("click", function (e) {
            var dom = document.getElementById(id);
            var href = url + "?path=" + dom.name;
            if (id == "but") { //编辑
                _this.eidt(dom.name);
            }
            else if (id == "but0") { //保存。
                var txt = document.getElementById("edit").value;
                _this.query(href + "&data=" + txt, "post", null, function (mark) {
                    if (mark == "ok") {
                        alert("保存成功");
                    }
                    else {
                        alert("保存失败");
                    }
                });
            }
            else if (id == "but1") { //下载
                window.location.href = href;
            }
        }, false);
    };
    tree.prototype.setbuttonName = function (data, id) {
        var dom = document.getElementById(id);
        dom.name = data;
    };
    tree.prototype.eidt = function (file) {
        var txt = ["txt", "xml", "json", "html"];
        var type = file.substring(file.indexOf(".") + 1).trim();
        if (txt.indexOf(type) == -1) {
            alert("网页不能编辑的类型");
            return;
        }
        this.removeDom("edit");
        this.removeDom("show");
        var dom = document.getElementById("right");
        var domtxt = document.createElement("textarea");
        domtxt.id = "edit";
        domtxt.style.width = "100%";
        domtxt.style.height = "100%";
        this.query("/edit.do?path=" + file, "get", null, function (data) {
            domtxt.innerHTML = data;
            dom.appendChild(domtxt);
        });
    };
    tree.prototype.dropClick = function (e) {
        // let id = e.path[3].id;
        var id = e.target.parentNode.parentNode.id;
        var dom = document.getElementById(id); //li的dom;
        // 切换样式状态
        if (dom.className.indexOf("to_roate") > -1) {
            dom.className = ""; //改变样式
            e.target.src = "close.gif"; //改变图标
            if (e.target.parentNode.parentNode.childElementCount > 2) //修改错位的东西。
                e.target.parentNode.parentNode.lastElementChild.style.display = "none";
            // dom.style.display = "none";  //隐藏东西
        }
        else {
            dom.className = "to_roate";
            e.target.src = "expend.gif";
            // e.path[3].style.display = "block"; //显示。
            e.target.parentNode.parentNode.lastElementChild.style.display = "block";
            e.target.parentNode.nextElementSibling.style.display = "inline-block"; //防止图标错位。
        }
        if (Number(e.target.parentNode.parentNode.childElementCount) > 2) { //结点已经存在
            return;
        }
        this.query("/show.do?path=" + e.target.parentNode.title, "get", null, function (data) {
            var item = new Item(id, "", "", true, 0);
            item.addTreeItem(id, "", data, dom);
        });
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
        // var num = ((JSON.parse(json[0]).leave) - 1) * 3;
        var num = 0;
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
            var sp0 = '<span class="file"  title=' + path + '> <i>  <img src="null.gif" class="icon"></i></span>'; //文件
            var sp1 = '<span class="to__dropdownList"  title=' + path + '> <img src="close.gif" class="icon"> </span>'; // 文件夹
            var sp2 = '<span ><img src="file.png"  class="icon" value="' + path + ' "/>  <div class="to_name" title="' + path + '">' + name_1 + "</div></span>";
            var sp3 = '<span >  <img  src="dir.png" class="icon">  <div class="to_name">' + name_1 + '</div></span>';
            var temp = "";
            if (isdir) {
                temp = '<li id="' + leave + name_1 + '">' + sp1 + sp3 + '</li>'; //文件夹的处理
            }
            else {
                temp = '<li id="' + leave + name_1 + '">' + sp0 + sp2 + '</li>'; //文件的处理。
            }
            ulhtml += temp;
        }
        ulmdom.innerHTML = ulhtml;
        dom.appendChild(ulmdom);
        t.domClick(); //添加点击事件。
        return arry;
    };
    return Item;
}());
