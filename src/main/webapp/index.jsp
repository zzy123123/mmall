<%@page contentType="text/html;charset=utf-8" language="java" %>
<meta http-equiv=”Content-Type” content=”text/html; charset=utf-8″><html>
<body>
<h2>Hello World!</h2>

springmvc上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springmvc上传文件">
</form>

富文本图片上传文件
<form name="form1" action="/manage/product/rich_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本图片上传文件">
</form>
</body>
</html>
