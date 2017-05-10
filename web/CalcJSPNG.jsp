<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String strAtai1 = (String) request.getAttribute("atai1");
    String strAtai2 = (String) request.getAttribute("atai2");
    String strKigou = (String) request.getAttribute("kigou");
    String strComment = (String) request.getAttribute("comment");
%>
<!doctype html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Insert title here</title>
</head>
<body>
    <h2>【NG】計算結果画面</h2>
    IW14A703 浅田正雄
    <hr/>
    値１：<%= strAtai1 %><br/>
    演算：<%= strKigou %><br/>
    値２：<%= strAtai2 %><br/><br/>
    コメント：<br/><%= strComment %><br/>
</body>
</html>
