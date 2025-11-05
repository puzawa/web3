<%@ page import="java.util.List" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="web2.TableRow" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="pointsBean" class="web2.PointsBean" scope="session"/>

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
                <td id="student-name">Плотников Алексей Алексеевич web 3</td>
                <td class="group">P3214</td>
                <td class="option">Вариант 42321</td>
            </tr>
        </table>
    </div>
</header>

<script>
    <%
    List<TableRow> temp_rows = pointsBean.getHistory();
    TableRow lastRow = (temp_rows != null && !temp_rows.isEmpty()) ? temp_rows.get(temp_rows.size() - 1) : null;
%>
    const points = [
        <%
            if (temp_rows != null && !temp_rows.isEmpty()) {
                for (int i = 0; i < temp_rows.size(); i++) {
                    TableRow row = temp_rows.get(i);
        %>
        {
            x: <%= row.getX() %>,
            y: <%= row.getY() %>,
            r: <%= row.getR() %>,
            result: '<%= row.getResult() %>'
        }<%= (i < temp_rows.size() - 1) ? "," : "" %>
        <%
                }
            }
        %>
    ];
    const lastPoint = <%= (lastRow != null)
        ? "{ x: " + lastRow.getX() + ", y: " + lastRow.getY() + ", r: " + lastRow.getR() + ", result: " + lastRow.getResult() + " }"
        : "null" %>;

    window.selectedRValue = null;
    if (lastPoint != null)
        window.selectedRValue = lastPoint.r;
</script>

<main class="main">
    <div class="container">
        <div class="main__inner">
            <div class="form">
                <form class="form__body" method="get" onsubmit="exchange(event)">
                    <div class="button-column" id="buttonColumnX">
                        <p class="form-text">X</p>
                    </div>
                    <script defer>
                        const containerX = document.getElementById('buttonColumnX');
                        const buttonsX = [];
                        window.selectedXValue = null;
                        for (let i = -3; i <= 5; i++) {
                            const btn = document.createElement('button');
                            btn.type = 'button';
                            btn.textContent = i;

                            if (i === window.selectedXValue) {
                                btn.classList.add('selected');
                            }

                            btn.addEventListener('click', () => {
                                if (btn.classList.contains('selected')) {
                                    btn.classList.remove('selected');
                                    window.selectedXValue = null;
                                } else {
                                    buttonsX.forEach(b => b.classList.remove('selected'));
                                    btn.classList.add('selected');
                                    window.selectedXValue = i;

                                    handlePoint();
                                }
                                console.log('Selected X:', window.selectedXValue);
                            });

                            containerX.appendChild(btn);
                            buttonsX.push(btn);
                        }
                    </script>

                    <div class="Y-input">
                        <p class="form-text">Y</p>
                        <label>
                            <input type="text" name="Y-input" placeholder="Введите Y" class="Y-input__input form-input">
                        </label>
                    </div>

                    <div class="button-column" id="buttonColumnR">
                        <p class="form-text">R</p>
                    </div>
                    <script defer>
                        const containerR = document.getElementById('buttonColumnR');
                        const buttonsR = [];


                        for (let i = 1; i <= 3; i += 0.5) {
                            const btn = document.createElement('button');
                            btn.type = 'button';
                            btn.textContent = i;

                            if (i === window.selectedRValue) {
                                btn.classList.add('selected');
                            }

                            btn.addEventListener('click', () => {
                                if (btn.classList.contains('selected')) {
                                    btn.classList.remove('selected');
                                    window.selectedRValue = null;
                                } else {
                                    buttonsR.forEach(b => b.classList.remove('selected'));
                                    btn.classList.add('selected');
                                    window.selectedRValue = i;

                                    handlePoint();
                                }
                                console.log('Selected R:', window.selectedRValue);
                            });

                            containerR.appendChild(btn);
                            buttonsR.push(btn);
                        }
                    </script>
                    <input type="submit" class="form-submit" disabled>
                </form>
            </div>

            <div class="coordinate-plane">
                <canvas id="coordinate-plane"></canvas>
            </div>

            <div class="archive">
                <h2 class="archive__heading">История</h2>
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

                        <%
                            List<TableRow> rows = pointsBean.getHistory();
                            if (rows != null) {
                                for (TableRow row : rows) {
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

                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>


        </div>
    </div>
</main>

<script src="static/js/canvas.js"></script>
<script src="static/js/sendRequest.js"></script>

</body>
</html>