
 class utils{
    /**
 *   查询方法，封装了ajax的请求
 * @param url  
 * @param method 
 * @param data 
 * @param result 
 */

public   query(url:string,method:string,data:string,result:Function){
   const xhr=new XMLHttpRequest();  //创建对象
    
   xhr.open(method,url,true);   //true异步请求，false同步

   if(method=="post")
   {
       xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //请求头部
   }
  
   xhr.send(data);
  xhr.onreadystatechange=()=>{   //回调函数
    if(xhr.readyState==4&&xhr.status==200){
       let data=xhr.response||xhr.responseText
       alert(data);   
       result(data);   //处理数据的函数

    }

  }

}



public download(){
   var dom=document.getElementById("button2");

}




}


