package zjucst.arch.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {

    public static void responseWithStatus(HttpServletResponse response, String data, Integer status) {
        if (status != -1)
            response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

