package com.myapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.TransactionMode;

@WebListener
public class SampleContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Starting up!");

        GlobalConfiguration glob = new GlobalConfigurationBuilder()
                .clusteredDefault()
                .transport().addProperty("configurationFile", "jgroups-tcp.xml")
                .globalJmxStatistics().allowDuplicateDomains(true).enable()
                .build();
        
        Configuration loc = new ConfigurationBuilder()
                .jmxStatistics().enable()
                .clustering().cacheMode(CacheMode.DIST_ASYNC).hash().numOwners(2)
                .indexing()
                    .index(Index.LOCAL)
                    .addProperty("default.directory_provider", "infinispan")
                    .addProperty("lucene_version", "LUCENE_CURRENT")
                    .addProperty("default.indexmanager", "org.infinispan.query.indexmanager.InfinispanIndexManager")
                .build();

        Configuration indexREPL = new ConfigurationBuilder()
                .jmxStatistics().enable()
                .clustering().cacheMode(CacheMode.REPL_SYNC)
                .transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
                .indexing().index(Index.NONE)
                .build();
        
        Configuration indexDIST = new ConfigurationBuilder()
                .jmxStatistics().enable()
                .clustering().cacheMode(CacheMode.DIST_SYNC).hash().numOwners(2)
                .transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
                .indexing().index(Index.NONE)
                .build();

        DefaultCacheManager manager = new DefaultCacheManager(glob, loc, false);
        manager.defineConfiguration("LuceneIndexesMetadata", indexREPL);
        manager.defineConfiguration("LuceneIndexesData", indexDIST);
        manager.defineConfiguration("LuceneIndexesLocking", indexREPL);
        manager.start();
        CacheHolder.setManager(manager);

        Cache c = CacheHolder.getManager().getCache();
        CacheEntity entity = new CacheEntity();
        entity.setName("Test01");
        entity.setPhone("0123456789");
        c.put("1", entity);
        CacheHolder.setIvdPositionCache(c);

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Shutting down!");
    }
}
