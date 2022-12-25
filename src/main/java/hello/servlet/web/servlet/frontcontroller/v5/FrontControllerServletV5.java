package hello.servlet.web.servlet.frontcontroller.v5;

import hello.servlet.web.servlet.frontcontroller.ModelView;
import hello.servlet.web.servlet.frontcontroller.MyView;
import hello.servlet.web.servlet.frontcontroller.v3.ControllerV3;
import hello.servlet.web.servlet.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.servlet.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.servlet.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.servlet.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.servlet.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.servlet.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.servlet.frontcontroller.v5.adaptor.ControllerV3HanlderAdapter;
import hello.servlet.web.servlet.frontcontroller.v5.adaptor.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdaptors();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/newform", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberListControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v4/members/newform", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberListControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberSaveControllerV4());
    }

    private void initHandlerAdaptors() {
        handlerAdapters.add(new ControllerV3HanlderAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        if(handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);

    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
