package com.calc;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CalcServlet", urlPatterns = {"/CalcServlet"})
public class CalcServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    int calcParam1 = Integer.parseInt(request.getParameter("atai1"));
    int calcParam2 = Integer.parseInt(request.getParameter("atai2"));
    String arithmeticOperationParam = request.getParameter("keisan");
    double calcParamResult = 0;
    String exceptionComment;

    // communicate the result of authentication
    ServletContext sc = getServletContext();
    boolean arithmeticDone = false;
    try {
      switch (serchArithmeticOperation(arithmeticOperationParam)) {
        case "addition":
          calcParamResult = calcParam1 + calcParam2;
          break;
        case "subtraction":
          calcParamResult = calcParam1 - calcParam2;
          break;
        case "multiplication":
          calcParamResult = calcParam1 * calcParam2;
          break;
        case "division":
          if (calcParam2 == 0) {
            throw new ArithmeticException();
          } else {
            calcParamResult = calcParam1 / calcParam2;
          }
          break;
        case "remainder":
          if (calcParam2 == 0) {
            throw new ArithmeticException();
          } else {
            calcParamResult = calcParam1 % calcParam2;
          }
          break;
        case "exponentiation":
          calcParamResult = Math.pow(calcParam1, calcParam2);
          break;
      }
      arithmeticDone = true;
    } catch (NullPointerException e) {
      exceptionComment = "値1は存在しません: " + e.getMessage();
      request.setAttribute("comment", exceptionComment);
    } catch (NumberFormatException e) {
      exceptionComment = "値1は数値化できません: " + e.getMessage();
      request.setAttribute("comment", exceptionComment);
    } catch (ArithmeticException e) {
      exceptionComment = "0除算はできません";
      request.setAttribute("comment", exceptionComment);
    } catch (Exception e) {
      exceptionComment = "予期せぬ例外が発生しました: " + e.getMessage();
      request.setAttribute("comment", exceptionComment);
    } finally {
      request.setAttribute("atai1", String.valueOf(calcParam1));
      request.setAttribute("atai2", String.valueOf(calcParam2));
      request.setAttribute("kigou", arithmeticOperationParam);

      if (arithmeticDone) {
        request.setAttribute("kotae", String.valueOf(calcParamResult));
        sc.getRequestDispatcher("/CalcJSPOK.jsp").forward(request, response);
      } else {
        sc.getRequestDispatcher("/CalcJSPNG.jsp").forward(request, response);
      }
    }
  }

  private String serchArithmeticOperation(String arithmeticOperationParam) {
    String result = "";
    String[][] arithmeticOperations = {
        {"1", "addition"},
        {"2", "subtraction"},
        {"3", "multiplication"},
        {"4", "division"},
        {"5", "remainder"},
        {"6", "exponentiation"}
    };
    for (String[] arithmeticOperation : arithmeticOperations) {
      if (arithmeticOperationParam.equals(arithmeticOperation[0])) {
        result = arithmeticOperation[1];
      }
    }
    return result;
  }
}
