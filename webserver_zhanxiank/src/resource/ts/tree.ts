

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

                if (className == "to__dropdownList") {
                    this.dropClick(e);
                }


                let dom = <HTMLElement>self.getTargetDom(e);
                let item = self.allDoms.get(dom);
            }
        }

    }



    public dropClick(e: any) {  //点击事件。

        let dom = document.getElementById(e.path[3].id);  //li的dom;

        this.query(e[2].name,"get","ull",function(data){

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

            dom.style.display = "none";  //隐藏东西
        }
        else {
            dom.style.display = "black"; //显示。
        }
    }



}





class Item {

    parentid: string;
    id: string; //id 标签。
    name: string;
    isdir: boolean;
    path:string="d:/";
    leave: number;
    isExpend: boolean = false;


    constructor(parentid: string, id: string, name: string, isdir: boolean, leave: number) {
        this.parentid = parentid;
        this.id = id;
        this.name = name;
        this.isdir = isdir;
        this.leave = leave;
    }


    public setPath(path:string){
        this.path=path;
    }

    public addTreeItem(pid: string, id: string, data: string, dom: HTMLElement) {
        if (data == null) {
            return;
        }

        var arry = new Array();
        var json = JSON.parse(data);
        let ulmdom = document.createElement("ul");
        ulmdom.id = id;
        var ulhtml = '';
        for (let i = 0; i < json.length; i++) {

            let j = JSON.parse(json[i]);
            let name = j.name;
            let isdir = j.isdir == "true";
            let leave = Number(j.leave);
            let path=j.path;
            var item = new Item(pid, id, name, isdir, leave);
            this.setPath(path); //路径
            arry.push(item);
            let sp1 = '<span class="to__dropdownList"  name='  +path+ ' > <i onclick="dropClick(this)">  <img src="expend.gif" class="icon"></i></span>';
            let sp2 = '<span ><input type="checkbox" name="input" value="' + name + '" onclick="checkboxClick(this)" /> <div class="to_name">' + name + '</div></span>';
            let temp = '<li id="' + leave + name + '">' + sp1 + sp2 + '</li>';
            ulhtml += temp;
        }

        ulmdom.innerHTML = ulhtml;
        dom.appendChild(ulmdom);

        return arry;

    }








}
