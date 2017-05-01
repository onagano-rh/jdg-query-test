package com.myapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/jdg")
public class JDGTest extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(JDGTest.class);

    public void init() throws ServletException {
        // Do required initialization
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Mass indexer start");
        SearchManager searchManager = Search.getSearchManager(CacheHolder.getIvdPositionCache());
        searchManager.getMassIndexer().start();
        logger.info("Mass indexer end");
    }

    public void destroy() {
        // do nothing.
    }
}
