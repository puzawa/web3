<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Web Lab 2</title>

    <link rel="stylesheet" href="static/css/reset.css">
    <link rel="stylesheet" href="static/css/styles.css">

</head>

<body>

<header  class="header">
    <div class="container">
        <table class="header__inner">
            <tr>
                <td id="student-name">Плотников Алексей Алексеевич</td>
                <td class="group">P3214</td>
                <td class="option">Вариант 42321</td>
            </tr>
        </table>
    </div>
</header>

<main>
    <div class="error_msg">
        Sry. Something went wrong (
        <br>
        <%= request.getAttribute("error") %>
    </div>
    <br>
    <div class="home-link">
        <a href="index.jsp">На главную</a>
    </div>
</main>

</body>
</html>
