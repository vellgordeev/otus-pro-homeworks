<!DOCTYPE html>
<html>
<head>
    <title>Online Calculator</title>
</head>
<body>
    <h1>Online Calculator</h1>
    <form action="${pageContext.request.contextPath}/add" method="get">
        <input type="text" name="num1" placeholder="Number 1">
        <input type="text" name="num2" placeholder="Number 2">
        <button type="submit">Add</button>
    </form>
    <form action="${pageContext.request.contextPath}/subtract" method="get">
        <input type="text" name="num1" placeholder="Number 1">
        <input type="text" name="num2" placeholder="Number 2">
        <button type="submit">Subtract</button>
    </form>
    <form action="${pageContext.request.contextPath}/multiply" method="get">
        <input type="text" name="num1" placeholder="Number 1">
        <input type="text" name="num2" placeholder="Number 2">
        <button type="submit">Multiply</button>
    </form>
    <form action="${pageContext.request.contextPath}/div" method="get">
        <input type="text" name="num1" placeholder="Number 1">
        <input type="text" name="num2" placeholder="Number 2">
        <button type="submit">Divide</button>
    </form>
</body>
</html>