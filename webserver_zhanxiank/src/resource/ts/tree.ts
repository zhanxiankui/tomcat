

class tree {

    id: string;
    leave: number;

    constructor(id: string, leave: number) {
        this.id = id;
        this.leave = leave;
    }


    public init(url: string, way: string, path: string) {
        this.query(url, way, path, (data: any) => {
            this.addRootItem(data);
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
                alert(data);
                result(data);   //处理数据的函数

            }

        }

    }


    public addRootItem(data: string) {

        if (data == null) {
            return;
        }

       // 添加标题
       var itemdom= document.createElement("div")
       var arrow = '<span class="to__dropdownList" ><i onclick="dropClick(this)"><svg t="1550632829702" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1783" xmlns:xlink="http://www.w3.org/1999/xlink" width="100%" height="100%"><defs><style type="text/css"></style></defs><path d="M959.52557 254.29773 511.674589 702.334953 63.824631 254.29773Z" p-id="1784"></path></svg></i></span>'

       

        var json = JSON.parse(data);
        for (let i = 0; i < data.length; i++) {
            let j = JSON.parse(json[i]);
            let name = j.name;
            let isdir = j.isdir;
            let leave = j.leave;

            itemdom.innerHTML = '<div class="to__item level-' + i+ '">' + arrow + '<span onclick="domClick(this)"><input type="checkbox" name="cName" value="' +name+ '" onclick="checkboxClick(this)" /><div class="to__name">' +name+ '</div></span></div>'
            this.getDom().appendChild(itemdom);
       
        }
        // let item=new Item();


    }



}



class Item {

    parentid: string;
    id: string; //id 标签。
    name: string;
    isdir: boolean;
    leave: number;
    isExpend: boolean = false;


    constructor(parentid: string, id: string, name: string, isdir: boolean, leave: number) {
        this.parentid = parentid;
        this.id = id;
        this.name = name;
        this.isdir = isdir;
        this.leave = leave;
    }






}
