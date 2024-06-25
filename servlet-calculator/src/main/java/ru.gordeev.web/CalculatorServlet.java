package ru.gordeev.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();
        double num1, num2, result;
        try {
            num1 = Double.parseDouble(request.getParameter("num1"));
            num2 = Double.parseDouble(request.getParameter("num2"));
        } catch (NumberFormatException e) {
            sendErrorMessage(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid number");
            return;
        }

        switch (path) {
            case "/div":
                if (num2 == 0) {
                    sendErrorMessage(response, HttpServletResponse.SC_BAD_REQUEST, "Division by zero");
                    return;
                }
                result = num1 / num2;
                break;
            case "/add":
                result = num1 + num2;
                break;
            case "/subtract":
                result = num1 - num2;
                break;
            case "/multiply":
                result = num1 * num2;
                break;
            default:
                sendErrorMessage(response, HttpServletResponse.SC_NOT_FOUND, "Math operation not found");
                return;
        }

        sendResultMessage(response, result);
    }

    private void sendResultMessage(HttpServletResponse response, double result) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Result: " + result + "</h1>");
        out.println("</body></html>");
    }

    private void sendErrorMessage(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("text/html");
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }
}