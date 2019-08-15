

class tree {

    id: string;
    leave: number;

    constructor(id: string, leave: number) {
        this.id = id;
        this.leave = leave;
    }


    public init(url: string, way: string, path: string) {
        this.query(url, way, path, (data: any) => {

            this.addRootItem(data, this.getDom());
            this.domClick();
        });


    }

    public getDom() {
        return document.getElementById("left");
    }



    /**
    *   查询方法，封装了ajax的请求
    * @param url  
    * @param method 
    * @param data 
    * @param result 
    */

    public query(url: string, method: string, data: string, result: Function) {
        const xhr = new XMLHttpRequest();  //创建对象

        xhr.open(method, url, true);   //true异步请求，false同步

        if (method == "post") {
            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //请求头部
        }

        xhr.send(data);
        xhr.onreadystatechange = () => {   //回调函数
            if (xhr.readyState == 4 && xhr.status == 200) {
                let data = xhr.response || xhr.responseText
                // alert(data);
                result(data);   //处理数据的函数
            }

        }

    }

    //第一层结点。
    public addRootItem(data: string, dom: HTMLElement) {
        let item = new Item("-1", "0", "root", false, 0);
        item.addTreeItem("-1", "0", data, this.getDom());

    }


    public domClick() {    //点击事件
        // 选中子元素所有input框
        var dom = document.getElementsByTagName("span");
        for (let i = 0; i < dom.length; i++) {
            var self = this;

            dom[i].onclick = (e) => {
                var subItem = e.path;
                var obj = self;
                if (subItem == null) {
                    return;
                }
                let name = subItem[0].neme;
                let value = subItem[0].value; //选择输入框
                let txt = subItem[0].innerHTML; //点击文件名
                let className = subItem[2].className;  //展开和伸缩。

                let node = e.target; //点击的元素。

                if (className == "to__dropdownList") {  //文件夹才会展开。
                    this.dropClick(e);
                }
                if (txt != null && txt != "") {         //查看文件，点击文件名字。
                    this.seeFile(e);
                }

                if (value != null && value != "") {  //勾选按钮 是编辑文件东西。
                    this.eidt(e);
                }

            }
        }

    }


    public  removeDom(id:string){

        var key = document.getElementById(id);
        if (key)  //清楚本身存在的dom结构。
        {
            let parentNode = key.parentNode;
            parentNode.removeChild(key);
        }
    }

    public seeFile(e) {  //查看文件
        // this.query("/watchFile.do?path="+e.path[2].title,"get",null,function(data){ 

        var dom = document.getElementById("right");
        // let file=e.path[2].title;
        var file = e.path[0].title;

        let index = file.indexOf(".");
        if (index == -1) {
            return;  //不查看
        }
        let type = file.substring(index + 1).trim();
        let arrimg = ["gif", "bmp", "jpg", "png", "ico"];
        let txt = ["txt", "xml", "json"];

        this.removeDom("show"); //移除已经存在的dom结构
        if (arrimg.indexOf(type) > -1) {
            var img = document.createElement("img");
            img.src = "/watchFile.do?path=" + file;
            img.id = "show";
            dom.appendChild(img);
        }
        else if (type == "html") {
            var iframe = document.createElement("iframe");
            // iframe.width = "100%";
            iframe.id = "show";
            iframe.src = "/watchFile.do?path=" + file;
            dom.appendChild(iframe);

        } else if (txt.indexOf(type) > -1) {
            var novel= document.createElement("textarea");
             novel.id="show";
            this.query("/watchFile.do?path=" + file, "get", null, function (data) {
                novel.innerHTML = data;
            });

        }
        else {
            alert("网页不能查看的文件类型");
        }

    }


    public eidt(e) { //编辑文件

        this.removeDom("show"); //移除已经dom结构。
        let file = e.path[0].value;
        let txt = ["txt", "xml", "json", "html"];
        let type = file.substring(file.indexOf(".") + 1).trim();

        if (txt.indexOf(type) == -1) {
            alert("网页不能编辑的类型");
            return;
        }
        var domtxt = document.getElementById("txt");
        this.query("/watchFile.do?path=" + file, "get", null, function (data) {
            domtxt.innerHTML = data;
        });

    }

    public dropClick(e: any) {  //点击事件。
        let id = e.path[3].id;
        let dom = document.getElementById(id);  //li的dom;
        // 切换样式状态
        if (dom.className.indexOf("to_roate") > -1) {
            dom.className = ""; //改变样式
            e.path[0].src = "close.gif"; //改变图标
            e.path[3].lastElementChild.style.display = "none";
            // dom.style.display = "none";  //隐藏东西
        }
        else {
            dom.className = "to_roate";
            e.path[0].src = "expend.gif";
            // e.path[3].style.display = "block"; //显示。
            e.path[3].lastElementChild.style.display = "block";
        }

        if (Number(e.path[3].childElementCount) > 2) {  //结点已经存在

            return;
        }

        this.query("/show.do?path=" + e.path[2].title, "get", null, function (data) {  //追加结点
            var item = new Item(id, "", "", true, 0);
            item.addTreeItem(id, "", data, dom);
        });

    }

}





class Item {

    parentid: string;
    id: string; //id 标签。
    name: string;
    isdir: boolean;
    path: string = "d:/";
    leave: number;
    isExpend: boolean = false;


    constructor(parentid: string, id: string, name: string, isdir: boolean, leave: number) {
        this.parentid = parentid;
        this.id = id;
        this.name = name;
        this.isdir = isdir;
        this.leave = leave;
    }


    public setPath(path: string) {
        this.path = path;
    }

    public addTreeItem(pid: string, id: string, data: string, dom: HTMLElement) {
        if (data == null) {
            return;
        }

        let t = new tree(null, 0);  //使用对象，
        var arry = new Array();
        var json = JSON.parse(data);
        let ulmdom = document.createElement("ul");
        ulmdom.id = pid;
        var num = ((JSON.parse(json[0]).leave) - 1) * 16;
        ulmdom.style.cssText = "margin-left:" + num + "px;";
        var ulhtml = '';
        for (let i = 0; i < json.length; i++) {

            let j = JSON.parse(json[i]);
            let name = j.name;
            let isdir = j.isdir == "true";
            let leave = Number(j.leave);
            let path = j.path;
            var item = new Item(pid, id, name, isdir, leave);
            this.setPath(path); //路径
            arry.push(item);

            let sp0 = '<span class="file"  title=' + path + '> <i>  <img src=""></i></span>';  //文件
            let sp1 = '<span class="to__dropdownList"  title=' + path + '> <i>  <img src="close.gif" class="icon"></i></span>';  // 文件夹
            let sp2 = '<span ><input type="checkbox" name="input" value="' + path + ' "/><div class="to_name" title="' + path + '">' + name + "</div></span>";
            let sp3 = '<span > <div class="to_name">' + name + '</div></span>';

            let temp = "";

            if (isdir) {
                temp = '<li id="' + leave + name + '">' + sp1 + sp3 + '</li>'; //文件夹的处理
            } else {
                temp = '<li id="' + leave + name + '">' + sp0 + sp2 + '</li>';  //文件的处理。
            }
            ulhtml += temp;
        }

        ulmdom.innerHTML = ulhtml;
        dom.appendChild(ulmdom);
        t.domClick(); //添加点击事件。
        return arry;
    }


}
