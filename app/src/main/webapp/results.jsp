<%@ page import="web2.TableRow" %>
<%@ page errorPage="error.jsp" %>
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

<header class="header">
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
    <div class="archive">
        <h2 class="archive__heading">Последняя точка</h2>
        <div class="archive__table-wrapper">
            <table class="archive__table table-archive">
                <thead class="table-archive__head">
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Hit</th>
                </tr>
                </thead>
                <tbody class="table-archive__main">

                <% Object resultRow = request.getAttribute("newRow"); %>
                <% if (resultRow != null) {
                    TableRow row = (TableRow) resultRow;
                %>
                <tr>
                    <td><%= row.getX() %>
                    </td>
                    <td><%= row.getY() %>
                    </td>
                    <td><%= row.getR() %>
                    </td>
                    <td><%= row.getResult() ? "Hit" : "Miss" %>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <div class="home-link">
        <a href="index.jsp">На главную</a>
    </div>
</main>

</body>
</html>
