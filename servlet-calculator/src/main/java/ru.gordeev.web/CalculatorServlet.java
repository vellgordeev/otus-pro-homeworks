package ru.gordeev.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CalculatorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();
        double num1 = Double.parseDouble(request.getParameter("num1"));
        double num2 = Double.parseDouble(request.getParameter("num2"));
        double result = 0;

        switch (path) {
            case "/add":
                result = num1 + num2;
                break;
            case "/subtract":
                result = num1 - num2;
                break;
            case "/multiply":
                result = num1 * num2;
                break;
            case "/div":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h1>Division by zero is not allowed</h1>");
                    out.println("</body></html>");
                    return;
                }
                break;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Result: " + result + "</h1>");
        out.println("</body></html>");
    }
}