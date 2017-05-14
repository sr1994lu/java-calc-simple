package com.calc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
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
    ServletContext sc = getServletContext();
    double calcParamResult = 0;
    List<String> exceptionComment = new ArrayList<>();
    boolean arithmeticDone = false;
    // communicate the result of authentication
    String OptNullabbleCalcParam1 = hasAtai(request, "atai1").orElse("");
    String OptNullabbleCalcParam2 = hasAtai(request, "atai2").orElse("");
    try {
      if (!hasAtai(request, "atai1").isPresent()
          && !hasAtai(request, "atai2").isPresent()) {
        // do not have textboxes
        exceptionComment.add("値1のテキストボックスが存在しません");
        exceptionComment.add("値2のテキストボックスが存在しません");
        throw new NullPointerException();

      } else if (!hasAtai(request, "atai1").isPresent()) {
        // do not have textbox
        exceptionComment.add("値1のテキストボックスが存在しません");
        throw new NullPointerException();

      } else if (!hasAtai(request, "atai2").isPresent()) {
        // do not have textbox
        exceptionComment.add("値2のテキストボックスが存在しません");
        throw new NullPointerException();

      } else {
        // have textbox
        if (Objects.equals(OptNullabbleCalcParam1, "")
            && Objects.equals(OptNullabbleCalcParam2, "")) {
          // not yet entered
          exceptionComment.add("値1のテキストボックスが未入力です");
          exceptionComment.add("値2のテキストボックスが未入力です");

        } else if (Objects.equals(OptNullabbleCalcParam1, "")) {
          // not yet entered
          exceptionComment.add("値1のテキストボックスが未入力です");

        } else if (Objects.equals(OptNullabbleCalcParam2, "")) {
          // not yet entered
          exceptionComment.add("値2のテキストボックスが未入力です");

        }

        // entered
        Pattern p = Pattern.compile("^([1-9]\\d*|0)(\\.\\d+)?$");
        if (!p.matcher(OptNullabbleCalcParam1).find()
            && !p.matcher(OptNullabbleCalcParam2).find()) {
          // cannot be numerical
          exceptionComment.add("値1は数値化できません");
          exceptionComment.add("値2は数値化できません");
          throw new NumberFormatException();

        } else if (!p.matcher(OptNullabbleCalcParam1).find()) {
          // cannot be numerical
          exceptionComment.add("値1は数値化できません");
          throw new NumberFormatException();

        } else if (!p.matcher(OptNullabbleCalcParam2).find()) {
          // cannot be numerical
          exceptionComment.add("値2は数値化できません");
          throw new NumberFormatException();

        } else {
          // can be numerical
          Optional<String> OptArithmeticOperationParam = Optional
              .of(request.getParameter("keisan"));
          double calcParam1 = Double.parseDouble(OptNullabbleCalcParam1);
          double calcParam2 = Double.parseDouble(OptNullabbleCalcParam2);
          String arithmeticOperationParam = OptArithmeticOperationParam.get();
          String calcSign = serchArithmeticOperation(arithmeticOperationParam);

          // start calculation
          switch (calcSign) {
            case "addition":
              calcParamResult = calcParam1 + calcParam2;
              arithmeticDone = true;
              break;
            case "subtraction":
              calcParamResult = calcParam1 - calcParam2;
              arithmeticDone = true;
              break;
            case "multiplication":
              calcParamResult = calcParam1 * calcParam2;
              arithmeticDone = true;
              break;
            case "division":
              calcParamResult = calcParam1 / calcParam2;
              arithmeticDone = true;
              if (Double.isInfinite(calcParamResult) || Double.isNaN(calcParamResult)) {
                exceptionComment.add("0除算はできません");
                arithmeticDone = false;
                break;
              } else {
                break;
              }
            case "remainder":
              calcParamResult = calcParam1 % calcParam2;
              arithmeticDone = true;
              if (Double.isInfinite(calcParamResult) || Double.isNaN(calcParamResult)) {
                exceptionComment.add("0除算はできません");
                arithmeticDone = false;
                break;
              } else {
                break;
              }
            case "exponentiation":
              calcParamResult = Math.pow(calcParam1, calcParam2);
              arithmeticDone = true;
              break;
          }
        }

      }
    } catch (Exception e) {
      Optional<String> eMsg = Optional.ofNullable(e.getMessage());
      exceptionComment.add(eMsg.orElse(""));
    } finally {
      Optional<String> OptCalcParam1 = Optional.of(request.getParameter("atai1"));
      Optional<String> OptCalcParam2 = Optional.of(request.getParameter("atai2"));
      Optional<String> OptArithmeticOperationParam = Optional.of(request.getParameter("keisan"));
      String arithmeticOperationParam = OptArithmeticOperationParam.get();
      String calcSign = serchArithmeticOperation(arithmeticOperationParam);
      if (arithmeticDone) {
        request.setAttribute("atai1", String.valueOf(OptCalcParam1.get()));
        request.setAttribute("atai2", String.valueOf(OptCalcParam2.get()));
        request.setAttribute("kigou", calcSign);
        request.setAttribute("kotae", String.valueOf(calcParamResult));
        sc.getRequestDispatcher("/CalcJSPOK.jsp").forward(request, response);
      } else {
        request.setAttribute("atai1", String.valueOf(OptCalcParam1.get()));
        request.setAttribute("atai2", String.valueOf(OptCalcParam2.get()));
        request.setAttribute("kigou", calcSign);
        request.setAttribute("comment", String.join("<br>", exceptionComment));
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

  private Optional<String> hasAtai(HttpServletRequest request, String param) {
    return Optional.ofNullable(request.getParameter(param));
  }
}
