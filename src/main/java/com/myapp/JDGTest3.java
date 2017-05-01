package com.myapp;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/jdg3")
public class JDGTest3 extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(JDGTest3.class);

    public void init() throws ServletException {
        // Do required initialization
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Infinispan query started");
        
        QueryFactory queryFactory = Search.getQueryFactory(CacheHolder.getIvdPositionCache());

        Query q = queryFactory.from(CacheEntity.class)
                    .having("name").like("Test%")
                    .toBuilder().build();

        List<CacheEntity> list = q.list();        
        if (list == null || list.isEmpty()) {
            logger.info("Unable to find using:" + "Test");
        } else if (list.size() > 0) {
            logger.info("Found  result:" + list.get(0));
        }

        logger.info("Infinispan query updated");
    }

    public void destroy() {
        // do nothing.
    }
}
