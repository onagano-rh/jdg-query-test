package com.myapp;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.Query;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/jdg2")
public class JDGTest2 extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(JDGTest2.class);

    public void init() throws ServletException {
        // Do required initialization
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Lucene query started");

        SearchManager searchManager = Search.getSearchManager(CacheHolder.getIvdPositionCache());
        
        Query query = searchManager.buildQueryBuilderForClass(CacheEntity.class).get()
                .keyword()
                    .wildcard()
                .onField("name")
                .matching("Test*").createQuery(); // throwing exception

        List<Object> list = searchManager.getQuery(query, CacheEntity.class).list();
        if (list == null || list.isEmpty()) {
            logger.info("Unable to find using:" + "Test");
        } else if (list.size() > 0) {
            logger.info("Found  result:" + list.get(0));
        }

        logger.info("Lucene query updated");
    }

    public void destroy() {
        // do nothing.
    }
}
